<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
  <title>boInput.jsp</title>
  <script src="${ctp}/ckeditor/ckeditor.js"></script>
  <meta name="viewport" content="width=device-width, initial-scale=1"> 
  <jsp:include page="/WEB-INF/views/include/bs4.jsp" />
  <script>
  'use strict';
  function fCheck() {
  	let title = myform.title.value;
  	let content = myform.content.value;
  	
  	if(title.trim() == "") {
  		alert("게시글 제목을 입력하세요");
  		myform.title.focus();
  	}
  	/* else if(content.trim() == "") {
  		alert("게시글 글내용을 입력하세요");
  		myform.content.focus();
  	} */
  	else {
  		myform.submit();
  	}
  }
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp" />
<jsp:include page="/WEB-INF/views/include/slide2.jsp" />
<p><br/></p>
<div class="container">
	<form name="myform" method="post">
		<h2 class="text-center">게시판 글쓰기</h2>
		<br/>
		<table class="table">
			<tr>
				<th>글쓴이</th>
				<td>${sNickName}</td>
			</tr>
			<tr>
				<th>글제목</th>
				<td><input type="text" name="title" placeholder="글제목을 입력하세요" autofocus required class="form-control"></td>
			</tr>
			<tr>
				<th>이메일</th>
				<td><input type="text" name="email" placeholder="이메일을 입력하세요" value="${email}" class="form-control"></td>
			</tr>
			<tr>
				<th>홈페이지</th>
				<td><input type="text" name="homePage" placeholder="홈페이지 주소를 입력하세요" value="${homePage}" class="form-control"></td>
			</tr>
			<tr>
				<th>글내용</th>
				<td><textarea rows="6" name="content" id="CKEDITOR" class="form-control" required></textarea></td>
				<script>
					CKEDITOR.replace("content",{
						height: 500,
						filebrowserUploadUrl:"${ctp}/imageUpload",
						uploadUrl : "${ctp}/imageUpload"
					});
				</script>
			</tr>
			<tr>
				<td colspan="2" class="text-center">
					<input type="button" value="글올리기" onclick="fCheck()" class="btn btn-secondary"/> &nbsp;
					<input type="reset" value="다시입력" class="btn btn-warning"/> &nbsp;
					<input type="button" value="돌아가기" onclick="location.href='${ctp}/board/boardList?pag=${pag}&pageSize=${pageSize}';" class="btn btn-secondary"/> &nbsp;
				</td>
			</tr>
		</table>
		<input type="hidden" name="hostIp" value="${pageContext.request.remoteAddr}"/>
		<input type="hidden" name="mid" value="${sMid}"/>
		<input type="hidden" name="nickName" value="${sNickName}"/>
	</form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</body>
</html>