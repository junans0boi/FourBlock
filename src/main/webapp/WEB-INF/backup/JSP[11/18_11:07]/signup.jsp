<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ include file="navbar.jsp" %>
<%@ page import="org.auth.Auth" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FourBlock 회원가입</title>
    <link rel="stylesheet" href="css/signup.css">
</head>
<body>
    <div class="container">
        <div class="title">회원가입</div>
        
        <form method="post" action="signup.jsp">
            <input type="text" name="username" class="input-field" placeholder="ID*" required>
            <input type="password" name="password" class="input-field" placeholder="비밀번호*" required>
            <input type="text" name="name" class="input-field" placeholder="이름*" required>
            <input type="email" name="email" class="input-field" placeholder="이메일 주소*" required>
            <button type="submit" class="submit-btn">가입하기</button>
        </form>
        
        <p style="text-align: center; font-size: 14px; margin-top: 10px;">
            이미 계정이 있으신가요? <a href="login.jsp">로그인</a>
        </p>
    </div>
</body>
</html>

<script>
    const signupButton = document.querySelector('.submit-btn');
    const signupInputs = document.querySelectorAll('.input-field');
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    function checkSignupInputs() {
        const allFilled = Array.from(signupInputs).every(input => input.value.trim() !== '');
        const emailValid = emailRegex.test(document.querySelector('input[name="email"]').value);

        if (allFilled && emailValid) {
            signupButton.classList.add('active');
            signupButton.disabled = false;
        } else {
            signupButton.classList.remove('active');
            signupButton.disabled = true;
        }
    }

    signupInputs.forEach(input => {
        input.addEventListener('input', checkSignupInputs);
    });
</script>


<%
if (request.getMethod().equalsIgnoreCase("post")) {
    // 회원가입 폼 데이터 수집
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String name = request.getParameter("name");
    String email = request.getParameter("email");

    // 회원가입 처리
    int registrationStatus = Auth.register(username, password, name, email);
    boolean registrationSuccess = registrationStatus == 0;
    boolean isDuplicate = registrationStatus == 1;

    // 결과 처리
    if (registrationSuccess) {
%>
        <script>
            alert("회원가입 성공! 로그인 페이지로 이동합니다.");
            location.href = "login.jsp";
        </script>
<%
    } else if (isDuplicate) {
%>
        <script>
            alert("회원가입 실패: 이미 존재하는 사용자 이름 또는 이메일입니다.");
        </script>
<%
    } else {
%>
        <script>
            alert("회원가입 실패: 다시 시도해 주세요.");
        </script>
<%
    }
}
%>
