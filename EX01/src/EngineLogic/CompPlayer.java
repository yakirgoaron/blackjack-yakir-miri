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
public class CompPlayer extends Player
{
    static int CompIdGen=1;
    public CompPlayer()
    {
        Name = "Comp"+CompIdGen;
        CompIdGen++;
    }
}
