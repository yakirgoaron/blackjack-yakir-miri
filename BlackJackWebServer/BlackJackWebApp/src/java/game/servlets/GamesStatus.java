/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.servlets;

import game.ws.client.BlackJackWebService;
import game.ws.client.GameDetails;
import game.ws.client.GameDoesNotExists_Exception;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Yakir
 */
public class GamesStatus extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()){
            BlackJackWebService GameWS = (BlackJackWebService)request.getSession().getAttribute("GameWS");
            List<String> WaitingGames = GameWS.getWaitingGames();
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GamesStatus</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<table border=\"1\" style=\"width:300px\"><tr><td></td><td>Name</td><td>Players</td><td>Joined</td></tr>");
            for (String string : WaitingGames) 
            {
                try 
                {
                    GameDetails gm = GameWS.getGameDetails(string);
                    out.println("<tr>");
                    out.println("<td><input type=\"radio\" name=\"game\" value=\"string\"></td>");
                    out.println("<td>"+gm.getName()+"</td>");
                    out.println("<td>"+gm.getHumanPlayers()+"</td>");
                    out.println("<td>"+gm.getJoinedHumanPlayers()+"</td>");
                    out.println("</tr>");
                    
                }
                catch (GameDoesNotExists_Exception ex) 
                {
                    Logger.getLogger(GamesStatus.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            out.println("</table>");
            out.println("<input type=\"submit\" name=\"submit\" value=\"Submit\">");
            out.println("<h1>Servlet GamesStatus at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
