<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%request.setCharacterEncoding("UTF-8");%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>FOURBLOCK Navbar</title>
  <link rel="stylesheet" href="css/navbar.css">
</head>
<body>
  <header class="navbar">
    <!-- Logo -->
    <a href="index.jsp" class="logo">FOURBLOCK</a>
    
    <div class="icons">
      <a href="index.jsp">HOME</a>
      
      <%
      if (session != null && session.getAttribute("username") != null) {
          // 로그인한 경우
          String name = (String) session.getAttribute("name");
      %>
          <a href="mypage.jsp"><%= name %>님</a>
          <a href="logout.jsp">로그아웃</a>
      <%
      } else {
          // 로그인하지 않은 경우
      %>
          <a href="login.jsp">로그인</a>
          <a href="signup.jsp">회원가입</a>
      <%
      }
      %>
      
      <a href="search_page.jsp">🔍</a>
      <a href="cart.jsp">🛒</a>
    </div>
  </header>
</body>
</html>
