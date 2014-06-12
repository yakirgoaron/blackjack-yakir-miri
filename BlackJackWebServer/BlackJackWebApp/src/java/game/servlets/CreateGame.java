/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.servlets;

import BlackJack.Utils.SessionUtils;
import game.ws.client.BlackJackWebService;
import game.ws.client.DuplicateGameName_Exception;
import game.ws.client.InvalidParameters_Exception;
import game.ws.client.InvalidXML_Exception;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
/**
 *
 * @author Yakir
 */
@MultipartConfig
public class CreateGame extends HttpServlet {

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
        //response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) 
        {
            BlackJackWebService GameWS = SessionUtils.getBJWSClient(request);

            String Message = "SUCCESS";
            String Name  = "";
            String Tab = request.getParameter("tab").toString();
           
            if(Tab.equals("1"))
            {
                String GameName= request.getParameter("GameName");
                int HumanPlayers = Integer.parseInt(request.getParameter("HumanPlayers"));
                int CompPlayers = Integer.parseInt(request.getParameter("CompPlayersNumber"));

                try {
                    GameWS.createGame(GameName, HumanPlayers, CompPlayers);
                } catch (DuplicateGameName_Exception ex) {
                    Message = ex.getMessage();
                } catch (InvalidParameters_Exception ex) {
                    Message = ex.getMessage();
                }

                Name = GameName;
            }
            else
            {
                Part Xmlfile = request.getPart("XMLFile");
                Scanner s = new Scanner(Xmlfile.getInputStream()).useDelimiter("\\A");
                try 
                {
                    Name = GameWS.createGameFromXML(s.next());
                } catch (DuplicateGameName_Exception ex) {
                    Message = ex.getMessage();
                } catch (InvalidParameters_Exception ex) {
                    Message = ex.getMessage();
                } catch (InvalidXML_Exception ex) {
                    Message = ex.getMessage();
                }

            }
            request.getSession().setAttribute("GameName", Name);
            response.sendRedirect("GameList.html");
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
