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
import java.sql.PreparedStatement;

@WebServlet("/deleteFromCart")
public class DeleteFromCartServlet extends HttpServlet {

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

        String url = "jdbc:mysql://fourblock.kro.kr:3306/FourBlock?useUnicode=true&characterEncoding=UTF-8";

        try (Connection conn = DriverManager.getConnection(url, "root", "0825")) {
            conn.setAutoCommit(false);

            int userId = CartUtil.getUserId(conn, username); // Retrieve the user ID

            String deleteQuery = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.setInt(1, userId);
                stmt.setString(2, productId);
                stmt.executeUpdate();
            }

            conn.commit();
            response.setStatus(HttpServletResponse.SC_OK); // Indicate success
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete item from cart.");
        }
    }
}
