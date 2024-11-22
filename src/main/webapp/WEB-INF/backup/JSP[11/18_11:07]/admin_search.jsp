<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="org.naverApi.NaverApi, org.naverApi.SearchResult, org.naverApi.NaverProductSaver, java.text.NumberFormat, java.util.Locale" %>
<%@ include file="navbar.jsp" %>
<%
    request.setCharacterEncoding("UTF-8"); // Request 인코딩 설정

    // 로그인 상태 확인
    String username = (String) session.getAttribute("username");
    Integer userId = (Integer) session.getAttribute("user_id"); // 세션에서 user_id 가져오기

    boolean isLoggedIn = (username != null && userId != null && userId > 0); // 초기화

    if (!isLoggedIn) {
        System.out.println("Redirecting: Not logged in or invalid user session");
        response.sendRedirect("login.jsp");
        return;
    }



    // 검색어 가져오기
    String query = request.getParameter("query");
    SearchResult[] searchResults = null;
    if (query != null && !query.isEmpty()) {
        searchResults = NaverApi.searchShopping(query);
        if (searchResults != null) {
            NaverProductSaver.saveSearchResults(userId, query, searchResults); // userId 추가
        }
    }

    // 한국 로케일에 맞게 세 자리 쉼표를 위한 NumberFormat 생성
    NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>관리자 상품 검색 및 저장 - <%= query != null ? query : "검색" %></title>
</head>
<body>
    <h1>관리자 상품 검색 및 저장</h1>
    <div class="container">
        <% if (query == null || query.isEmpty()) { %>
            <h2>검색어를 입력하세요.</h2>
        <% } else if (searchResults == null) { %>
            <h2>검색 결과를 가져오지 못했습니다.</h2>
        <% } else { %>
            <h2><%= searchResults.length %>개의 상품이 저장되었습니다.</h2>
            <% for (SearchResult product : searchResults) { %>
                <% 
                    String formattedPrice = "가격 정보 없음";
                    if (product.lprice != null && !product.lprice.isEmpty()) { 
                        try {
                            formattedPrice = numberFormat.format(Integer.parseInt(product.lprice)) + "원";
                        } catch (NumberFormatException e) { 
                            formattedPrice = "가격 정보 오류"; 
                        } 
                    }
                %>
                <div class="product-card">
                    <a href="<%= product.link %>" target="_blank">
                        <img src="<%= product.image %>" alt="<%= product.title %>">
                        <div class="product-title"><%= product.title %></div>
                    </a>
                    <div class="price"><%= formattedPrice %></div>
                    <div class="details">
                        <span>상점: <%= product.mallName %></span>
                    </div>
                </div>
            <% } %>
        <% } %>
    </div>

    <form action="admin_search.jsp" method="get" class="search-form">
        <input type="text" name="query" placeholder="검색어를 입력하세요" required>
        <button type="submit">검색</button>
    </form>
  <%
    System.out.println("Session Debug: username = " + session.getAttribute("username"));
    System.out.println("Session Debug: user_id = " + session.getAttribute("user_id"));
    System.out.println("Session Debug: isLoggedIn = " + isLoggedIn);
%>


</body>
</html>
