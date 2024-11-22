<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%request.setCharacterEncoding("UTF-8");%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>FOURBLOCK</title>
    <link rel="stylesheet" href="css/index.css">
</head>
<body>
    <jsp:include page="navbar.jsp" />

    <!-- Main Container -->
    <div class="main-container">
        <!-- Search Bar Above Slider -->
        <div class="search-bar">
            <form action="search.jsp" method="get">
                <input type="text" name="query" placeholder="검색어를 입력하세요" class="search-input">
                <button type="submit" class="search-button">검색</button>
            </form>
        </div>

        <!-- Image Slider Section -->
        <div class="slider">
            <div class="slides">
                <div class="slide"><img src="images/banner1.png" alt="Banner 1"></div>
                <div class="slide"><img src="images/banner2.png" alt="Banner 2"></div>
                <div class="slide"><img src="images/banner3.png" alt="Banner 3"></div>
                <div class="slide"><img src="images/banner4.png" alt="Banner 4"></div>
            </div>
            <div class="slider-controls">
                <span class="control prev" onclick="moveSlide(-1)">❮</span>
                <span class="control next" onclick="moveSlide(1)">❯</span>
            </div>
            <div class="slider-dots">
                <span class="dot" onclick="currentSlide(1)"></span>
                <span class="dot" onclick="currentSlide(2)"></span>
                <span class="dot" onclick="currentSlide(3)"></span>
                <span class="dot" onclick="currentSlide(4)"></span>
            </div>
        </div>

        
        <!-- Recommended Categories Section -->
        <div class="category-container">
            <div class="category-row">
                <a href="search.jsp?query=패딩" class="category-item">
                    <img src="images/category_padding.png" alt="패딩">
                    <span>패딩</span>
                </a>
                <a href="search.jsp?query=니트" class="category-item">
                    <img src="images/category_knit.png" alt="니트">
                    <span>니트</span>
                </a>
                <a href="search.jsp?query=스니커즈" class="category-item">
                    <img src="images/category_sneakers.png" alt="스니커즈">
                    <span>스니커즈</span>
                </a>
                <a href="search.jsp?query=후드" class="category-item">
                    <img src="images/category_hood.png" alt="후드">
                    <span>후드</span>
                </a>
                <a href="search.jsp?query=후드집업" class="category-item">
                    <img src="images/category_zipup.png" alt="후드집업">
                    <span>후드집업</span>
                </a>
                <a href="search.jsp?query=자켓" class="category-item">
                    <img src="images/category_jacket.png" alt="자켓">
                    <span>자켓</span>
                </a>
                <a href="search.jsp?query=어그" class="category-item">
                    <img src="images/category_shoes.png" alt="어그">
                    <span>어그</span>
                </a>
                <a href="search.jsp?query=모자" class="category-item">
                    <img src="images/category_hat.png" alt="모자">
                    <span>모자</span>
                </a>
                <a href="search.jsp?query=지갑" class="category-item">
                    <img src="images/category_Wallets.png" alt="지갑">
                    <span>지갑</span>
                </a>
            </div>
        </div>
    </div>

    <!-- Footer Section -->
    <footer style="background-color: #333; color: #fff; text-align: center; padding: 20px; margin-top: 30px; font-size: 14px;">
        <p>Copyright©2024 INHATC FOURBLOCK All rights reserved.</p>
        <p>Server Deployment Management Project FOURBLOCK</p>
        <p>Junhwan Lee, Jiwon Jung, Kwanyong Lee, Yubin Kang</p>
    </footer>
    <script src="js/index.js"></script>
</body>
</html>
