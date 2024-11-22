<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%request.setCharacterEncoding("UTF-8");%>
<%@ include file="navbar.jsp" %>
<%@ page import="java.sql.*, javax.servlet.http.*, javax.servlet.*" %>
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

<%
if (request.getMethod().equalsIgnoreCase("post")) {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    
    boolean loginSuccess = false;
    String name = null;
    int userId = -1; // 초기값 설정

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://fourblock.kro.kr:3306/FourBlock?characterEncoding=UTF-8&useUnicode=true", "root", "0825");

        String query = "SELECT user_id, name FROM users WHERE username = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            loginSuccess = true;
            name = rs.getString("name");
            userId = rs.getInt("user_id"); // user_id 가져오기
        }

        rs.close();
        stmt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    if (loginSuccess) {
        // 세션에 username, name, user_id 저장
        session.setAttribute("username", username);
        session.setAttribute("name", name);
        session.setAttribute("user_id", userId); // user_id 저장
%>
        <script>alert("로그인 성공!"); location.href = "index.jsp";</script>
<%
    } else {
%>
        <script>alert("로그인 실패: 사용자 이름 또는 비밀번호가 잘못되었습니다.");</script>
<%
    }
}
%>
