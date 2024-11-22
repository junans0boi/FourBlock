<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ include file="navbar.jsp" %>
<%@ page import="java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet" %>
<%@ page import="org.db.DatabaseManager" %>

<%
    // 세션에서 사용자 이름 가져오기
    String username = (String) session.getAttribute("username");

    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String name = "";
    String email = "";

    try (Connection conn = DatabaseManager.getConnection()) {
        String query = "SELECT name, email FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("name");
                    email = rs.getString("email");
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Page</title>
    <link rel="stylesheet" href="css/mypage.css">
</head>
<body>
    <div class="container">
        <div class="profile-section">
            <div class="profile-picture"></div>
            <div class="profile-details">
                <strong><%= name %></strong><br>
                <%= email %>
            </div>
        </div>
    </div>
</body>
</html>
