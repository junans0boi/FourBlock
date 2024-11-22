// 컴퓨터시스템공학과 2-B 202045066 이준환
package org.cart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/addToCartFromSearch")
public class AddToCartFromSearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String productId = request.getParameter("productId");
        String productName = request.getParameter("productName");
        String productPrice = request.getParameter("productPrice");
        String purchaseUrl = request.getParameter("purchaseUrl");
        String productImageUrl = request.getParameter("productImageUrl");

        if (productId == null || productName == null || productPrice == null || productId.isEmpty() || productName.isEmpty() || productPrice.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product details");
            return;
        }

        String url = "jdbc:mysql://fourblock.kro.kr:3306/FourBlock?useUnicode=true&characterEncoding=UTF-8";
        try (Connection conn = DriverManager.getConnection(url, "root", "0825")) {
            conn.setAutoCommit(false);

            int userId = CartUtil.getUserId(conn, username);
            CartUtil.addOrUpdateCart(conn, userId, productId, productName, productPrice, 1, productImageUrl, purchaseUrl);

            conn.commit();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred while adding to cart.");
        }
    }
}
