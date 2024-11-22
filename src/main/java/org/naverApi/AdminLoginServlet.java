// 컴퓨터시스템공학과 2-B 202145058 강유빈
package org.naverApi;

import org.db.DatabaseManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminLoginServlet {

    // POST 요청을 처리하는 메서드
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (isValidUser(username, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username); // 사용자 이름 설정
            session.setAttribute("user_id", getUserIdByUsername(username)); // 데이터베이스에서 user_id 가져오기
            response.sendRedirect("admin_search.jsp");
        } else {
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    // 사용자 이름과 비밀번호가 유효한지 확인하는 메서드
    private boolean isValidUser(String username, String password) {
        boolean isValid = false;

        String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        isValid = true;
                    }
                }
                
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    // 사용자 이름으로 user_id를 가져오는 메서드
    private int getUserIdByUsername(String username) {
        int userId = -1;

        String query = "SELECT user_id FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("user_id");
                }
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }
}
