/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.servlets;

import BlackJack.Utils.SessionUtils;
import JsonClasses.BetJson;
import JsonClasses.CardJson;
import com.google.gson.Gson;
import game.ws.client.BlackJackWebService;
import game.ws.client.GameDoesNotExists_Exception;
import game.ws.client.InvalidParameters_Exception;
import game.ws.client.Card;
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
public class PlayerDetails extends HttpServlet{

        class PlayerData{
        private String name;
        private String status;
        private String type;
        private List<BetJson> Bets;
        private double money;
        
        public List<CardJson> ConvertPlayerCards(List<Card> BetCards)
        {
            List<CardJson> TempBet = new ArrayList<>();
            for (Card card : BetCards) 
            {
                TempBet.add(CardJson.ConvertToLocal(card));
            }
            return TempBet;
        }
    
        private void InsertBets(game.ws.client.PlayerDetails plSource)
        {
            Bets = new ArrayList<>();
            Bets.add(new BetJson(ConvertPlayerCards(plSource.getFirstBet()), plSource.getFirstBetWage()));
            Bets.add(new BetJson(ConvertPlayerCards(plSource.getSecondBet()), plSource.getSecondBetWage()));   
        }

        public PlayerData(game.ws.client.PlayerDetails plSource)
        {
            name = plSource.getName();
            status = plSource.getStatus().toString();
            type = plSource.getType().toString();
            money = plSource.getMoney();
            InsertBets(plSource);

        }
    }
        
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
            String PlayerName = request.getParameter("PlayerName");
            if(PlayerName == null)
                PlayerName = (String)request.getSession().getAttribute("PlayerName");
            
            String GameName = request.getParameter("GameName");            
            if (GameName == null)
                GameName = (String)request.getSession().getAttribute("GameName");
            
            BlackJackWebService GameWS = SessionUtils.getBJWSClient(request);
            PlayerData Player = GetPlayerDetailsByName(GameWS, GameName, PlayerName);
          
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(Player);
            out.print(jsonResponse);
            out.flush();

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

    private PlayerData GetPlayerDetailsByName(BlackJackWebService GameWS, String GameName, String PlayerName) { 
        try
        {
            List<game.ws.client.PlayerDetails> Players =  GameWS.getPlayersDetails(GameName);

            for (game.ws.client.PlayerDetails playerDetails : Players) 
            {
                 if(playerDetails.getName().equals(PlayerName))
                     return new PlayerData(playerDetails);              
            }
            
        } 
        catch (GameDoesNotExists_Exception ex) {
            
        }
        return null; 
    }
}