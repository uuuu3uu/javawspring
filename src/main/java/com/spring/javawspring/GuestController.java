package com.spring.javawspring;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.GuestService;
import com.spring.javawspring.vo.GuestVO;

@Controller
@RequestMapping("/guest")
public class GuestController {
	
	@Autowired
	GuestService guestService;
	
	@Autowired
	PageProcess pageProcess;
	
	/*
	@RequestMapping(value = "/guestList", method=RequestMethod.GET)
	public String guestListGet(Model model,
		@RequestParam(name="pag", defaultValue = "1", required = false) int pag) {
		// 1. 페이지(pag)를 결정한다.
		
		// 2. 한 페이지의 분량을 결정한다.
		int pageSize = 3;
		
		// 3. 총 레코드 건수를 구한다
		int totRecCnt = guestService.totRecCnt();
		
		// 4. 총 페이지 건수를 구한다.
		int totPage = (totRecCnt % pageSize)==0 ? totRecCnt / pageSize : (totRecCnt / pageSize) + 1;
		System.out.println("totRecCnt" + totRecCnt);
		// 5. 현재 페이지의 시작 인덱스 번호를 구한다.
		int startIndexNo = (pag - 1) * pageSize;
		
		// 6. 현재 화면에 보여주는 시작 번호를 구한다.
		int curScrStrtNo = totRecCnt - startIndexNo;
		
		// 블록페이징 (3단계) -> 블록의 시작번호를 0번부터 처리했다
		// 1. 블록의 크기를 결정한다(여기선 3으로 지정)
		int blockSize = 3;
		
		// 2. 현재페이지가 위치하고 있는 블록 번호를 구한다.(예: 1페이지는 0블록, 3페이지는 0블록, 5페이지는 1블록)
		int curBlock = (pag - 1) / blockSize;
		
		// 3. 마지막블록을 구한다.
		int lastBlock = (totPage - 1) / blockSize;
		
		
//	ArrayList<GuestVO> vos = dao.getGuestList();
		ArrayList<GuestVO> vos = guestService.getGuestList(startIndexNo, pageSize); // 실제 인덱스번호와 페이지 사이즈를 넘겨준다
		
		model.addAttribute("vos", vos); // DAO에서 5건씩 담아지게 된걸 가져옴
		model.addAttribute("pag", pag);
		model.addAttribute("totPage", totPage);
		model.addAttribute("curScrStartNo", curScrStrtNo);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("curBlock", curBlock);
		model.addAttribute("lastBlock", lastBlock);
		
		System.out.println("curScrStrtNo" + curScrStrtNo);
		return "guest/guestList";
	}
	*/
	
	// Pagination을 이용한 리스트 처리
	@RequestMapping(value = "/guestList", method=RequestMethod.GET)
	public String guestListGet(Model model,
		@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
		@RequestParam(name="pageSize", defaultValue = "3", required = false) int pageSize) {
		
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "guest", "", "");
		
//	ArrayList<GuestVO> vos = dao.getGuestList();
		ArrayList<GuestVO> vos = guestService.getGuestList(pageVo.getStartIndexNo(), pageSize); // 실제 인덱스번호와 페이지 사이즈를 넘겨준다
		
		model.addAttribute("vos", vos); // DAO에서 5건씩 담아지게 된걸 가져옴
		model.addAttribute("pageVo", pageVo);
	
		return "guest/guestList";
	}
	
	
	
	@RequestMapping(value = "/guestInput", method=RequestMethod.GET)
	public String guestInputGet(Model model) {
		return "guest/guestInput";
	}
	
	@RequestMapping(value = "/guestInput", method=RequestMethod.POST)
	public String guestInputPost(GuestVO vo) {
		guestService.setGuestInput(vo);
		
		return "redirect:/msg/guestInputOk";
	}
	
	@RequestMapping(value = "/guestDelete", method=RequestMethod.GET)
	public String guestDeleteGet(int idx) {
		guestService.setguestDelete(idx);
		
		return "redirect:/msg/guestDeleteOk";
	}
}
