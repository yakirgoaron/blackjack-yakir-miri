/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

/**
 *
 * @author yakir
 */
public class XmlHandler 
{
    private XmlHandler()
    {
    
    }
    
    public static CompPlayer LoadComputer(EngineLogic.XmlClasses.Player XmlPlayer)
    {
        
        return new CompPlayer();
    }
    
    
    public static HumanPlayer LoadHuman(EngineLogic.XmlClasses.Player XmlPlayer)
    {
        String Name = XmlPlayer.getName();
        HumanPlayer NewPlayer = new HumanPlayer(Name);
        
        return NewPlayer;
    }
}
