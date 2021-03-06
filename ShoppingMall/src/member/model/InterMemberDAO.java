package member.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import main.model.OneInquiryVO;
import main.model.OrderHistoryVO;
import member.model.MemberVO;

public interface InterMemberDAO {

	// ID중복 검사 (userid가 중복없어 사용가능true, 사용불가능 false 리턴)
	boolean idDuplicateCheck(String userid) throws SQLException;

	// 이메일 중복 검사
	boolean emailDuplicateCheck(String email) throws SQLException;

	// 회원가입
	int registerMember(MemberVO membervo) throws SQLException;
	
	// 로그인(아이디와 암호를 입력받아서 그 회원에 대한 정보를 리턴)
	MemberVO selectOneMember(HashMap<String, String> paraMap) throws SQLException;

	// 휴면상태 사용자계정 로그인 시 휴면해제(lastLoginDate 컬럼 값 sysdate로 update)
	void expireIdle(int member_num) throws SQLException;

	//아이디찾기(이름, 핸드폰번호) 
	String findUserid(HashMap<String, String> paraMap) throws SQLException;
	
	//비밀번호 찾기(이름, 아이디, 이메일 번호를 입력받아 해당 사용자가 존재하는지 유무를 알려준다.)
	boolean isUserExist(HashMap<String, String> paraMap) throws SQLException;
	
	//패스워드 업데이트 
	int pwdUpdate(String pwd, String userid) throws SQLException;
	
	//탈퇴하기 패스워드 중복검사
	boolean dropoutPwdDuplicateCheck(String pwd) throws SQLException ;

	//회원탈퇴하기 
	int dropoutMember(String userid) throws SQLException ;
	
	//1:1문의 게시판(고객센터)
	int serviceCenterMyQboardWrite(OneInquiryVO oneInQueryVO ) throws SQLException;

	//1:1문의 게시판(주문조회)
	List<OrderHistoryVO> selectOneMemberOrderList(HashMap<String, String> paraMap) throws SQLException;

	//1:1문의 게시판(주문주회) 페이징처리 
	int getTotalpage(HashMap<String, String> paraMap) throws SQLException;



}
