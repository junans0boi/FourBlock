// 컴퓨터시스템공학과 2-B 202345062 이관용
// 컴퓨터시스템공학과 2-B 202045066 이준환
package org.naverApi;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/popularSearch")
public class PopularSearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> top10Keywords = DataLabAPI.getTop10Keywords();
        request.setAttribute("top10Keywords", top10Keywords);
        request.getRequestDispatcher("search_page.jsp").forward(request, response);
    }
}

