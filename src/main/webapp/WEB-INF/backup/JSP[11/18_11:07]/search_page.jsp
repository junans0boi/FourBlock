<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%request.setCharacterEncoding("UTF-8");%>
<%@ page import="java.util.List, java.util.ArrayList" %>
<%@ include file="navbar.jsp" %>

<%
    // 세션을 사용하여 최근 검색어 목록을 관리합니다.
    List<String> recentSearches = (List<String>) session.getAttribute("recentSearches");
    if (recentSearches == null) {
        recentSearches = new ArrayList<>();
        session.setAttribute("recentSearches", recentSearches);
    }
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
                <% for (String keyword : recentSearches) { %>
                    <span class="tag"><%= keyword %></span>
                <% } %>
            </div>
        </section>

        <!-- 인기 검색어 섹션 -->
        <section class="popular-search">
            <h3>인기 검색어 <span class="update-time">11.14 13:00 기준</span></h3>
            <div class="search-list">
                <ul>
                    <li>1 1996Nuptse <span class="new">NEW</span></li>
                    <li>2 눕시 블랙 <span class="new">NEW</span></li>
                    <li>3 베이스먼트</li>
                    <li>4 x basement <span class="new">NEW</span></li>
                    <li>5 랩</li>
                    <!-- 나머지 항목 추가 가능 -->
                </ul>
                <ul>
                    <li>11 산산가어</li>
                    <li>12 스누</li>
                    <li>13 뉴발란스 993</li>
                    <li>14 991</li>
                    <li>15 뉴발</li>
                    <!-- 나머지 항목 추가 가능 -->
                </ul>
            </div>
        </section>
    </div>
</div>

</body>
</html>
