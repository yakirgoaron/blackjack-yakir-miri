/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import EngineLogic.Exception.DuplicateCardException;
import EngineLogic.XmlClasses.Bet;
import EngineLogic.XmlClasses.Bets;
import EngineLogic.XmlClasses.Cards;
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
    
    public static Dealer CreateDealer(Bet XmlDealer,
                                      GameEngine GameEng) 
                         throws DuplicateCardException
    {
        Dealer dealer;
        List<EngineLogic.XmlClasses.Cards.Card> XmlCards = 
                XmlDealer.getCards().getCard();
        ArrayList<Card> PlayerCards = CreateHandCards(XmlCards, GameEng);       
        dealer = new Dealer(PlayerCards);         
        return dealer;
    }
    
    public static Bet SaveDealer(Dealer GameDealer){
        
        Bet XmlDealer = new Bet();
        SaveBetCards(XmlDealer, GameDealer.getDealerCards());
        XmlDealer.setSum(0);
        return XmlDealer;
    }
    
    
    public static Player CreatePlayer(EngineLogic.XmlClasses.Player XmlPlayer,
                                      GameEngine GameEng) 
                                      throws DuplicateCardException
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
    
    public static EngineLogic.XmlClasses.Player SavePlayer(Player GamePlayer){
        
        EngineLogic.XmlClasses.Player XmlPlayer = 
                new EngineLogic.XmlClasses.Player();
        XmlPlayer.setName(GamePlayer.getName());
       // XmlPlayer.setMoney((Float)GamePlayer.getMoney());       
        return null;
    }
    
    private static ArrayList<Bid> CreatePlayerBids(Bets bets, 
                                                   GameEngine GameEng) throws 
                                                   DuplicateCardException {
        
            ArrayList<Bid> Bids = new ArrayList<>();
            
            for(Bet bet: bets.getBet()){
                
                Double TotalBid = (double)bet.getSum();
                List<EngineLogic.XmlClasses.Cards.Card> XmlCards = 
                        bet.getCards().getCard();
     
                ArrayList<Card> PlayerCards = 
                        CreateHandCards(XmlCards, GameEng);
                
                Bids.add(new Bid(PlayerCards, TotalBid));
            }           
            return Bids;
    }
    
    private static ArrayList<Card> CreateHandCards(
            List<EngineLogic.XmlClasses.Cards.Card> XmlCards,
            GameEngine GameEng) throws DuplicateCardException{
        
        ArrayList<Card> PlayerCards = new ArrayList<>();
                                      
        for (EngineLogic.XmlClasses.Cards.Card card : XmlCards){

            Card.Rank rank = XmlToGameRank(card.getRank());
            Card.Suit suit = XmlToGameSuit(card.getSuit());
            Card cardToAdd = GameEng.getCardAndRemove(rank, suit);                     

            if (cardToAdd == null)
                throw new DuplicateCardException();
            PlayerCards.add(cardToAdd);
        }
        
        return PlayerCards;
    }
    
    public static void SaveBetCards(Bet XmlBet, Hand PlayerHand){
       
        ArrayList<Card> HandCards = PlayerHand.getCards();
        EngineLogic.XmlClasses.Cards XmlCards = new Cards();
        
        for (Card card:HandCards){
            
            Rank rank = GameToXmlRank(card.getRank());
            Suit suit = GameToXmlSuit(card.getSuit());
            EngineLogic.XmlClasses.Cards.Card cardToAdd = 
                    new EngineLogic.XmlClasses.Cards.Card();
            cardToAdd.setRank(rank);
            cardToAdd.setSuit(suit);
            XmlCards.getCard().add(cardToAdd);           
        }
        
        XmlBet.setCards(XmlCards);
        
    }
    
    private static Card.Rank XmlToGameRank(Rank rank) {
       return Card.Rank.values()[rank.ordinal()];
    }
    
     private static Card.Suit XmlToGameSuit(Suit suit) {
        return Card.Suit.values()[suit.ordinal()];
    }
     
         
    private static Rank GameToXmlRank(Card.Rank rank) {
       return Rank.values()[rank.ordinal()];
    }
    
    private static Suit GameToXmlSuit(Card.Suit suit) {
        return Suit.values()[suit.ordinal()];
    }
}
