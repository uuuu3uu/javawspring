package com.spring.javawspring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	PageProcess pageProcess;

	@RequestMapping(value = "/memberLogin", method=RequestMethod.GET)
	public String memberLoginGet(HttpServletRequest request) {
		// 로그인폼 호출 시에 기존에 저장된 쿠키가 있다면 불러와서 mid에 담아서 넘겨준다
		Cookie[] cookies = request.getCookies();
		for(int i=0; i<cookies.length; i++) {
			if(cookies[i].getName().equals("cMid")) {
				request.setAttribute("mid", cookies[i].getValue());
				break;
			}
		}
	return "member/memberLogin";
	}
	@RequestMapping(value = "/memberLogin", method=RequestMethod.POST)
	public String memberLoginPost(HttpServletRequest request, HttpServletResponse response, HttpSession session,
	@RequestParam(name="mid", defaultValue = "", required = false) String mid, // 널값 처리
	@RequestParam(name="pwd", defaultValue = "", required = false) String pwd, 
	@RequestParam(name="idCheck", defaultValue = "", required = false) String idCheck) {
		
	MemberVO vo = memberService.getMemberIdCheck(mid);
	
	if(vo != null && passwordEncoder.matches(pwd, vo.getPwd()) && vo.getUserDel().equals("NO")) {
		// 회원 인증 처리된 경우 수행할 내용 ? strLevel처리, session에 필요한 자료를 저장, 쿠키 값 처리, 그날 방문자수 1증가(방문포인트도)
		String strLevel = "";
		if(vo.getLevel() == 0) strLevel = "관리자";
		else if(vo.getLevel() == 1) strLevel = "운영자";
		else if(vo.getLevel() == 2) strLevel = "우수회원";
		else if(vo.getLevel() == 3) strLevel = "정회원";
		else if(vo.getLevel() == 4) strLevel = "준회원";
		
		session.setAttribute("sLevel", vo.getLevel());
		session.setAttribute("sStrLevel", strLevel);
		session.setAttribute("sMid", mid);
		session.setAttribute("sNickName", vo.getNickName());
		
		if(idCheck.equals("on")) {	// 아이디 기억하겠다에 체크
			Cookie cookie = new Cookie("cMid", mid);
			cookie.setMaxAge(60*60*24*7);
			response.addCookie(cookie);
		}
		else {
			Cookie[] cookies = request.getCookies();
			for(int i=0; i<cookies.length; i++) {
				if (cookies[i].getName().equals("cMid")) {
					cookies[i].setMaxAge(0);
					response.addCookie(cookies[i]);
					break;
				}
			}
		}
		
		// 로그인한 사용자의 방문횟수 (포인트)누적
		memberService.setMemberVisitProcess(vo);
		
		return "redirect:/msg/memberLoginOk?mid="+mid;
		}
		else {
			return "redirect:/msg/memberLoginNo";
		}	
	}
	
	@RequestMapping(value="/memberLogout", method = RequestMethod.GET)
	public String memberLogoutGet(HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		
		session.invalidate(); // session끊기
		
		return "redirect:/msg/memberLogout?mid="+mid;
	}
	
	
	@RequestMapping(value = "/memberMain", method=RequestMethod.GET)
	public String memberMainGet(HttpSession session, Model model) {
		String mid = (String) session.getAttribute("sMid");
		
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		model.addAttribute("vo" , vo);
		
		return "member/memberMain";
	}
	
	@RequestMapping(value = "/adminLogout", method=RequestMethod.GET)
	public String adminLogoutGet(HttpSession session) {	
		String mid = (String)session.getAttribute("sAMid");
		
		session.invalidate();
		
		return "redirect:/msg/memberLogout?mid="+mid;
	}
	
	//회원가입폼
	@RequestMapping(value = "/memberJoin", method=RequestMethod.GET)
	public String memberJoinGet() {
		return "member/memberJoin";
	}
	
	// 회원가입처리
	@RequestMapping(value = "/memberJoin", method=RequestMethod.POST)
	public String memberJoinPost(MemberVO vo) {
		//System.out.println("memberVo : " + vo);
		// 아이디 중복 체크
		if(memberService.getMemberIdCheck(vo.getMid()) != null) {
			return "redirect:/msg/memberIdCheckNo";
		}
		// 닉네임 중복 체크
		if(memberService.getMemberNickNameCheck(vo.getMid()) != null) {
			return "redirect:/msg/memberNickNameCheckNo";
		}
		
		// 비밀번호 암호화(BCryptPasswordEncoder)
		vo.setPwd(passwordEncoder.encode(vo.getPwd()));
		
		// 체크가 완료되면 vo에 담긴 자료를 DB에 저장시켜준다 (회원가입)
		int res = memberService.setMemberJoinOk(vo);
		
		if(res ==1) return "redirect:/msg/memberJoinOk";
		else return "redirect:/msg/memberJoinNo";
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/memberIdCheck", method=RequestMethod.POST)
	public String memberIdCheckGet(String mid) {
		String res = "0";
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		if(vo != null) res = "1";
		
		return res;
	}
	
	@ResponseBody
	@RequestMapping(value = "/memberNickNameCheck", method=RequestMethod.POST)
	public String memberNickNameCheckGet(String nickName) {
		String res = "0";
		MemberVO vo = memberService.getMemberNickNameCheck(nickName);
		
		if(vo != null) res = "1";
		
		return res;
	}
	
	// 전체리스트와 검색리스트를 하나의 메소드로 처리
	/*
	@RequestMapping(value = "/memberList", method = RequestMethod.GET)
	public String memberLsitGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false) int pageSize) {

		int totRecCnt = memberService.totRecCnt();
		int totPage = (totRecCnt % pageSize)==0 ? totRecCnt / pageSize : (totRecCnt / pageSize) + 1;
		int startIndexNo = (pag - 1) * pageSize;
		int curScrStartNo = totRecCnt - startIndexNo;
		
		int blockSize = 3;
		int curBlock = (pag - 1) / blockSize;
		int lastBlock = (totPage - 1) / blockSize;
		
		ArrayList<MemberVO> vos = memberService.getMemberList(startIndexNo, pageSize);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pag", pag);
		model.addAttribute("totPage", totPage);
		model.addAttribute("curScrStartNo", curScrStartNo);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("curBlock", curBlock);
		model.addAttribute("lastBlock", lastBlock);
		
		return "member/memberList";
	}


	
	//전체리스트와 검색리스트를 하나의 메소드로 처리 -> 동적쿼리
	@RequestMapping(value = "/memberList", method = RequestMethod.GET)
	public String memberLsitGet(Model model,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false) int pageSize) {

		int totRecCnt = memberService.totTermRecCnt(mid);
		int totPage = (totRecCnt % pageSize)==0 ? totRecCnt / pageSize : (totRecCnt / pageSize) + 1;
		int startIndexNo = (pag - 1) * pageSize;
		int curScrStartNo = totRecCnt - startIndexNo;
		
		int blockSize = 3;
		int curBlock = (pag - 1) / blockSize;
		int lastBlock = (totPage - 1) / blockSize;
		
		ArrayList<MemberVO> vos = memberService.getTermMemberList(startIndexNo, pageSize, mid);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pag", pag);
		model.addAttribute("totPage", totPage);
		model.addAttribute("totRecCnt", totRecCnt);
		model.addAttribute("curScrStartNo", curScrStartNo);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("curBlock", curBlock);
		model.addAttribute("lastBlock", lastBlock);
		
		model.addAttribute("mid", mid);
		
		return "member/memberList";
	}
		*/
	
	// pagination 이용하기
	@RequestMapping(value = "/memberList", method = RequestMethod.GET)
	public String memberLsitGet(Model model,
		@RequestParam(name="mid", defaultValue = "", required = false) String mid,
		@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
		@RequestParam(name="pageSize", defaultValue = "3", required = false) int pageSize) {

		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "member", "", "");
		
		List<MemberVO> vos = memberService.getMemberList(pageVo.getStartIndexNo(), pageSize);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		
		return "member/memberList";
	}
	
	// 회원 검색
	@RequestMapping(value = "/memberSearch", method = RequestMethod.POST)
	public String memberSearchPost(String mSearch, String memberSearchC, Model model) {
		ArrayList<MemberVO> vos = memberService.memberSearchPost(mSearch, memberSearchC);
		model.addAttribute("vos", vos);
		return "member/memberSearch";
	}
	
	// 회원 개별검색
	@RequestMapping(value = "/memberInfor", method = RequestMethod.GET)
	public String memberInforGet(Model model, String mid) {
		
		MemberVO vo = memberService.memberInforGet(mid);
		model.addAttribute("vo", vo);
		return "member/memberInfor";
	}
	
	// 회원삭제
	@RequestMapping(value = "/memberDelete" , method = RequestMethod.GET)
	public String memberDeleteGet() {
		
		return "redirect:/msg/memberDelete";
	}
	
	// 회원삭제 처리
	@RequestMapping(value = "/memberDeleteOk" , method = RequestMethod.GET)
	public String memberDeleteOkGet(String mid) {
		memberService.memberDeleteOkGet(mid);
		return "redirect:/msg/memberDeleteOk";
	}
	
	
	
}
