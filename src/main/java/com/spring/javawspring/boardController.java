package com.spring.javawspring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.BoardService;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.BoardReplyVO;
import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.GoodVO;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/board")
public class boardController {
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	PageProcess pageProcess;
	
	@Autowired
	MemberService memberService;
	


	@RequestMapping(value = "/boardList", method=RequestMethod.GET)
	public String boardListGet(Model model,
		@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
		@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize,
		@RequestParam(name="searchString", defaultValue = "", required = false) String searchString,
		@RequestParam(name="part", defaultValue = "title", required = false) String part
		) {
		
		PageVO pageVO = pageProcess.totRecCnt(pag, pageSize, "board", part, searchString);
		
		// 전체리스트를 불러오는 메소드
		List<BoardVO> vos = boardService.getBoardList(pageVO.getStartIndexNo(),pageSize, searchString, part);
		
		model.addAttribute("vos" , vos);
		model.addAttribute("pageVO" , pageVO);
		
		return "board/boardList";
	}
	
	@RequestMapping(value = "/boardInput", method = RequestMethod.GET)
	public String boardInputGet(Model model, HttpSession session, int pag, int pageSize) {	
		String mid = (String) session.getAttribute("sMid");
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		model.addAttribute("pag" , pag);
		model.addAttribute("pageSize" , pageSize);
		model.addAttribute("email" , vo.getEmail());
		model.addAttribute("homePage" , vo.getHomePage());
		
		return "board/boardInput";
	}
	
	@RequestMapping(value = "/boardInput", method = RequestMethod.POST)
	public String boardInputPost(BoardVO vo) {
		// content에 이미지가 저장되어 있다면, 저장된 이미지만 골라서 resources/data/board폴더에 저장시켜준다
		boardService.imgCheck(vo.getContent());
		
		// 이미지 복사작업이 끝나면, board폴더에 실제로 저장된 파일명을 DB에 저장시켜준다(/resources/data/ckeditor)
		vo.setContent(vo.getContent().replace("/data/ckeditor/", "/data/ckeditor/board/"));
		int res = boardService.setBoardInput(vo);

		if(res == 1) return "redirect:/msg/boardInputOk";
		else return "redirect:/msg/boardInputNo";
	}
	
	@RequestMapping(value = "/boardContent", method = RequestMethod.GET)
	public String boardContentGet(HttpSession session , Model model, int idx, int pag, int pageSize) {
		
		//조회수 증가처리
		boardService.setBoardReadNum(idx);
		
		BoardVO vo = boardService.getBoardContent(idx);
		
		model.addAttribute("vo",vo);
		model.addAttribute("pag",pag);
		model.addAttribute("pageSize",pageSize);
		
		
		// DB에서 현재 게시글에 '좋아요'가 체크되어 있는지를 알아오자
		String mid = (String) session.getAttribute("sMid");
		GoodVO goodVo = boardService.getBoardGoodCheck(idx, "board", mid);
		model.addAttribute("goodVo", goodVo);
		
		// 이전글 / 다음글 가져오기
		ArrayList<BoardVO> pnVos = boardService.getPrevNext(idx); // 값이 두개가 넘어오도록 처리하겠다
		//System.out.println("pnVos : " + pnVos);
		model.addAttribute("pnVos",pnVos);
	
		// 댓글 가져오기(replyVOs)
		List<BoardReplyVO> replyVos = boardService.getBoardReply(idx);
		model.addAttribute("replyVos", replyVos);
		
		return "board/boardContent";
	}
	
	
	// 좋아요
	@ResponseBody
	@RequestMapping(value = "/boardGood", method = RequestMethod.POST)
	public String boardGood(int idx, int gFlag, HttpSession session) {
		gFlag = gFlag * (-1);
		boardService.getBoardGoodUpdate(idx, gFlag);
		session.setAttribute("sGFlag", gFlag);
		
		return "board/boardContent";
	}
	
	// 게시글 삭제하기
	@RequestMapping(value = "/boardDeleteOk", method = RequestMethod.GET)
	public String boardDelCheckOkGet(int idx, int pag, int pageSize, Model model) {
		// 게시글에 사진이 존재한다면 서버에 있는 사진파일을 먼저 삭제한다.
		BoardVO vo = boardService.getBoardContent(idx);
		if(vo.getContent().indexOf("src=\"/") != -1) boardService.imgDelete(vo.getContent());
		
		// DB에서 실제로 존재하는 게시글을 삭제처리한다.
		boardService.setBoardDeleteOk(idx);
		
		model.addAttribute("flag","?pag="+pag+"&pageSize="+pageSize);
		
		return "redirect:/msg/boardDeleteOk";
	}
	
