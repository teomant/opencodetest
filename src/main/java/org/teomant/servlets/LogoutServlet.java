package org.teomant.servlets;

import org.teomant.util.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Закрываем сессию
 */

@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        //invalidate the session if exists
        HttpSession session = request.getSession(false);

        if(session != null){
            session.invalidate();
        }

        request.getRequestDispatcher("/index.jsp").forward(request,response);
    }
}
