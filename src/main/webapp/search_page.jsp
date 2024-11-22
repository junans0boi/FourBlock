<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page import="java.util.List, java.util.ArrayList, java.util.Arrays" %>
<%@ include file="navbar.jsp" %>

<%
    // 인기 검색어 데이터를 서블릿으로부터 전달받음
    List<String> top10Keywords = (List<String>) request.getAttribute("top10Keywords");

    // 데이터가 없는 경우 서블릿으로 리다이렉트
    if (top10Keywords == null) {
        response.sendRedirect("popularSearch"); // 서블릿 경로
        return;
    }

    // 현재 시각 가져오기
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy.MM.dd HH:mm");
    java.util.TimeZone timeZone = java.util.TimeZone.getTimeZone("Asia/Seoul"); // 한국 시간대 설정
    formatter.setTimeZone(timeZone);
    String currentTime = formatter.format(new java.util.Date());

    // 쿠키에서 최근 검색어 읽기
    javax.servlet.http.Cookie[] cookies = request.getCookies();
    String recentSearches = "";
    if (cookies != null) {
        for (javax.servlet.http.Cookie cookie : cookies) {
            if ("recentSearches".equals(cookie.getName())) {
                recentSearches = java.net.URLDecoder.decode(cookie.getValue(), "UTF-8");
            }
        }
    }

    // 검색어 리스트로 변환
    List<String> searches = recentSearches.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(recentSearches.split(",")));
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>검색 페이지</title>
    <link rel="stylesheet" href="css/search_page.css">
</head>
<body>

<div class="search-container">
    <div class="search-header">
        <!-- 검색 입력란과 버튼 -->
        <form action="search.jsp" method="get" class="search-form">
            <input type="text" name="query" placeholder="브랜드, 상품, 프로필, 태그 등" class="search-input">
            <button type="submit" class="search-button">검색</button>
        </form>
    </div>
    <div class="search-content">
        <!-- 최근 검색어 섹션 -->
        <section class="recent-search">
            <h3>최근 검색어</h3>
            <div class="tags">
                <% if (!searches.isEmpty()) {
                    for (String keyword : searches) { %>
                        <span class="tag">
                            <a href="search.jsp?query=<%= java.net.URLEncoder.encode(keyword, "UTF-8") %>"><%= keyword %></a>
                        </span>
                <% } } else { %>
                    <span class="tag">최근 검색어가 없습니다.</span>
                <% } %>
            </div>
        </section>

  <!-- 인기 검색어 섹션 -->
        <section class="popular-search">
            <h3>인기 검색어 <span class="update-time"><%= currentTime %> 기준</span></h3>
            <div class="search-list">
                <div class="tags">
                    <% if (top10Keywords != null && !top10Keywords.isEmpty()) {
                        int rank = 1;
                        for (String keyword : top10Keywords) { %>
                            <span class="tag">
                                <span class="rank"><%= rank++ %>위</span> <a href="search.jsp?query=<%= keyword %>"><%= keyword %></a>
                            </span>
                    <% } 
                    } else { %>
                        <span class="tag">인기 검색어 데이터를 불러올 수 없습니다.</span>
                    <% } %>
                </div>
            </div>
        </section>
    </div>
</div>
</body>
</html>
