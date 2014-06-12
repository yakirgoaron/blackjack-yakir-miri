/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BlackJack.Utils;

import game.ws.client.BlackJackWebService;
import game.ws.client.BlackJackWebService_Service;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Yakir
 */
public class SessionUtils 
{
    public static BlackJackWebService getBJWSClient (HttpServletRequest request) {
        URL url = (URL)request.getSession().getAttribute("GameWS");
        BlackJackWebService_Service WSForConnect = new BlackJackWebService_Service(url);
        return WSForConnect.getBlackJackWebServicePort();
    }
    
}
