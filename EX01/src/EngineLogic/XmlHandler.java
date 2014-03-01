/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import EngineLogic.XmlClasses.Bet;
import EngineLogic.XmlClasses.Bets;
import EngineLogic.XmlClasses.Rank;
import EngineLogic.XmlClasses.Suit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yakir
 */
public class XmlHandler 
{

    private XmlHandler()
    {
    
    }
    
    
    public static Player CreatePlayer(EngineLogic.XmlClasses.Player XmlPlayer,
                                      GameEngine GameEng)
    {
        Player NewPlayer = null;
        
        String Name = XmlPlayer.getName();
        double Money = XmlPlayer.getMoney();
        ArrayList<Bid> Bids = CreatePlayerBids(XmlPlayer.getBets(), GameEng);
        
        switch (XmlPlayer.getType()){
            case HUMAN:
            {
               NewPlayer = new HumanPlayer(Name, Money, Bids);
               break;
            }
            case COMPUTER:
            {
               NewPlayer = new CompPlayer(Name, Money, Bids);
               break;
            }
            default:           
        }
                      
        return NewPlayer;
    }
    
    private static ArrayList<Bid> CreatePlayerBids(Bets bets, 
                                                   GameEngine GameEng) {
        
            ArrayList<Bid> Bids = new ArrayList<>();
            
            for(Bet bet: bets.getBet()){
                List<EngineLogic.XmlClasses.Cards.Card> XmlCards = 
                        bet.getCards().getCard();
                Double TotalBid = (double)bet.getSum();
                
                ArrayList<Card> PlayerCards = new ArrayList<>();
                                      
                for (EngineLogic.XmlClasses.Cards.Card card : XmlCards){
                  
                    Card.Rank rank = RankToRank(card.getRank());
                    Card.Suit suit = SuitToSuit(card.getSuit());
                    PlayerCards.add(GameEng.getCardAndRemove(rank, suit));                     
                } 
                
                Bids.add(new Bid(PlayerCards, TotalBid));
            }           
            return Bids;
    }
    
    private static Card.Rank RankToRank(Rank rank) {
       return Card.Rank.values()[rank.ordinal()];
    }
    
     private static Card.Suit SuitToSuit(Suit suit) {
        return Card.Suit.values()[suit.ordinal()];
    }
}
