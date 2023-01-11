<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>fileList.jsp</title>
	<jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
</head>
<body>

<p><br/></p>
<div class="container">
	<h2>서버 파일 리스트</h2>
	<hr/>
	<p>서버의 파일 경로 : ${ctp}/data/ckeditor/oo파일명</p>
	<hr/>
	<c:forEach var="file" items="${files}" varStatus="st">
		<img src="${ctp}/data/ckeditor/${file}" width="150px" /><hr/>
	</c:forEach>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"/>
</body>
</html>