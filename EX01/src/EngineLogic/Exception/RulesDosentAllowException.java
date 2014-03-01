/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic.Exception;

/**
 *
 * @author yakir
 */
public class RulesDosentAllowException extends Exception{
    
    private final String Message;
    public RulesDosentAllowException(String Message)
    {
        this.Message = Message;
    }
    @Override
    public String getMessage()
    {
        return Message;
    }
}
