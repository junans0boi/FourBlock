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

@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String productId = request.getParameter("product_id");
        String quantityStr = request.getParameter("quantity");
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quantity");
            return;
        }

        String url = "jdbc:mysql://fourblock.kro.kr:3306/FourBlock?useUnicode=true&characterEncoding=UTF-8";

        try (Connection conn = DriverManager.getConnection(url, "root", "0825")) {
            conn.setAutoCommit(false);

            int userId = CartUtil.getUserId(conn, username);

            // Pass null for optional fields: productName, productPrice, imageUrl, and link
            CartUtil.addOrUpdateCart(conn, userId, productId, null, null, quantity, null, null);

            conn.commit();
            response.sendRedirect("cart.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while adding to cart.");
        }
    }
}
