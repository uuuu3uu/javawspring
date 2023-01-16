package com.spring.javawspring;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
	
	@Autowired
	JavaMailSender mailSender;

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
	
//카카오 로그인 완료후 수행할 내용들을 기술한다.
	@RequestMapping(value="/memberKakaoLogin", method=RequestMethod.GET)
	public String memberKakaoLoginGet(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			String nickName,
			String email) {
		
		// 카카오로그인한 회원이 현재 우리 회원인지를 조회한다.
		// 이미 가입된 회원이라면 서비스를 사용하게 하고, 그렇지 않으면 강제로 회원 가입시킨다.
		MemberVO vo = memberService.getMemberNickNameEmailCheck(nickName, email);
		
		// 현재 우리회원이 아니면 자동회원가입처리..(가입필수사항: 아이디,닉네임,이메일) - 아이디는 이메일주소의 '@'앞쪽 이름을 사용하기로 한다.
		if(vo == null) {
			// 아이디 결정하기
			String mid = email.substring(0, email.indexOf("@"));
			
			// 임시 비밀번호 발급하기(여기선 '0000'으로 발급하기로 한다.)
			String pwd = passwordEncoder.encode("0000");
			
			// 자동 회원 가입처리한다.
			memberService.setKakaoMemberInputOk(mid, pwd, nickName, email);
			
			// 가입 처리된 회원의 정보를 다시 읽어와서 vo에 담아준다.
			vo = memberService.getMemberIdCheck(mid);
		}
		// 만약에 탈퇴신청한 회원이 카카오로그인처리하였다라면 'userDel'필드를 'NO'로 업데이트한다.
		if(!vo.getUserDel().equals("NO")) {
			memberService.setMemberUserDelCheck(vo.getMid());
		}
		
		// 회원 인증처리된 경우 수행할 내용? strLevel처리, session에 필요한 자료를 저장, 쿠키값처리, 그날 방문자수 1 증가(방문포인트도 증가), ..
		String strLevel = "";
		if(vo.getLevel() == 0) strLevel = "관리자";
		else if(vo.getLevel() == 1) strLevel = "운영자";
		else if(vo.getLevel() == 2) strLevel = "우수회원";
		else if(vo.getLevel() == 3) strLevel = "정회원";
		else if(vo.getLevel() == 4) strLevel = "준회원";
		
		session.setAttribute("sLevel", vo.getLevel());
		session.setAttribute("sStrLevel", strLevel);
		session.setAttribute("sMid", vo.getMid());
		session.setAttribute("sNickName", vo.getNickName());
		
		// 로그인한 사용자의 오늘 방문횟수(포인트) 누적...
		memberService.setMemberVisitProcess(vo);
		
		return "redirect:/msg/memberLoginOk?mid="+vo.getMid();
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
	public String memberJoinPost(MultipartFile fName, MemberVO vo) {
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
		
		// 체크가 완료되면 사진파일 업로드 후, vo에 담긴 자료를 DB에 저장시켜준다 (회원가입) - 서비스 객체에서 수행 처리했다.
		int res = memberService.setMemberJoinOk(fName, vo);
		
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

		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "member", "", mid);
		
		// 회원 검색하기위한 과정
		List<MemberVO> vos = memberService.getMemberList(pageVo.getStartIndexNo(), pageSize, mid);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		
		return "member/memberList";
	}
	
	// 회원 검색(셀렉트박스 사용해서 처음에 내가 한거)
	@RequestMapping(value = "/memberSearch", method = RequestMethod.POST)
	public String memberSearchPost(String mSearch, String memberSearchC, Model model) {
		ArrayList<MemberVO> vos = memberService.memberSearchPost(mSearch, memberSearchC);
		model.addAttribute("vos", vos);
		return "member/memberSearch";
	}
	
	// 회원 상세페이지
	@RequestMapping(value = "/memberInfor", method = RequestMethod.GET)
	public String memberInforGet(Model model, String mid) {
		
		MemberVO vo = memberService.memberInforGet(mid);
		model.addAttribute("vo", vo);
		return "member/memberInfor";
	}
	
	
	// 비밀번호 확인
	@RequestMapping(value = "/memberPwdCheck", method = RequestMethod.GET)
	public String memberPwdCheckGet() {
		
		return "member/memberPwdCheck";
	}
	
	// 비밀번호 확인 처리
	@RequestMapping(value = "/memberPwdCheck", method = RequestMethod.POST)
	public String memberPwdCheckPost(Model model, HttpSession session, String oldPwd) {
		String mid = (String) session.getAttribute("sMid");
		
		MemberVO vo = memberService.getMemberIdCheck(mid);
		model.addAttribute("vo", vo);	
		
		if(passwordEncoder.matches(oldPwd, vo.getPwd())) {
			return "member/memberUpdate";
		}
		else {
			return "redirect:/msg/memberPwdCheckNo";
		}
		
	}
	
	/*
	// 회원 수정하기 
	@RequestMapping(value = "/memberUpdate", method = RequestMethod.GET)
	public String memberUpdateGet(Model model, HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		
		MemberVO vo = memberService.getMemberIdCheck(mid);
		model.addAttribute("vo", vo);	
		
		return "member/memberUpdate";
	}
	*/
	//회원 수정하기 처리
	@RequestMapping(value = "/memberUpdate", method = RequestMethod.POST)
	public String memberUpdatePost(Model model, HttpSession session, String sMid) {
		
		String mid = (String) session.getAttribute("sMid");
		MemberVO vo = memberService.getMemberIdCheck(mid);
		model.addAttribute("vo", vo);	
		
		return "redirect:/msg/memberUpdateOk";
	}
	
	// 회원삭제
	@RequestMapping(value = "/memberDelete" , method = RequestMethod.GET)
	public String memberDeleteGet() {
		
		return "redirect:/msg/memberDelete";
	}
	
	// 회원삭제 처리
	@RequestMapping(value = "/memberDeleteOk" , method = RequestMethod.GET)
	public String memberDeleteOkGet(HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		
		memberService.memberDeleteOk(mid);
		session.invalidate(); // session끊기
		
		return "redirect:/msg/memberDeleteOk";
	}
	
	
	
	// 비밀번호 찾기를 위한 임시비밀번호 발급 폼
	@RequestMapping(value= "/memberPwdSearch", method = RequestMethod.GET)
	public String memberPwdSearchGet() {
		
		return "member/memberPwdSearch";
	}
	
	//비밀번호 찾기를 위한 임시비밀번호처리(임시비밀번호를 생성시켜 메일로 보내기)
	@RequestMapping(value = "/memberPwdSearch", method = RequestMethod.POST)
	public String memberPwdSearchPost(String mid, String toMail) {
		MemberVO vo = memberService.getMemberIdCheck(mid);
		if(vo.getEmail().equals(toMail)) {
			// 회원정보가 맞다면 임시비밀번호를 받급받는다.
			UUID uid = UUID.randomUUID();
			String pwd = uid.toString().substring(0,8);
			
			// 발급받은 임시비밀번호를 암호화처리시켜서 DB에 저장한다.
			memberService.setMemberPwdUpdate(mid, passwordEncoder.encode(pwd));
			String content = pwd;
			
			// 임시비밀번호를 메일로 전송처리한다.
			String res = mailSend(toMail, content);
			System.out.println("1");
			if(res.equals("1")) return "redirect:/msg/memberImsiPwdOk";
			else return "redirect:/msg/memberImsiPwdNo";
		}
		else {
			return "redirect:/msg/memberImsiPwdNo";
		}
		
	}
	
	public String mailSend(String toMail, String content) {
		try {
			String title = "임시 비밀번호가 발급되었습니다.";
			System.out.println("2");
			// 메일을 전송하기위한 객체 : MimeMessage() , MimeMessageHelper()
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			
			// 메일보관함에 회원이 보내온 메세지들을 모두 저장시킨다.
			messageHelper.setTo(toMail);
			messageHelper.setSubject(title);
			messageHelper.setText(content);
			
			// 메세지 보관함의 내용(content)에 필요한 정보를 추가로 담아서 전송시킬수 있도록 한다.
			content = "<br><hr><h3>신규 비밀번호는 <font color='red'>"+content+"</font></h3><hr><br>";
			content += "<br><hr>아래 주소로 로그인하셔서 비밀번호를 변경하시기 바랍니다.<hr><br>";
			content += "<p>방문하기 : <a href='http://49.142.157.251:9090/cjgreen/'>CJ Green프로젝트</a></p>";
			content += "<p><img src=\"cid:main.jpg\" width='500px'></p>";
			content += "<hr>";
			messageHelper.setText(content, true);
			
			// 본문에 기재된 그림파일의 경로를 따로 표시시켜준다. 그리고, 보관함에 다시 저장시켜준다.
			FileSystemResource file = new FileSystemResource("E:\\JavaWorkspace\\springframework\\works\\javawspring\\src\\main\\webapp\\resources\\images\\main.jpg");
			messageHelper.addInline("main.jpg", file);
			
			// 메일 전송하기
			mailSender.send(message);
			return "1";
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return "0";
	}
	
	// 비밀번호 변경처리
	@RequestMapping(value = "/memberPwdUpdate", method = RequestMethod.GET)
	public String memberPwdUpdateGet(String mid, String pwd) {
		
		return "member/memberPwdUpdate";
	}
	// 비밀번호 변경처리
	@RequestMapping(value = "/memberPwdUpdate", method = RequestMethod.POST)
	public String memberPwdUpdatePost(HttpSession session, String newPwd) {
		String mid = (String) session.getAttribute("sMid");
		
		memberService.setMemberPwdUpdate(mid, passwordEncoder.encode(newPwd));
		
		return "redirect:/msg/memberPwdUpdateOk";
	}
	
	
	
}
