/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EngineLogic;

/**
 *
 * @author miri
 */
public interface AIPlayer extends GameParticipant{
    public Communicable.PlayerAction Play(Hand hand);
    
}
