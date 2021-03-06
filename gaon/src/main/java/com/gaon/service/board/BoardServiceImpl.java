package com.gaon.service.board;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gaon.domain.board.BoardDTO;
import com.gaon.persistence.board.BoardDAO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BoardServiceImpl implements BoardService {
	
	@Inject
	private BoardDAO bDao;
	
	@Transactional
	@Override
	public void createPlay(BoardDTO bDto) {
		// 게시글 등록
		bDao.createPlay(bDto);
		// attach 테이블에 첨부파일 이름 추가
		String[] files = bDto.getFiles();
		if (files == null) return; // 첨부파일 없으면 skip
		for (String name : files) {
			bDao.addAttach(name); // attach 테이블에 insert
		}
	}
	
	@Override
	public void update(BoardDTO bDto) {
		bDao.update(bDto);
	}

	@Override
	public void delete(int bno) {
		bDao.delete(bno);
	}

	@Override
	public BoardDTO read(int bno) {
		return bDao.read(bno);
	}

	@Override
	public List<BoardDTO> listAll(String sort_option, String search_option, String keyword, int start, int end) {
		return bDao.listAll(sort_option, search_option, keyword, start, end);
	}

	@Override
	public void increaseViewCnt(int bno, HttpSession session) {
		long update_time=0;
		if(session.getAttribute("update_time_"+bno)!=null) {
			update_time = (long)session.getAttribute("update_time_"+bno);
		}
		long current_time = System.currentTimeMillis();
		//일정시간이 경과한 후 조회수 증가 처리
		if(current_time - update_time > 24*60*60*1000) {
			//조회수 증가처리
			bDao.increaseViewCnt(bno, session);
			
			//조회수를 올린 시간 저장
			session.setAttribute("update_time_"+bno, current_time);
		}
	}

	@Override
	public int countArticle(String search_option, String keyword) {
		return bDao.countArticle(search_option, keyword);
	}

	@Override
	public int goodCheck(int bno, String id) {
		return bDao.goodCheck(bno, id);
	}
	
	@Transactional
	@Override
	public void goodCnt(int bno, String id) {
		int goodCheck = bDao.goodCheck(bno, id);
		if (goodCheck > 0) {
			// -1 해줌(좋아요를 이미 눌렀기 때문)
			log.info(">>>>> goodcnt - 1");
			bDao.goodDelete(bno, id);
		} else {
			// +1 해줌
			log.info(">>>>> goodcnt + 1");
			bDao.goodAdd(bno, id);
		}
	}

	@Override
	public int goodTotal(int bno, String id) {
		int goodTotal = bDao.goodTotal(bno, id);
		bDao.goodUpdate(bno, goodTotal);
		log.info(">>>>> goodTotal : " + goodTotal);
		return goodTotal;
	}
	
	@Override
	public void answer(BoardDTO bDto) {
		// 비즈니스 로직: 답글 등록
		// 1. 답글을 달려고 하는 게시글의 re_step보다 큰 답글들을 re_step + 1
		bDao.updateStep(bDto.getRef(), bDto.getRe_step());
		// 2. 답글 테이블에 등록하기
		bDto.setRe_step(bDto.getRe_step() + 1);
		bDto.setRe_level(bDto.getRe_level() + 1);
		bDao.answer(bDto);
	}
	
}
