/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.ServerClasses;

import game.client.ws.GameDetails;

/**
 *
 * @author Yakir
 */
public class GameInfo {
    public enum GameStatus {WAITING,ACTIVE,FINISHED};
    
    private int computerizedPlayers;
    private int humanPlayers;
    private int joinedHumanPlayers;
    private double money;
    private String name;
    private GameStatus status;
    private boolean LoadedFromXml;
    public GameInfo(GameDetails gamesrc)
    {
        this.computerizedPlayers = gamesrc.getComputerizedPlayers();
        this.humanPlayers = gamesrc.getHumanPlayers();
        this.joinedHumanPlayers = gamesrc.getJoinedHumanPlayers();
        this.money = gamesrc.getMoney();
        this.name = gamesrc.getName();
        this.status = GameStatus.valueOf(gamesrc.getStatus().value());
        LoadedFromXml = gamesrc.isLoadedFromXML();
    }

    public int getComputerizedPlayers() {
        return computerizedPlayers;
    }

    public int getHumanPlayers() {
        return humanPlayers;
    }

    public int getJoinedHumanPlayers() {
        return joinedHumanPlayers;
    }

    public double getMoney() {
        return money;
    }

    public String getName() {
        return name;
    }

    public GameStatus getStatus() {
        return status;
    }

    public boolean isLoadedFromXml() {
        return LoadedFromXml;
    }
    
    
    
    
}
