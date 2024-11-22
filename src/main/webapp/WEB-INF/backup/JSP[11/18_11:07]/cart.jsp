<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page import="java.util.*, java.sql.*, java.text.NumberFormat" %>
<%@ page import="org.db.DatabaseManager" %>
<%@ include file="navbar.jsp" %>

<%
    String username = (String) session.getAttribute("username");

    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<Map<String, Object>> cartItems = new ArrayList<>();

    try (Connection conn = DatabaseManager.getConnection()) {
        String query = "SELECT c.product_id, c.quantity, c.product_name, c.price, c.image_url, c.link " +
                       "FROM cart c JOIN users u ON c.user_id = u.user_id WHERE u.username = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("product_id", rs.getString("product_id"));
                    item.put("product_name", rs.getString("product_name"));
                    item.put("quantity", rs.getInt("quantity"));
                    item.put("price", rs.getDouble("price"));
                    item.put("image_url", rs.getString("image_url"));
                    item.put("link", rs.getString("link"));

                    cartItems.add(item);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    NumberFormat currencyFormatter = NumberFormat.getNumberInstance(Locale.KOREA);
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>장바구니</title>
    <link rel="stylesheet" href="css/cart.css">
</head>
<body>
<div class="cart-container">
    <h1>장바구니</h1>
    <% if (cartItems.isEmpty()) { %>
        <p>장바구니가 비어 있습니다.</p>
    <% } else { %>
        <div class="cart-header">
            <input type="checkbox" id="select-all">
            <label for="select-all">전체 선택</label>
            <button class="delete-selected">선택 삭제</button>
        </div>

        <div class="cart-items">
            <% for (Map<String, Object> item : cartItems) { %>
                <div class="cart-item">
                    <input type="checkbox" class="item-select" data-price="<%= item.get("price") %>" data-quantity="<%= item.get("quantity") %>">
                    <div class="item-image">
                        <img src="<%= item.get("image_url") %>" alt="<%= item.get("product_name") %>">
                    </div>
                    <div class="item-details">
                        <h3><%= item.get("product_name") %></h3>
                        <p>수량: <%= item.get("quantity") %></p>
                        <p>가격: <%= currencyFormatter.format(item.get("price")) %>원</p>
                    </div>
                    <button class="delete-item" data-product-id="<%= item.get("product_id") %>">삭제</button>
                    <a href="<%= item.get("link") %>" class="purchase-button" target="_blank">바로 주문</a>
                </div>
            <% } %>
        </div>

        <div class="cart-summary">
            <div class="summary-details">
                <p>총 상품금액: <span id="total-price">₩0</span></p>
                <p>배송비: <span id="shipping-fee">₩0</span></p>
                <p><strong>총 결제 금액: <span id="final-total">₩0</span></strong></p>
            </div>
            <button class="checkout-button">바로 주문</button>
        </div>
    <% } %>
</div>

<script src="js/cart.js"></script>
</body>
</html>