	// 게시글 수정 폼 호출
	@RequestMapping(value = "/boardUpdate", method = RequestMethod.GET)
	public String boardUpdate(Model model, int idx, int pag, int pageSize) {
		// 수정창으로 이동 시에는 먼저 원본파일에 그림파일이 있다면, 현재폴더(board)의 그림파일을 ckeditor 폴더로 복사시켜준다.
		BoardVO vo = boardService.getBoardContent(idx);
		if(vo.getContent().indexOf("src=\"/") != -1) boardService.imgCheckUpdate(vo.getContent()); // 컨텐트안에 그림파일이 존재 한다면 이미지를 체크해서 보낸다
		
		model.addAttribute("vo", vo);
		model.addAttribute("pag", pag);
		model.addAttribute("pageSize", pageSize);
		
		return "board/boardUpdate";
	}
	
	//변경된 게시글 수정 처리(그림포함)
	@RequestMapping(value = "/boardUpdate", method = RequestMethod.POST)
	public String boardUpdate(Model model, BoardVO vo, int pag, int pageSize) {
		// 수정된 자료가 원본 자료와 완전히 동일하다면 수정할 필요가 없기에, DB에 저장된 원본 자료를 불러와서 비교해준다.
		BoardVO origVo = boardService.getBoardContent(vo.getIdx()); // db에서 넘어온 idx
		
		// content의 내용이 조금이라도 변경된 것이 있다면 아래 내용을 수행처리시킨다
		if(!origVo.getContent().equals(vo.getContent())) {	//오리지널에 있는 콘텐츠와 vo.콘텐츠가 같지않다면
			// 실제로 수정하기 버튼을 클릭하게 되면 기존의 board폴더에 저장된 현재 content의 그림 파일 모두를 삭제시킨다
			if(origVo.getContent().indexOf("src=\"/") != -1) boardService.imgDelete(origVo.getContent()); // 사진이 있다면 지우겠따
			
			// vo.GetContent()에 들어있는 파일의 경로는 'ckeditor/board' 경로를 'ckeditor' 변경시켜줘야한다.
			vo.setContent(vo.getContent().replace("/data/ckeditor/board/", "/data/ckeditor/"));
			
			// 앞의 모든 준비가 끝나면, 파일을 처음 업로드한것과 같이 같은 작업을 처리한다.
			// 이 작업은 처음 게시글을 오릴 때의 파일 복사 작업과 동일한 작업이다
			boardService.imgCheck(vo.getContent());
			
			// 파일 업로드가 끝나면 다시 경로를 수정한다, 'ckeditor' 경로를 'ckeditor/board'로 변경시켜줘야한다(즉, 변경된 vo.setCotnent() 처리한다.)
			vo.setContent(vo.getContent().replace("/data/ckeditor/", "/data/ckeditor/board/"));
			
		}
		
		// 잘 정비된 vo를 DB에 Update시켜준다
		boardService.setBoardUpdateOk(vo);
		
		model.addAttribute("flag","?pag="+pag+"&pageSize="+pageSize);
		
		return "redirect:/msg/boardUpdateOk";
	}
	
	// 댓글달기
	@ResponseBody // ajax처리했으니까
	@RequestMapping(value = "/boardReplyInput", method=RequestMethod.POST)
	public String boardReplyInputPost(BoardReplyVO replyVo) {
		int levelOrder = 0;
		String strLevelOrder = boardService.getMaxLevelOrder(replyVo.getBoardIdx());
		if(strLevelOrder != null) levelOrder = Integer.parseInt(strLevelOrder) + 1;
		replyVo.setLevelOrder(levelOrder + 1);
		
		boardService.setBoardReplyInput(replyVo);
		return "1";
	}
	
	//대댓글(답변글)달기
	@ResponseBody
	@RequestMapping(value = "/boardReplyInput2", method=RequestMethod.POST)
	public String boardReplyInput2Post(BoardReplyVO replyVo) {
		//System.out.println("replyVo : " + replyVo);
		boardService.setLevelOrderPlusUpdate(replyVo);
		replyVo.setLevel(replyVo.getLevel()+1);
		replyVo.setLevelOrder(replyVo.getLevelOrder()+1);
		boardService.setBoardReplyInput2(replyVo);
		return "";
	}
	

	// 댓글 삭제하기
	@ResponseBody
	@RequestMapping(value = "/boardReplyDeleteOk", method = RequestMethod.POST)
	public String boardReplyDeleteOk(int idx) {
		
		boardService.setBoardReplyDeleteOk(idx);
		
		return "";
	}
	
}
