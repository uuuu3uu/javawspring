package com.spring.javawspring;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spring.javawspring.common.SecurityUtil;
import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.PdsService;
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
	
	// 자료실 검색기
	@RequestMapping(value = "/pdsSearch", method = RequestMethod.POST)
	public String pdsSearchPost(String serach, String searchString) {
	
		return "";
	}
	
	// pds내용 삭제처리하기(삭제처리하기 전에 비밀번호 먼저 체크하여 맞으면 삭제처리한다)
	@ResponseBody
	@RequestMapping(value = "/pdsDelete", method = RequestMethod.POST)
	public String pdsDeletePost(int idx, String pwd) {
		//SecurityUtil security = new SecurityUtil(); // 왜냐믄 SecurityUtil를 static을 선언하였기에 메소드 영역에 올라가 있다. 이때는 클래스명으로 후출해서 사용한다
		pwd = SecurityUtil.encryptSHA256(pwd);
		
		PdsVO vo = pdsService.getPdsContent(idx);
		if(!pwd.equals(vo.getPwd())) return "0";
				
		// 비밀번호가 맞으면 파일 삭제 후 DB의 내역을 삭제처리한다
		pdsService.setPdsDelete(vo);
		
		return "1";
	}
	
	@RequestMapping(value = "/pdsTotalDown", method = RequestMethod.GET)
	public String pdsTotalDownGet(HttpServletRequest request, int idx) throws IOException {
		// 파일 압축다운로드전에 다운로드수를 증가시킨다.
		pdsService.setPdsDownNum(idx);
		
		// 여러개의 파일일 경우 모든 파일을 하나의 파일로 압축(?=통합)하여 다운한다. 이때 압축파일명은 '제목'으로 처리한다.
		String realPath = request.getSession().getServletContext().getRealPath("/resources/data/pds/");
		
		PdsVO vo = pdsService.getPdsContent(idx);
		
		String[] fNames = vo.getFName().split("/");
		String[] fSNames = vo.getFSName().split("/");
		
		String zipPath = realPath + "temp/";
		String zipName = vo.getTitle() + ".zip";
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath + zipName));
		
		byte[] buffer = new byte[2048];
		
		for(int i=0; i<fSNames.length; i++) {
			fis = new FileInputStream(realPath + fSNames[i]);
			fos = new FileOutputStream(zipPath + fNames[i]);
			File moveAndRename = new File(zipPath + fNames[i]);
			
			// fos에 파을 쓰기작업
			int data;
			while((data = fis.read(buffer, 0, buffer.length)) != -1) {
				fos.write(buffer, 0, data);
			}
			fos.flush();
			fos.close();
			fis.close();
			
			// zip파일에 fos를 넣는 작업
			fis = new FileInputStream(moveAndRename);
			zout.putNextEntry(new ZipEntry(fNames[i]));
			
			while((data = fis.read(buffer, 0, buffer.length)) != -1) {
				zout.write(buffer, 0, data);
			}
			zout.flush();
			zout.closeEntry();
			fis.close();
		}
		zout.close();
		
		return "redirect:/pds/pdsDownAction?file="+java.net.URLEncoder.encode(zipName);
	}
	
	@RequestMapping(value = "/pdsDownAction", method = RequestMethod.GET)
	public void pdsDownActionGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String file = request.getParameter("file");
		
		String downPathFile = request.getSession().getServletContext().getRealPath("/resources/data/pds/temp/") + file;
		
		File downFile = new File(downPathFile);

		String downFileName = new String(file.getBytes("UTF-8"), "8859_1");
		
		response.setHeader("Content-Disposition", "attachment;filename="+downFileName);
		
		FileInputStream fis = new FileInputStream(downFile);
		ServletOutputStream sos = response.getOutputStream();
		
		byte[] buffer = new byte[2048];
		int data = 0;
		while((data = fis.read(buffer, 0, buffer.length)) != -1) {
			sos.write(buffer, 0, data);
		}
		sos.flush();
		sos.close();
		fis.close();
		
		// 다운로드 완료후 temp폴더의 파일들을 모두 삭제한다.
		// new File(downPathFile).delete();
		downFile.delete();
	}
	
}
