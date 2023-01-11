package com.spring.javawspring.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

public class JavawspringProvide {
	public int fileUpload(MultipartFile fName) {
		int res = 0;
		try {
			UUID uid = UUID.randomUUID();	// 중복된 이름을 주지 않으려고
			String oFileName = fName.getOriginalFilename();
			String saveFileName = uid + "_" + oFileName;
			
			writeFile(fName, saveFileName, "");
			res = 1;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	// 
	public void writeFile(MultipartFile fName, String saveFileName, String flag) throws IOException {
		byte[] data = fName.getBytes();
		// 걍 적어두고 쓰렴...^^..
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		String realPath = request.getSession().getServletContext().getRealPath("/resources/"+flag+"/");
		
		
		FileOutputStream fos = new FileOutputStream(realPath + saveFileName); // 절대경로 + 고유번호 고유이름 저장한 파일이름
		fos.write(data);
		fos.close();
	}
}
