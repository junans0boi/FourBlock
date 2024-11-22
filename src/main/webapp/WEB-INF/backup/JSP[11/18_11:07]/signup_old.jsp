<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ include file="navbar.jsp" %>
<%@ page import="java.sql.*" %>
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
            <input type="text" name="username" class="input-field" placeholder="사용자 이름*" required>
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

<%
if (request.getMethod().equalsIgnoreCase("post")) {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String name = request.getParameter("name");
    String email = request.getParameter("email");
    
    boolean registrationSuccess = false;
    boolean isDuplicate = false;
    
    System.out.println("디버그: 회원가입 시작"); // 디버깅 메시지
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("디버그: MySQL 드라이버 로드 성공"); // 디버깅 메시지

        Connection conn = DriverManager.getConnection("jdbc:mysql://fourblock.kro.kr:3306/FourBlock?characterEncoding=UTF-8&useUnicode=true", "root", "0825");


        System.out.println("디버그: 데이터베이스 연결 성공"); // 디버깅 메시지

        // 중복 확인
        String checkQuery = "SELECT * FROM users WHERE username = ? OR email = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
        checkStmt.setString(1, username);
        checkStmt.setString(2, email);
        
        ResultSet rs = checkStmt.executeQuery();
        
        if (rs.next()) {
            isDuplicate = true;
            System.out.println("디버그: 중복된 사용자 이름 또는 이메일 존재"); // 디버깅 메시지
        } else {
            System.out.println("디버그: 중복된 사용자 없음, 데이터 삽입 시작"); // 디버깅 메시지
        }
        
        rs.close();
        checkStmt.close();

        if (!isDuplicate) {
            // 삽입
            String query = "INSERT INTO users (username, password, name, email, created_at) VALUES (?, ?, ?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setString(4, email);
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                registrationSuccess = true;
                System.out.println("디버그: 회원가입 성공"); // 디버깅 메시지
            } else {
                System.out.println("디버그: 데이터베이스에 데이터 삽입 실패"); // 디버깅 메시지
            }
            
            stmt.close();
        }
        
        conn.close();
        
    } catch (Exception e) {
        System.out.println("디버그: 예외 발생 - " + e.getMessage());
        e.printStackTrace();
    }
    
    if (registrationSuccess) {
%>
        <script>alert("회원가입 성공! 로그인 페이지로 이동합니다."); location.href = "login.jsp";</script>
<%
    } else if (isDuplicate) {
%>
        <script>alert("회원가입 실패: 이미 존재하는 사용자 이름 또는 이메일입니다.");</script>
<%
    } else {
%>
        <script>alert("회원가입 실패: 다시 시도해 주세요.");</script>
<%
    }
}
%>
