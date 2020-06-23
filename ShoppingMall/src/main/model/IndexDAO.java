package main.model;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import member.model.EncryptMyKey;
import util.security.AES256;


public class IndexDAO implements InterIndexDAO{

	private DataSource ds; 
	// DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다. 
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private AES256 aes = null;
	
	// 생성자 
	public IndexDAO() {
		// 암호화/복호화 키 (양방향암호화) ==> 이메일,휴대폰의 암호화/복호화
		String key = EncryptMyKey.KEY;
		try {
		    Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			ds = (DataSource)envContext.lookup("jdbc/semiProject");
			aes = new AES256(key);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
	}
	
	// 사용한 자원을 반납하는 close() 메소드 생성하기
	public void close() {
		try {
			if(rs != null) { rs.close(); rs=null; }
			if(pstmt != null) { pstmt.close(); pstmt=null; } 
			if(conn != null) { conn.close(); conn=null; }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// index.jsp에서 보는 DIV에 나타낼 리스트 출력
	@Override
	public List<ProductVO> listCall(Map<String,String>paraMap) throws SQLException {
		List<ProductVO> productList = new ArrayList<ProductVO>();
		String type = paraMap.get("type");
		try {
			conn = ds.getConnection();
			String sql = "";
			String subSql = " select product_num, product_name, price, stock, to_char(registerdate,'yyyy-mm-dd') as registerdate, representative_img from product_table ";
			
			switch (type) {
			case "sale":
				subSql+=" order by sale desc ";
				break;
			case "best" :
				subSql+="where fk_category_num = ? order by best_point desc";
				break;
			case "new":
				subSql+="order by registerdate desc";
				break;
			default:
				break;
			}
			
			if("random".equals(type)) {
				sql= subSql+" where product_num in(?,?,?,?,?,?,?,?) ";
			}
			else {
				sql="select ROM,product_num, product_name, price, stock, representative_img from (select rownum as ROM, product_num, product_name, price, stock, representative_img  from("+subSql+") )T where T.ROM between 1 and 8 ";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			if("best".equals(type)) pstmt.setString(1, paraMap.get("category"));
			
			else if("random".equals(type)) {
				String num = paraMap.get("random");
				String[] numArr = num.split(",");
				for(int i=0; i<numArr.length; i++) {
					pstmt.setString(i+1, numArr[i]);
				}
			}
			System.out.println(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ProductVO product = new ProductVO();
				product.setProduct_num(rs.getInt("product_num"));
				product.setProduct_name(rs.getString("product_name"));
				product.setPrice(rs.getInt("price"));
				product.setStock(rs.getInt("stock"));
				product.setRepresentative_img(rs.getString("representative_img"));
				productList.add(product);
			}
			
		
		}
		finally {
			close();
		}
		
		
		return productList;
	}

	// 상세페이지에 나타낼 ProductVO 조회
	@Override
	public ProductVO productDetail(String idx) throws SQLException {
		ProductVO product = null;
		
		try {
			conn = ds.getConnection();
			String sql = " select P.product_num, P.product_name, P.price, P.stock, P.origin, P.packing, P.unit, P.representative_img, P.explain, C.category_content , S.subcategory_content" + 
					" from product_table P join product_category_table C \r\n" + 
					" on P.fk_category_num = C.category_num  " + 
					" join product_subcategory_table S\r\n" + 
					" on P.fk_subcategory_num = S.subcategory_num " + 
					" where P.product_num = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, idx);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				System.out.println("DB조회 성공");
				product = new ProductVO();
				product.setProduct_num(rs.getInt(1));
				product.setProduct_name(rs.getString(2));
				product.setPrice(rs.getInt(3));
				product.setStock(rs.getInt(4));
				product.setOrigin(rs.getString(5));
				product.setPacking(rs.getString(6));
				product.setUnit(rs.getString(7));
				product.setRepresentative_img(rs.getString(8));
				product.setExplain(rs.getString(9));
				product.setCategory_content(rs.getString(10));
				product.setSubcategory_content(rs.getString(11));
				
			}
		}
		finally {
			close();
		}
		return product;
	}

	// 모든 상품번호 조회 //
	@Override
	public List<String> product_numFind() throws SQLException {
		List<String> product_numArr = new ArrayList<String>();
		try {
			conn = ds.getConnection();
			String sql = " select product_num from product_table where stock > 0 ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String product_num = rs.getString(1);
				product_numArr.add(product_num);
			}
		}
		finally {
			close();
		}
		return product_numArr;
	}

	// 특정 상품 후기 조회 //
	@Override
	public List<ReviewVO> reviewCall(String product_num) throws SQLException {
		List<ReviewVO> reviewList = new ArrayList<ReviewVO>();
		try {
			conn = ds.getConnection();
			String sql = " select R.review_num, R.subject, R.content, to_char(R.write_date,'yyyy-mm-dd') as write_date,"
					   + " R.hit, R.favorite, R.fk_product_num, R.fk_order_num, R.fk_member_num, M.name"
					   + " from review_table R join member_table M on R.fk_member_num = M.member_num where R.fk_product_num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, product_num);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReviewVO review = new ReviewVO();
				review.setReview_num(rs.getInt(1));
				review.setSubject(rs.getString(2));
				review.setContent(rs.getString(3));
				review.setWrite_date(rs.getString(4));
				review.setHit(rs.getInt(5));
				review.setFavorite(rs.getInt(6));
				review.setFk_product_num(rs.getInt(7));
				review.setFk_order_num(rs.getInt(8));
				review.setFk_member_num(rs.getInt(9));
				review.setName(rs.getString(10));
				reviewList.add(review);
			}
			rs.close();
			
			if(reviewList.size()>0) {
				sql = " select image from review_image_table where fk_review_num = ? ";
				pstmt = conn.prepareStatement(sql);
				for(int i=0; i<reviewList.size(); i++) {
					pstmt.setInt(1, reviewList.get(i).getReview_num());
					rs = pstmt.executeQuery();
					List<String>imageList = new ArrayList<String>();
					while(rs.next()) {
						String image = rs.getString(1);
						imageList.add(image);
					}
					rs.close();
					reviewList.get(i).setImageList(imageList);
				}
			}
		}
		finally {
			close();
		}
		
		return reviewList;
	}

	
	// 특정 상품의 상품문의 조회 //
	@Override
	public List<ProductInquiryVO> productQCall(String product_num) throws SQLException {
		List<ProductInquiryVO> productQList = new ArrayList<ProductInquiryVO>();
		try {
			conn = ds.getConnection();
			String sql = " select PI.inquiry_num, PI.subject, PI.content, to_char(PI.write_date,'yyyy-mm-dd') as write_date,"
					   + " PI.answer, PI.emailFlag, PI.smsFlag, PI.secretFlag, PI.fk_member_num, M.name "
					   + " from product_inquiry_table PI join member_table M "
					   + " on PI.fk_member_num = M.member_num "
					   + " where PI.fk_product_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, product_num);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ProductInquiryVO productQ = new ProductInquiryVO();
				productQ.setInquiry_num(rs.getInt(1));
				productQ.setSubject(rs.getString(2));
				productQ.setContent(rs.getString(3));
				productQ.setWrite_date(rs.getString(4));
				productQ.setAnswer(rs.getString(5));
				productQ.setEmailFlag(rs.getInt(6));
				productQ.setSmsFlag(rs.getInt(7));
				productQ.setSecretFlag(rs.getInt(8));
				productQ.setFk_member_num(rs.getInt(9));
				productQ.setName(rs.getString(10));
				productQList.add(productQ);
			}
			rs.close();
			
			if(productQList.size()>0) {
				sql = " select image from product_inquiry_image_table where fk_inquiry_num = ? ";
				pstmt = conn.prepareStatement(sql);
				for(int i=0; i<productQList.size(); i++) {
					pstmt.setInt(1, productQList.get(i).getInquiry_num());
					rs = pstmt.executeQuery();
					List<String>imageList = new ArrayList<String>();
					while(rs.next()) {
						String image = rs.getString(1);
						imageList.add(image);
					}
					rs.close();
					productQList.get(i).setImageList(imageList);
				}
			}
		}
		finally {
			close();
		}
		
		return productQList;
	}

	// 카테고리 정보 조회
	@Override
	public List<Map<String, String>> categoryCall() throws SQLException {
		List<Map<String, String>> categoryList = new ArrayList<Map<String,String>>();
		try {
			conn = ds.getConnection();
			String sql = " select * from product_category_table union select * from product_subcategory_table ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Map<String, String> category = new HashMap<String, String>();
				category.put("num", rs.getString(1));
				category.put("content", rs.getString(2));
				categoryList.add(category);
			}
		}
		finally {
			close();
		}
		return categoryList;
	}
	
}
