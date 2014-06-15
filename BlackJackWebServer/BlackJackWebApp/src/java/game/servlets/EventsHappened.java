/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.servlets;

import BlackJack.Utils.SessionUtils;
import JsonClasses.ActionJson;
import JsonClasses.CardJson;
import JsonClasses.EventTypeJson;
import JsonClasses.RankJson;
import JsonClasses.SuitJson;
import com.google.gson.Gson;
import game.ws.client.Action;
import game.ws.client.BlackJackWebService;
import game.ws.client.Card;
import game.ws.client.Event;
import game.ws.client.EventType;
import game.ws.client.InvalidParameters_Exception;
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
public class EventsHappened extends HttpServlet {

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
            BlackJackWebService GameWS = SessionUtils.getBJWSClient(request);
            Integer eventdId = 0;
            if(request.getSession().getAttribute("EventID") != null)
               eventdId = (Integer)request.getSession().getAttribute("EventID") ;
            Integer PlayerId =  (Integer)request.getSession().getAttribute("PlayerID");
            List<Event> EventsHappened = GameWS.getEvents(PlayerId, eventdId);
            request.getSession().setAttribute("EventID", EventsHappened.size() + eventdId);
            List<EventJson> ArrayOfEvents = new ArrayList<>();
            for (Event event : EventsHappened) 
            {
                ArrayOfEvents.add(new EventJson(event));
            }
            
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(ArrayOfEvents);
            out.print(jsonResponse);
            out.flush();
        } catch (InvalidParameters_Exception ex) {
            Logger.getLogger(EventsHappened.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
  
    
    class EventJson
    {
        private List<CardJson> cards;
        final private int id;
        final private float money;
        final private ActionJson playerAction;
        final private String playerName;
        final private int timeout;
        final private EventTypeJson type;
        
        public EventJson(Event event)
        {
            cards = new ArrayList<>();
            this.id = event.getId();
            this.money = event.getMoney();
            this.playerName = event.getPlayerName();
            this.timeout = event.getTimeout();
            this.playerAction =ActionJson.valueOf(event.getPlayerAction().value());
            this.type = EventTypeJson.valueOf(event.getType().value());
            InsertCards(event);
            
        }
        private void InsertCards(Event event)
        {
            for (Card cardJson : event.getCards()) 
            {
                RankJson rank = RankJson.valueOf(cardJson.getRank().value());
                SuitJson suit = SuitJson.valueOf(cardJson.getSuit().value());
                cards.add(new CardJson(rank, suit));
            }
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
