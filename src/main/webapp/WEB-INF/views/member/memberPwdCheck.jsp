<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>memberPwdUpdate.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
	<script>
  'use strict';
  	function fCheck() {
			let regPwd = /(?=.*[0-9a-zA-Z]).{4,20}$/;
    	
    	let oldPwd = document.getElementById("oldPwd").value;
    	
  		
    	if(oldPwd.trim() == "") {
    		alert("비밀번호를 입력하세요");
    		document.getElementById("oldPwd").focus();
    	}
    	else {
    		myform.submit();
    	}
	}
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"/>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"/>
<p><br/></p>
<div class="container">
	<h2>비밀번호 확인하기</h2>
	<form name="myform" method="post" class="was-validated">
		<h2 class="valid-feedback">비밀번호 확인하기</h2>
		<br/>
		<table class="table table-bordered">
			<tr>
				<th>비밀번호</th>
				<td>
					<input type="password" name="oldPwd" id="oldPwd" autofocus required class="form-control"/>
					<div class="invalid-feedback">비밀번호를 입력하세요</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="text-center">
					<input type="button" value="비밀번호확인" onclick="fCheck()" class="btn btn-success"/> &nbsp;
        	<input type="reset" value="다시입력" class="btn btn-success"/> &nbsp;
        	<input type="button" value="돌아가기" onclick="location.href='${ctp}/memberMain';" class="btn btn-success"/>
      	</td>
			</tr>
		</table>
	</form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"/>
</body>
</html>