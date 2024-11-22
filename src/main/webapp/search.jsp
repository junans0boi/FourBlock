<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page import="org.naverApi.NaverApi, org.naverApi.SearchResult, java.text.NumberFormat, java.util.Locale, java.sql.Connection, java.sql.DriverManager, java.sql.PreparedStatement, java.sql.SQLException" %>
<jsp:include page="navbar.jsp" />
<%@ page import="org.db.DatabaseManager" %>


<%
    // 로그인 상태 확인
    String username = (String) session.getAttribute("username");
    boolean isLoggedIn = (username != null);

    request.setCharacterEncoding("UTF-8"); // Request 인코딩 설정
    // 검색어를 요청에서 가져오기
    String query = request.getParameter("query");
    if (query != null && !query.isEmpty()) {
        // 기존 쿠키 가져오기
        javax.servlet.http.Cookie[] cookies = request.getCookies();
        String recentSearches = "";
        if (cookies != null) {
            for (javax.servlet.http.Cookie cookie : cookies) {
                if ("recentSearches".equals(cookie.getName())) {
                    recentSearches = java.net.URLDecoder.decode(cookie.getValue(), "UTF-8");
                }
            }
        }

        // 검색어를 리스트로 관리
        java.util.List<String> searches = new java.util.ArrayList<>(java.util.Arrays.asList(recentSearches.split(",")));
        if (searches.contains(query)) {
            searches.remove(query); // 중복 제거
        }
        searches.add(0, query); // 최신 검색어를 맨 앞에 추가

        // 최대 검색어 개수를 제한 (예: 10개)
        if (searches.size() > 10) {
            searches = searches.subList(0, 10);
        }

        // 쿠키에 저장할 문자열 생성
        recentSearches = String.join(",", searches);
        javax.servlet.http.Cookie recentSearchCookie = new javax.servlet.http.Cookie("recentSearches", java.net.URLEncoder.encode(recentSearches, "UTF-8"));
        recentSearchCookie.setMaxAge(60 * 60 * 24 * 7); // 7일간 유지
        recentSearchCookie.setPath("/"); // 모든 경로에서 쿠키 접근 가능
        response.addCookie(recentSearchCookie);
    }

    String sort = request.getParameter("sort");
    if (sort == null || sort.isEmpty()) {
        sort = "sim"; // Default to similarity
    }

    SearchResult[] searchResults = null;
    if (query != null && !query.isEmpty()) {
        if ("sim".equals(sort)) {
            searchResults = NaverApi.searchShopping(query, 100, 1, "sim");
        } else {
            searchResults = NaverApi.searchShopping(query, 100, 1, sort);
        }
    }

    // 한국 로케일에 맞게 세 자리 쉼표를 위한 NumberFormat 생성
    NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);

    

    // JDBC 연결 설정
    try (Connection conn = DatabaseManager.getConnection()) {
        if (searchResults != null) {
            for (SearchResult product : searchResults) {
                String insertQuery = "INSERT INTO search_results (title, link, image, lprice, hprice, mall_name, product_id, product_type, search_query, userid) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                                    "ON DUPLICATE KEY UPDATE " +
                                    "lprice = VALUES(lprice), hprice = VALUES(hprice), mall_name = VALUES(mall_name), search_query = VALUES(search_query), userid = VALUES(userid)";
                try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                    stmt.setString(1, product.title != null && !product.title.isEmpty() ? product.title : null);
                    stmt.setString(2, product.link != null && !product.link.isEmpty() ? product.link : null);
                    stmt.setString(3, product.image != null && !product.image.isEmpty() ? product.image : null);
                    stmt.setInt(4, product.lprice != null ? Integer.parseInt(product.lprice) : 0);
                    stmt.setInt(5, (product.hprice != null && !product.hprice.isEmpty()) ? Integer.parseInt(product.hprice) : 0);
                    stmt.setString(6, product.mallName != null && !product.mallName.isEmpty() ? product.mallName : null);
                    stmt.setString(7, product.productId != null ? product.productId : null);
                    stmt.setString(8, product.productType != null ? product.productType : null);
                    stmt.setString(9, query != null ? query : "");
                    stmt.setString(10, username != null ? username : "guest");

                    stmt.executeUpdate();
                    System.out.println("데이터 삽입 성공: " + product.title);
                } catch (SQLException e) {
                    System.err.println("SQL 에러: " + e.getMessage());
                }
            }
        } else {
            System.out.println("검색 결과가 없습니다.");
        }
    } catch (SQLException e) {
        System.err.println("DB 연결 오류: " + e.getMessage());
    }



%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>검색 결과 - <%= query != null ? query : "검색" %></title>
    <link rel="stylesheet" href="css/search.css">
</head>
<body>
    
    <form class="search-bar" action="search.jsp" method="get">
        <input type="text" name="query" placeholder="검색어를 입력하세요" value="<%= query != null ? query : "" %>">
        <button type="submit">검색</button>
    </form>

    <div class="sort-options">
        <a href="search.jsp?query=<%= query %>&sort=sim" class="<%= ("sim".equals(sort) || sort == null) ? "active" : "" %>">정확도순</a>
        <a href="search.jsp?query=<%= query %>&sort=asc" class="<%= "asc".equals(sort) ? "active" : "" %>">낮은 가격순</a>
        <a href="search.jsp?query=<%= query %>&sort=dsc" class="<%= "dsc".equals(sort) ? "active" : "" %>">높은 가격순</a>
    </div>

    <div class="container">
        <% if (query == null || query.isEmpty()) { %>
            <h2>검색어를 입력하세요.</h2>
        <% } else if (searchResults == null) { %>
            <h2>검색 결과를 가져오지 못했습니다.</h2>
        <% } else { %>
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
                        <button class="icon" onclick="addToCart('<%= product.productId %>', '<%= product.title %>', '<%= product.lprice %>', '<%= product.link %>', '<%= product.image %>')">🛒</button>
                    </div>
                </div>
            <% } %>
        <% } %>
    </div>

    <!-- JavaScript -->
    <script>
        // 로그인 상태를 서버에서 가져오기
        const isLoggedIn = <%= isLoggedIn %>;

        function addToCart(productId, productName, productPrice, purchaseUrl, productImageUrl) {
        if (!isLoggedIn) {
            alert("로그인이 필요합니다.");
            window.location.href = "login.jsp"; // 로그인 페이지로 이동
            return;
        }

        // 로그인 상태일 경우, 장바구니에 추가 요청
        fetch('/addToCart', { // 통합된 서블릿 경로
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({
                productId: productId,
                productName: productName,
                productPrice: productPrice,
                purchaseUrl: purchaseUrl,
                productImageUrl: productImageUrl
            })
        })
        .then(response => {
            if (response.ok) {
                alert("장바구니에 추가되었습니다.");
            } else {
                alert("장바구니 추가 실패!");
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("장바구니 추가 중 오류가 발생했습니다.");
        });
    }
    </script>
</body>
</html>
