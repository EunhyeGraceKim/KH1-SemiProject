package main.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import common.controller.AbstractController;
import main.model.IndexDAO;
import main.model.InterIndexDAO;
import main.model.ReviewVO;

public class ReviewCallAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String product_num = request.getParameter("product_num");
		System.out.println("확인용=>product_num : "+product_num);
		
		int currentPage = Integer.parseInt(request.getParameter("currentPage"));
		int totalPage = Integer.parseInt(request.getParameter("totalPage"));
		int pagePerNum = Integer.parseInt(request.getParameter("pagePerNum"));
		
		Map<String, Integer> paraMap = new HashMap<String, Integer>();
		paraMap.put("product_num", Integer.parseInt(product_num));
		paraMap.put("currentPage", currentPage);
		paraMap.put("totalPage",totalPage);
		paraMap.put("pagePerNum",pagePerNum);
		
		int start = currentPage*pagePerNum - (pagePerNum-1);
		int end = start + (pagePerNum-1);
		
		paraMap.put("start", start);
		paraMap.put("end", end);
		
		InterIndexDAO dao = new IndexDAO();
		List<ReviewVO> reviewList = dao.reviewCall(paraMap);
		
		totalPage = dao.getReviewtotalPage(paraMap);
		System.out.println("전체 페이지:"+totalPage);
		
	 int pageNo = 1; // 페이지바의 첫 번째	    	 
   	 
   	 int loop = 1; // 페이지 순서 증가 1 2 3 ...
   	 
   	 int blockSize = 10; // 페이지바 크기
   	 
   	 
   	 // pageNo 구하기
   	 System.out.println("currentPage:"+currentPage);
   	 pageNo = ((currentPage-1)/blockSize)*blockSize+1;
   	 System.out.println("pageNo:"+pageNo);
   	 String pageBar="";	    	 
   	 
   	 // [이전]
   	 if(pageNo!=1) {
   		 pageBar += " &nbsp; <span style='cursor:pointer;' onclick='func_reviewCall('"+(pageNo-pagePerNum)+"')'>이전</span> &nbsp ; ";
   	 }
   	 
   	 // 페이지바
   	 while(!(loop > blockSize || pageNo > totalPage)) {
  		  
	   		  if(pageNo == currentPage) {
	   			  pageBar += " &nbsp; <span style='color: red; padding: 2px 4px;'>" + pageNo + "</span> &nbsp; ";			  
	   		  } else {	
	   			  pageBar += " &nbsp; <span style='cursor:pointer;' onclick='func_reviewCall("+pageNo+")'>"+pageNo+"</span> &nbsp; ";
	   		  }

	   		  pageNo++;	// 1 2 3 4 5... (pageNo이 1이라면).... 40 41 42
	   		  loop++;	// 1 2 3 4 5 6 7 8 9 10
  		  
  	     }
   	 
   	 // [다음]
   	 if(!(pageNo > totalPage)) {
	    	
	    	 pageBar += " &nbsp; <span style='cursor:pointer;' onclick='func_reviewCall('"+(pageNo+pagePerNum)+"')'>다음</span> &nbsp; ";
   	 }
		
		
		JSONObject jobj = new JSONObject();
		jobj.put("reviewList", reviewList);
		jobj.put("pageBar", pageBar);
		
		String json = jobj.toString();
		
		
		request.setAttribute("json", json);
		super.setViewPage("/WEB-INF/jsonview.jsp");
	}

}
