/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.ServerClasses;

import game.client.ws.PlayerDetails;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yakir
 */
public class PlayerInfo 
{
    private String name;
    private PlayerStatus status;
    private PlayerType type;
    private List<PlayerBet> Bets;
    private double money;
    
    private List<Card> ConvertPlayerCards(List<game.client.ws.Card> BetCards)
    {
        List<Card> TempBet = new ArrayList<>();
        for (game.client.ws.Card card : BetCards) 
        {
            TempBet.add(Card.ConvertToLocal(card));
        }
        return TempBet;
    }
    
    private void InsertBets(PlayerDetails plSource)
    {
        Bets = new ArrayList<>();
        Bets.add(new PlayerBet(ConvertPlayerCards(plSource.getFirstBet()), plSource.getFirstBetWage()));
        Bets.add(new PlayerBet(ConvertPlayerCards(plSource.getSecondBet()), plSource.getSecondBetWage()));   
    }
    
    public PlayerInfo(PlayerDetails plSource)
    {
        name = plSource.getName();
        status = PlayerStatus.valueOf(plSource.getStatus().value());
        type = PlayerType.valueOf(plSource.getType().value());
        // ADD MONEY
        //money = plSource.
        InsertBets(plSource);
        
    }

    public String getName() {
        return name;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public PlayerType getType() {
        return type;
    }

    public List<PlayerBet> getBets() {
        return Bets;
    }

    public double getMoney() {
        return money;
    }
    
    
    
}
