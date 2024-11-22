package org.util;

import org.db.DatabaseManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;

@WebServlet("/clearDatabase")  // 이 URL로 접근하면 서블릿이 호출됩니다.
public class ClearDatabaseServlet extends javax.servlet.http.HttpServlet {

    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws ServletException, IOException {
        // GET 요청은 지원하지 않는다는 메시지를 출력합니다.
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<h3>HTTP GET 요청은 지원되지 않습니다. POST 요청을 사용하세요.</h3>");
    }

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws ServletException, IOException {
        // 응답 인코딩 설정
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try (Connection conn = DatabaseManager.getConnection()) { // DatabaseManager를 통해 연결
            Statement stmt = conn.createStatement();
            String sql = "TRUNCATE TABLE search_results";
            stmt.executeUpdate(sql);
            out.println("<h3>테이블 데이터 초기화 완료</h3>");
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>데이터베이스 초기화 실패</h3>");
        }
    }
}