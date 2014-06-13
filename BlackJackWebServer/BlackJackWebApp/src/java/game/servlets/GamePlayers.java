/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.servlets;

import BlackJack.Utils.SessionUtils;
import com.google.gson.Gson;
import game.ws.client.BlackJackWebService;
import game.ws.client.GameDoesNotExists_Exception;
import game.ws.client.PlayerDetails;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
public class GamePlayers extends HttpServlet {

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
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String GameName = request.getParameter("GameName").toString();
            BlackJackWebService GameWS = SessionUtils.getBJWSClient(request);
            List<PlayerInfo> Players = new ArrayList<>();
            List<PlayerDetails> GamePlayers = null;
            try {
                GamePlayers = GameWS.getPlayersDetails(GameName);
            } catch (GameDoesNotExists_Exception ex) {
                Logger.getLogger(GamePlayers.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (PlayerDetails playerDetails : GamePlayers) 
            {
                 Players.add(new PlayerInfo(playerDetails.getName(), playerDetails.getStatus().name(),playerDetails.getType().name()));
            }
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(Players);
            out.print(jsonResponse);
            out.flush();
        } 
    }

    class PlayerInfo {

        final private String Name;
        final private String Status;
        final private String Type;
        public PlayerInfo(String Name,String Status,String Type) {
            this.Name = Name;
            this.Status = Status;
            this.Type = Type;
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
