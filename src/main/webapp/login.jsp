<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ include file="navbar.jsp" %>
<%@ page import="org.auth.Auth" %>
<%@ page import="org.auth.User" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FOURBLOCK Login</title>
    <link rel="stylesheet" href="css/login.css">
</head>
<body>
    <div class="container">
        <div class="logo">FOURBLOCK</div>
        <div class="tagline">Create a shopping mall with JSP?</div>
        
        <form method="post" action="login.jsp">
            <input type="text" name="username" class="input-field" placeholder="사용자 이름" required>
            <input type="password" name="password" class="input-field" placeholder="비밀번호" required>
            <button type="submit" class="login-btn">로그인</button>
        </form>
        
        <div class="help-links">
          <a href="signup.jsp">회원가입</a>
          <span>|</span>
          <a href="#">이메일 찾기</a>
          <span>|</span>
          <a href="#">비밀번호 찾기</a>
        </div>
    </div>
</body>
</html>
<script>
    const loginButton = document.querySelector('.login-btn');
    const inputs = document.querySelectorAll('.input-field');

    function checkInputs() {
        const allFilled = Array.from(inputs).every(input => input.value.trim() !== '');
        if (allFilled) {
            loginButton.classList.add('active');
            loginButton.disabled = false;
        } else {
            loginButton.classList.remove('active');
            loginButton.disabled = true;
        }
    }

    inputs.forEach(input => {
        input.addEventListener('input', checkInputs);
    });
</script>

<%
if (request.getMethod().equalsIgnoreCase("post")) {
    // 폼 데이터 수집
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    // 로그인 처리
    User user = Auth.login(username, password);

    if (user != null) {
        // 로그인 성공 시 세션에 사용자 정보 저장
        session.setAttribute("username", user.getUsername());
        session.setAttribute("name", user.getName());
        session.setAttribute("user_id", user.getUserId());
        user = null;  // gc
%>
        <script>
            alert("로그인 성공!");
            location.href = "index.jsp";
        </script>
<%
    } else {
%>
        <script>
            alert("로그인 실패: 사용자 이름 또는 비밀번호가 잘못되었습니다.");
        </script>
<%
    }
}
%>

