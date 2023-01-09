package com.spring.javawspring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {
	
	@RequestMapping(value="/msg/{msgFlag}", method=RequestMethod.GET)
	public String msgGet(@PathVariable String msgFlag, Model model,
			@RequestParam(value="mid", defaultValue = "", required = false) String mid) {
		
		if(msgFlag.equals("memberLoginOk")) {
			model.addAttribute("msg", mid + "님 로그인 되었습니다");
			model.addAttribute("url","member/memberMain");
		}
		else if(msgFlag.equals("memberLogout")) {
			model.addAttribute("msg", mid + "님 로그아웃 되셨습니다");
			model.addAttribute("url","member/memberLogin");
		}
		else if(msgFlag.equals("memberLoginNo")) {
			model.addAttribute("msg", "로그인 실패");
			model.addAttribute("url","member/memberLogin");
		}
		else if(msgFlag.equals("guestInputOk")) {
			model.addAttribute("msg", "방명록에 글이 등록되었습니다");
			model.addAttribute("url","guest/guestList");
		}
		else if(msgFlag.equals("guestDeleteOk")) {
			model.addAttribute("msg", "삭제되었습니다");
			model.addAttribute("url","guest/guestList");
		}
		else if(msgFlag.equals("memberJoinOk")) {
			model.addAttribute("msg", "회원가입 완료");
			model.addAttribute("url","member/memberLogin");
		}
		else if(msgFlag.equals("memberIdCheckNo")) {
			model.addAttribute("msg", "중복된 아이디 입니다");
			model.addAttribute("url","member/memberJoin");
		}
		else if(msgFlag.equals("memberNickNameCheckNo")) {
			model.addAttribute("msg", "중복된 닉네임 입니다");
			model.addAttribute("url","member/memberJoin");
		}
		else if(msgFlag.equals("adminNo")) {
			model.addAttribute("msg", "관리자가 아닙니다");
			model.addAttribute("url","member/memberLogin");
		}
		else if(msgFlag.equals("memberNo")) {
			model.addAttribute("msg", "로그인 후 사용하세요");
			model.addAttribute("url","member/memberLogin");
		}
		else if(msgFlag.equals("levelCheckNo")) {
			model.addAttribute("msg", "권한이 없습니다");
			model.addAttribute("url","member/memberMain");
		}
		else if(msgFlag.equals("mailSendOk")) {
			model.addAttribute("msg", "메일을 정상적으로 전송했습니다");
			model.addAttribute("url","study/mail/mailForm");
		}
		else if(msgFlag.equals("memberDelete")) {
			model.addAttribute("msg", "탈퇴하시겠습니까?");
			model.addAttribute("url","member/memberDeleteOk");
		}
		else if(msgFlag.equals("memberDeleteOk")) {
			model.addAttribute("msg", "정상적으로 탈퇴처리 되었습니다.");
			model.addAttribute("url","member/memberList");
		}
		
		
		
		return "include/message";
	}
}
