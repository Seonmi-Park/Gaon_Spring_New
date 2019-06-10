package com.gaon.controller.board;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gaon.domain.board.BoardDTO;
import com.gaon.service.board.BoardService;
import com.gaon.service.board.Pager;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("board/*")
public class BoardController {
	
	@Inject
	private BoardService service;
	
	// 게시글 전체리스트 출력
	@RequestMapping(value="list", method=RequestMethod.GET)
	public ModelAndView list( // 외부에서 출력한 데이터들(form, 쿼리 등) 가져옴
			// defaultValue: null값일 때 대신 들어가는 데이터
			@RequestParam(defaultValue="new") String sort_option,
			@RequestParam(defaultValue="all") String search_option,
			@RequestParam(defaultValue="") String keyword,
			@RequestParam(defaultValue="1") int curPage ) {
		log.info(">>>>> 게시글목록 출력");
		
		// 레코드 갯수 계산
		int count = service.countArticle(search_option,keyword);
		
		//페이지 관련 설정
		Pager pager = new Pager(count, curPage);
		int start = pager.getPageBegin();
		int end = pager.getPageEnd();
		
		// 페이지에 출력할 게시글 목록
		List<BoardDTO> list = service.listAll(sort_option, search_option, keyword, start, end);
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("count", count);
		map.put("pager", pager);
		map.put("sort_option", sort_option);
		map.put("search_option", search_option);
		map.put("keyword", keyword);
		
		mav.setViewName("board/list");
		mav.addObject("map", map);
		
		// model.addAttribute("map", map);
		// return "board/list";
		
		return mav;
	}
}
