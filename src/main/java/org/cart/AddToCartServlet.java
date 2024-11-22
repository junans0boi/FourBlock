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
import java.sql.SQLException;
import org.db.DatabaseManager;

@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        // 로그인 상태 확인
        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 요청에서 상품 정보 가져오기
        String productId = request.getParameter("productId");
        String productName = request.getParameter("productName");
        String productPrice = request.getParameter("productPrice");
        String purchaseUrl = request.getParameter("purchaseUrl");
        String productImageUrl = request.getParameter("productImageUrl");

        // 필수 정보 확인
        if (productId == null || productName == null || productPrice == null || 
            productId.isEmpty() || productName.isEmpty() || productPrice.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product details");
            return;
        }

        // 데이터베이스 작업
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            // 유저 ID 조회
            int userId = CartUtil.getUserId(conn, username);

            // 장바구니 추가 또는 업데이트
            CartUtil.addOrUpdateCart(conn, userId, productId, productName, productPrice, 1, productImageUrl, purchaseUrl);

            conn.commit();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "장바구니에 추가하는 동안 데이터베이스 오류가 발생했습니다.");
        }
    }
}
