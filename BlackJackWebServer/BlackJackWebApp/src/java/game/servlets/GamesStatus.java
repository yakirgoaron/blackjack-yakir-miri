/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.servlets;

import BlackJack.Utils.SessionUtils;
import com.google.gson.Gson;
import game.ws.client.BlackJackWebService;
import game.ws.client.GameDetails;
import game.ws.client.GameDoesNotExists_Exception;
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
        response.setContentType("application/json;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()){
            BlackJackWebService GameWS = SessionUtils.getBJWSClient(request);
            List<String> WaitingGames = GameWS.getWaitingGames();
            List<GameDetail> GamesForJoin = new ArrayList<>();
            for (String string : WaitingGames) 
            {
                try 
                {
                    GameDetails gm = GameWS.getGameDetails(string);
                    GamesForJoin.add(new GameDetail(gm.getName(),gm.getHumanPlayers(),gm.getJoinedHumanPlayers()));
                    
                }
                catch (GameDoesNotExists_Exception ex) 
                {
                    Logger.getLogger(GamesStatus.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(GamesForJoin);
            out.print(jsonResponse);
            out.flush();
        }
    }
    class GameDetail {

        final private String Name;
        final private int HumanPlayers;
        final private int JoinedPlayers;
        
        public GameDetail(String Name, int HumanPlayers,int JoinedPlayers) {
            this.Name = Name;
            this.HumanPlayers = HumanPlayers;
            this.JoinedPlayers = JoinedPlayers;
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
