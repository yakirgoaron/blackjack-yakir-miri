/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Controllers;

import blackjackfx.Events;
import game.client.ws.GameDetails;
import game.client.ws.GameDoesNotExists_Exception;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Yakir
 */
public class WaitingGamesController implements Initializable 
{
    private Events GameWS;
    @FXML
    private TableView<GameDetailsRow> WaitinGamesView;
    private final ObservableList<GameDetailsRow> data =
        FXCollections.observableArrayList(
            new GameDetailsRow("1", "5", "6"),
            new GameDetailsRow("2", "4", "3"),
            new GameDetailsRow("Ethan", "Williams", "ethan.williams@example.com"),
            new GameDetailsRow("Emma", "Jones", "emma.jones@example.com"),
            new GameDetailsRow("Michael", "Brown", "michael.brown@example.com")
        );
    public void setGameWS(Events GameWS) {
        this.GameWS = GameWS;
    }
    /**
     * Initializes the controller class.
     */
    
    public class GameDetailsRow
    {
        private SimpleStringProperty GameName;
        private SimpleStringProperty AmountPlayersTotal;
        private SimpleStringProperty AmountPlayersJoin;

        public GameDetailsRow(String GameName,String PlayersAmount,String AmountPlayerJoin)
        {
            this.GameName = new SimpleStringProperty(GameName);
            this.AmountPlayersTotal = new SimpleStringProperty(PlayersAmount); 
            this.AmountPlayersJoin = new SimpleStringProperty(AmountPlayerJoin);
        }
        
       

        public void setGameName(SimpleStringProperty GameName) {
            this.GameName = GameName;
        }
        
        public void setAmountPlayersTotal(SimpleStringProperty AmountPlayersTotal) {
            this.AmountPlayersTotal = AmountPlayersTotal;
        }
        public void setAmountPlayersJoin(SimpleStringProperty AmountPlayersJoin) {
            this.AmountPlayersJoin = AmountPlayersJoin;
        }
        
        public String getAmountPlayersTotal() {
            return AmountPlayersTotal.get();
        }

        public String getGameName() {
            return GameName.get();
        }

        public String getAmountPlayersJoin() {
            return AmountPlayersJoin.get();
        }

        
        
        
    }
    
    private void InITable()
    {
        TableColumn GameNameCol = new TableColumn("Game Name");
        GameNameCol.setMinWidth(100);
        GameNameCol.setCellValueFactory(
                new PropertyValueFactory<GameDetailsRow, String>("GameName"));
 
        TableColumn AmountPlayerCol = new TableColumn("Amount Of Players");
        AmountPlayerCol.setMinWidth(200);
        AmountPlayerCol.setCellValueFactory(
                new PropertyValueFactory<GameDetailsRow, String>("AmountPlayersTotal"));
 
        TableColumn PlayerJoinedGame = new TableColumn("Players Joined");
        PlayerJoinedGame.setMinWidth(200);
        PlayerJoinedGame.setCellValueFactory(
                new PropertyValueFactory<GameDetailsRow, String>("AmountPlayersJoin"));
 
        WaitinGamesView.setItems(data);
        WaitinGamesView.getColumns().addAll(GameNameCol, AmountPlayerCol, PlayerJoinedGame);
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        //List<String> WaitingGames = GameWS.GetWaitingGames();
        InITable();
        /*for (String currGame : WaitingGames) 
        {
            try
            {
                GameDetails curr = GameWS.GetGameDetails(currGame);
                GameDetailsRow row = new GameDetailsRow(curr.getName(),Integer.toString(curr.getHumanPlayers()) ,
                                                        Integer.toString(curr.getJoinedHumanPlayers()));
                data.add(row);
                
                
            }
            catch (GameDoesNotExists_Exception ex) 
            {
                
            }
            
        }*/
    }    
    
}
