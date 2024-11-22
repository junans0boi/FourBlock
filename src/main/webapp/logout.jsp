<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%request.setCharacterEncoding("UTF-8");%>
<%@ page import="javax.servlet.http.*, javax.servlet.*" %>

<%
if (session != null) {
    session.invalidate(); // 세션 무효화
}
response.sendRedirect("index.jsp"); // 로그아웃 후 메인 페이지로 이동
%>
