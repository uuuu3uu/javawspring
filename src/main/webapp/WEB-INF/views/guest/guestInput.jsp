<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
  <title>guestInput.jsp</title>
  <jsp:include page="/WEB-INF/views/include/bs4.jsp"></jsp:include>
  <script>
  	
  </script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/nav.jsp"/>
<jsp:include page="/WEB-INF/views/include/slide2.jsp"/>
<p><br/></p>
<div class="container">
	<form name="myform" method="post" class="was-validated"> 
		<h2>방 명 록 글 올 리 기</h2>
		<br/>
		<div class="form-group">
      <label for="name">성명</label>
      <input type="text" class="form-control" name="name" id="name" placeholder="이름을 입력하세요" required />
      <div class="valid-feedback">통과</div>
      <div class="invalid-feedback">이름 입력은 필수입니다</div>
    </div>
    <div class="form-group">
      <label for="email">E-mail</label>
      <input type="text" class="form-control" name="email" id="email" placeholder="이메일을 입력하세요" required />
      <div class="valid-feedback">사용할 수 있는 이메일입니다</div>
      <div class="invalid-feedback">이메일은 영문 대/소문자와 숫자만 사용할 수 있습니다</div> 
    </div>
    <div class="form-group">
      <label for="homePage">홈페이지</label>
      <input type="text" class="form-control" name="homePage" id="homePage" value="http://" placeholder="홈페이지 주소를 입력하세요" />
      <!-- <div class="valid-feedback">통과</div>
      <div class="invalid-feedback">이메일은 선택사항입니다</div> -->
    </div>
    <div class="form-group">
      <label for="content">방문소감</label>
      <textarea rows="5" class="form-control" name="content" id="content" required></textarea>
      <div class="valid-feedback">통과</div>
      <div class="invalid-feedback">방문소감 입력은 필수입니다</div>
    </div>
    <div class="form-group">
	    <button type="submit" class="btn btn-primary">방명록 등록</button>
	    <button type="reset" class="btn btn-primary">방명록 다시입력</button>
	    <button type="button" onclick="location.href='${ctp}/guest/guestList';" class="btn btn-primary">돌아가기</button><!--${ctp}절대경로 입력 필수  -->
		</div>
		<!-- hidden 모아두기 -->
		<input type="hidden" name="hostIp" value="<%=request.getRemoteAddr() %>"/>
	</form>
</div>
<p><br/></p>
<jsp:include page="/WEB-INF/views/include/footer.jsp"/>
</body>
</html>