package com.gaon.service.board;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;

import com.gaon.domain.board.BoardDTO;
import com.gaon.persistence.board.BoardDAO;

@Repository
public class BoardServiceImpl implements BoardService {
	
	@Inject
	private BoardDAO bDao;

	@Override
	public void create(BoardDTO bDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void upadate(BoardDTO bDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int bno) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BoardDTO read(int bno) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BoardDTO> listAll(String sort_option, String search_option, String keyword, int start, int end) {
		return bDao.listAll(sort_option, search_option, keyword, start, end);
	}

	@Override
	public void increaseViewCnt(int bno, HttpSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int countArticle(String search_option, String keyword) {
		return bDao.countArticle(search_option, keyword);
	}
	
}
