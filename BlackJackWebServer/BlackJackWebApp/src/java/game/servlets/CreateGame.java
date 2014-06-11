/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.servlets;

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
            BlackJackWebService GameWS = (BlackJackWebService)request.getSession().getAttribute("GameWS");
            /*out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CreateGame</title>");            
            out.println("</head>");
            out.println("<body>");*/
            String Message = "SUCCESS";
            String Name  = "";
            request.getSession().getAttribute("HostName");
            Part Xmlfile = request.getPart("XMLFile");
            if(Xmlfile == null)
            {
                String GameName= request.getParameter("GameName");
                if(request.getParameter("HumanPlayers") == null || request.getParameter("CompPlayersNumber") == null )
                {
                    Message = "ERROR";
                }
                else
                {
                    int HumanPlayers = Integer.parseInt(request.getParameter("HumanPlayers"));
                    int CompPlayers = Integer.parseInt(request.getParameter("CompPlayersNumber"));
                    
                    try {
                        GameWS.createGame(GameName, HumanPlayers, CompPlayers);
                    } catch (DuplicateGameName_Exception ex) {
                        Message = ex.getMessage();
                    } catch (InvalidParameters_Exception ex) {
                        Message = ex.getMessage();
                    }
                }
                //out.println("<h1>Servlet CreateGame at " + Message + "</h1>");
                Name = GameName;
            }
            else
            {
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
                //out.println("<h1>Servlet CreateGame at " + s.next() + "</h1>");
                /* TODO output your page here. You may use following sample code. */

               

            }
            request.getSession().setAttribute("GameName", Name);
            //out.println("</body>");
            //out.println("</html>");
            response.sendRedirect("GamesStatus");
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
