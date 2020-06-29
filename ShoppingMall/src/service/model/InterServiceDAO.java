package service.model;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.model.FAQtableVO;
import main.model.NoticeVO;
import main.model.OneInquiryVO;

public interface InterServiceDAO {

	// 공지사항 테이블 조회
	List<NoticeVO> selectPagingMember(HashMap<String, String> paraMap) throws SQLException;

	// 공지사항 총 게시글 수 조회
	int getTotalPage(HashMap<String, String> paraMap) throws SQLException;


	// 자주하는 질문 테이블 조회
	List<FAQtableVO> selectFAQ(HashMap<String, String> paraMap) throws SQLException;

	// 자주하는 질문 총 게시글 수 조회
	int getTotalPageFAQ(HashMap<String, String> paraMap) throws SQLException;

	// 자주하는 질문 카테고리 조회
	List<Map<String, String>> getFAQcategory() throws SQLException;

	// 1:1문의 테이블 조회
	List<OneInquiryVO> selectOneInquiry(Map<String, Integer> paraMap) throws SQLException;

}
