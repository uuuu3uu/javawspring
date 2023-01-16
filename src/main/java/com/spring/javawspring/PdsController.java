package com.spring.javawspring;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spring.javawspring.common.SecurityUtil;
import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.AdminService;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.service.PdsService;
import com.spring.javawspring.vo.MemberVO;
import com.spring.javawspring.vo.PdsVO;

@Controller
@RequestMapping("/pds")
public class PdsController {

	@Autowired
	PdsService pdsService;
	
	@Autowired
	PageProcess pageProcess;
	
	
	@RequestMapping(value = "/pdsList", method=RequestMethod.GET)
	public String adminMainGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(name="part", defaultValue = "전체", required = false) String part) {
		
		PageVO pageVo = pageProcess.totRecCnt(pag, pageSize, "pds", part, "");
		List<PdsVO> vos = pdsService.getPdsList(pageVo.getStartIndexNo(), pageVo.getPageSize(), part);
		pageVo.setPart(part);
		System.out.println("vos : " + vos);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVo", pageVo);
		
		return "pds/pdsList";
	}
	
	@RequestMapping(value = "/pdsInput", method = RequestMethod.GET)
	public String pdsInputGet() {

		return "pds/pdsInput";
	}
	
	@RequestMapping(value = "/pdsInput", method = RequestMethod.POST)
	public String pdsInputPost(PdsVO vo,
			MultipartHttpServletRequest file) {		// 여러개 저장할때..
		String pwd = vo.getPwd();
		SecurityUtil security = new SecurityUtil();
		pwd = security.encryptSHA256(pwd);
		vo.setPwd(pwd);
		
		// 멀티파일을 서버에 저장시키고, 파일의 정보를 vo에 담아서 DB에 저장시킨다.
		pdsService.setPdsInput(file, vo);
		
		return "redirect:/msg/pdsInputOk";
	}
	
	// 파일 다운로드 시 다운로드횟수 증가
	@ResponseBody
	@RequestMapping(value = "/pdsDownNum", method = RequestMethod.POST)
	public String pdsDownNumPost(int idx) {
		pdsService.setPdsDownNum(idx);
		
		return "pds/pdsInput";
	}
	
	@RequestMapping(value = "/pdsSearch", method = RequestMethod.POST)
	public String pdsSearch(String serach, String searchString) {
		
		
		return "";
	}
	
	
}
