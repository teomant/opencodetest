package org.teomant.servlets;

import org.teomant.util.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * сервлет страницы игры. Вызывается по кнопке на game.jsp, проверяет, заверщена ли игра. Если да - генерирует новое число,
 * очищает историю попыток. Если игра не заверщена - проверяет присланное число. После проверки - обновляет данные об игроке на сервере.
 */
@WebServlet("/Game")
public class GameServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        User user =(User) session.getAttribute("User");
        String number = request.getParameter("number");
        String result = user.getResult();
        if (result.contains(": RIGHT!</br>")){
            user.setResult("");
            Random rand = new Random();
            int  n = rand.nextInt(9999);
            String newtask = String.format("%04d",n);
            user.setTask(newtask);
            updateDB((Connection)request.getServletContext().getAttribute("DBConnection"),user);
            RequestDispatcher rd = request.getServletContext().getRequestDispatcher("/game.jsp");
            PrintWriter out= response.getWriter();
            out.println("<font color=Green>New game started!</font>");
            rd.forward(request, response);

        }
        String errorMsg = null;

        if (number.length()!=4){
            errorMsg ="Wrong size! Try again";
        }
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(number);
        if (!m.matches()){
            errorMsg ="You must use numbers!";
        }

        if(errorMsg != null){
            RequestDispatcher rd = request.getServletContext().getRequestDispatcher("/game.jsp");
            PrintWriter out= response.getWriter();
            out.println("<font color=red>"+errorMsg+"</font>");
            rd.include(request, response);
        }else{
            int cow=0;
            int bull=0;
            user.setAttempts(user.getAttempts()+1);
            String[]symbols=number.split("(?!^)");

            StringBuilder task=new StringBuilder(user.getTask());

            if (task.toString().equals(number)){
                user.setResult(user.getResult()+number+": RIGHT!</br>");
                user.setCorrect(user.getCorrect()+1);
                updateDB((Connection)request.getServletContext().getAttribute("DBConnection"),user);
                RequestDispatcher rd = request.getServletContext().getRequestDispatcher("/game.jsp");
                rd.include(request, response);
            }else{
                for (int i=0;i<number.length();i++)
                {
                    if (i==task.indexOf(symbols[i],i)) {
                        task.setCharAt(i, ' ');
                        bull++;
                    }else if (task.indexOf(symbols[i])>=0){
                        cow++;
                    }
                }
                user.setResult(user.getResult()+number+" - bulls:"+bull+" cows:"+cow+"<br/>");
                updateDB((Connection)request.getServletContext().getAttribute("DBConnection"),user);
                RequestDispatcher rd = request.getServletContext().getRequestDispatcher("/game.jsp");
                rd.include(request, response);
            }

        }
    }

    private void updateDB(Connection con, User user){
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("UPDATE public.\"Users\" " +
                    "SET username=?, password=?, task=?, result=?, attempts=?, correct=?, id=?" +
                    "WHERE id=?;");

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getTask());
            ps.setString(4, user.getResult());
            ps.setInt(5, user.getAttempts());
            ps.setInt(6, user.getCorrect());
            ps.setLong(7, user.getId());
            ps.setLong(8, user.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                ps.close();
            } catch (SQLException e) {
            }

        }
    }
}