/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Controllers;

import blackjackfx.ServerClasses.Events;
import blackjackfx.ServerClasses.GameInfo;
import blackjackfx.ServerClasses.PlayerInfo;
import blackjackfx.ServerClasses.PlayerStatus;
import blackjackfx.ServerClasses.PlayerType;
import game.client.ws.GameDoesNotExists_Exception;
import game.client.ws.InvalidParameters_Exception;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

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
    private Timer UpdateTable;
    private ObservableList<GameDetailsRow> GamesData;
    private SimpleBooleanProperty FinishJoinGame;
    @FXML
    private TableView<PlayerDetailsRow> PlayersJoined;
    private ObservableList<PlayerDetailsRow> PlayerData;
    private GameInfo chosen;
    @FXML
    private TextField txtPlayerName;
    @FXML
    private Button btnJoin;
    @FXML
    private Button BackToWait;
    @FXML
    private ChoiceBox<String> ChosePlayer;

    
     /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        GamesData =  FXCollections.observableArrayList();
        PlayerData = FXCollections.observableArrayList();
        FinishJoinGame = new SimpleBooleanProperty();
        InitTableGames();    
        WaitinGamesView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GameDetailsRow>()
        {

            @Override
            public void changed(ObservableValue<? extends GameDetailsRow> ov, GameDetailsRow t, GameDetailsRow t1) {
                if(t1 != null)
                {
                    GameWS.setGameName(t1.getGameName());
                    chosen = t1.getGameChosen();
                    GameChosen();
                }
                
            }
            
        });
    }  
   
    public SimpleBooleanProperty getFinishJoinGame() {
        return FinishJoinGame;
    }
    
    public void setGameWS(Events GameWS) {
        this.GameWS = GameWS;
        CreateTimer();
    }

    private void GameChosen()
    {
        this.WaitinGamesView.setVisible(false);
        InITablePlayer();
        this.PlayersJoined.setVisible(true);
        if(!chosen.isLoadedFromXml())
            txtPlayerName.setDisable(false);
        else
        {
            ChosePlayer.setVisible(true);
            
        }
        btnJoin.setDisable(false);
        BackToWait.setDisable(false);
        UpdatePlayersTable();
    }
    
    private void UpdatePlayersTable()
    {
        ChosePlayer.getItems().clear();
        List<PlayerInfo> Players  = GameWS.GetPlayersInGame(); 
        PlayerData.clear();
        for (PlayerInfo playerDetails : Players)
        {
            PlayerData.add(new PlayerDetailsRow(playerDetails.getName(), playerDetails.getType().toString(),Double.toString(playerDetails.getMoney())));
            if(playerDetails.getStatus().equals(PlayerStatus.ACTIVE) && !playerDetails.getType().equals(PlayerType.COMPUTER))
                ChosePlayer.getItems().add(playerDetails.getName());
        }
    }
    
    private void InitTableGames()
    {
        WaitinGamesView.getColumns().clear();
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
 
        WaitinGamesView.setItems(GamesData);
        WaitinGamesView.getColumns().addAll(GameNameCol, AmountPlayerCol, PlayerJoinedGame);
    }
    
    private void InITablePlayer()
    {
        PlayersJoined.getColumns().clear();
        TableColumn PlayerNameCol = new TableColumn("Player Name");
        PlayerNameCol.setMinWidth(100);
        PlayerNameCol.setCellValueFactory(
                new PropertyValueFactory<GameDetailsRow, String>("PlayerName"));
 
        TableColumn PlayerTypeCol = new TableColumn("Player Type");
        PlayerTypeCol.setMinWidth(200);
        PlayerTypeCol.setCellValueFactory(
                new PropertyValueFactory<GameDetailsRow, String>("PlayerType"));
 
        TableColumn PlayerMoneyCol = new TableColumn("Player Money");
        PlayerMoneyCol.setMinWidth(200);
        PlayerMoneyCol.setCellValueFactory(
                new PropertyValueFactory<GameDetailsRow, String>("PlayerMoney"));
 
        PlayersJoined.setItems(PlayerData);
        PlayersJoined.getColumns().addAll(PlayerNameCol, PlayerTypeCol, PlayerMoneyCol);
    }
    
    private void CreateTimer()
    {
        UpdateTable = new Timer(true);
        TimerTask taskUpdate = new TimerTask() {

            @Override
            public void run() {
                List<String> WaitingGames = GameWS.GetWaitingGames();
                GamesData.clear();
                for (String currGame : WaitingGames) 
                {
                    try
                    {
                        GameInfo curr = GameWS.GetGameDetails(currGame);
                        GameDetailsRow row = new GameDetailsRow(curr.getName(),Integer.toString(curr.getHumanPlayers()) ,
                                                        Integer.toString(curr.getJoinedHumanPlayers()),curr);
                        GamesData.add(row);
                
                
                    }
                    catch (GameDoesNotExists_Exception ex) 
                    {
                
                    }
                }
            }
        };
        
        UpdateTable.schedule(taskUpdate,100, 1000);
    }

    @FXML
    private void JoinToTheGame(ActionEvent event)
    {
        
        try {
            if(chosen.isLoadedFromXml())
                GameWS.JoinGame(ChosePlayer.getValue());
            else               
                GameWS.JoinGame(txtPlayerName.getText());
            FinishJoinGame.set(true);
            
        } catch (GameDoesNotExists_Exception ex) {
            Logger.getLogger(WaitingGamesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidParameters_Exception ex) {
            Logger.getLogger(WaitingGamesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO CHANGE MONEY 
        PlayerData.add(new PlayerDetailsRow(txtPlayerName.getText(), PlayerType.HUMAN.toString(), "1000"));
    }

    @FXML
    private void GoToWaiting(ActionEvent event) {
        this.WaitinGamesView.setVisible(true);
        InitTableGames();
        this.PlayersJoined.setVisible(false);
        txtPlayerName.setDisable(true);
        btnJoin.setDisable(true);
        BackToWait.setDisable(true);
    }
    
    
     
    public class GameDetailsRow
    {
        private SimpleStringProperty GameName;
        private SimpleStringProperty AmountPlayersTotal;
        private SimpleStringProperty AmountPlayersJoin;
        private GameInfo GameChosen;
        public GameDetailsRow(String GameName,String PlayersAmount,String AmountPlayerJoin,GameInfo GameChosen)
        {
            this.GameName = new SimpleStringProperty(GameName);
            this.AmountPlayersTotal = new SimpleStringProperty(PlayersAmount); 
            this.AmountPlayersJoin = new SimpleStringProperty(AmountPlayerJoin);
            this.GameChosen = GameChosen;
        }

        public GameInfo getGameChosen() {
            return GameChosen;
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

    public class PlayerDetailsRow
    {
        private SimpleStringProperty PlayerName;
        private SimpleStringProperty PlayerType;
        private SimpleStringProperty PlayerMoney;
        
        public PlayerDetailsRow(String PlayerName,String PlayerType,String PlayerMoney)
        {
            this.PlayerName = new SimpleStringProperty(PlayerName);
            this.PlayerType = new SimpleStringProperty(PlayerType); 
            this.PlayerMoney = new SimpleStringProperty(PlayerMoney);
        }
        
        

        public void setPlayerName(SimpleStringProperty PlayerName) {
            this.PlayerName = PlayerName;
        }
        
        public void setPlayerMoney(SimpleStringProperty PlayerMoney) {
            this.PlayerMoney = PlayerMoney;
        }
        
        public void setPlayerType(SimpleStringProperty PlayerType) {
            this.PlayerType = PlayerType;
        }
        
        public String getPlayerType() {
            return PlayerType.get();
        }
        public String getPlayerName() {
            return PlayerName.get();
        }
        public String getPlayerMoney() {
            return PlayerMoney.get();
        }

        
    }


}
