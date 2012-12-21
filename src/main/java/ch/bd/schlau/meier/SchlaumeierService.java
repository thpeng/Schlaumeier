/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bd.schlau.meier;

import javax.inject.Inject;
import javax.jws.WebService;

/**
 *
 * @author caleb
 */
@WebService(serviceName = "GamePlayerControl", portName = "GamePlayerControlPort", endpointInterface = "ch.bnd.game.gameplayercontrol.GamePlayerControl", targetNamespace = "http://gameplayercontrol.game.bnd.ch/", wsdlLocation = "WEB-INF/wsdl/192.168.36.191_8080/GamePlayer/GamePlayerControl.wsdl")
public class SchlaumeierService {

    @Inject
    GameAnalyzer gameAnalyzer;
    
    public void startNewGame(ch.bnd.game.gameplayercontrol.Game game) {   
    }

    public void finishGame(ch.bnd.game.gameplayercontrol.Game game) {
    }

    public void receiveFeedbackForLastGuess(ch.bnd.game.gameplayercontrol.Game game, int guessId) {
    }

    public java.lang.String giveNextGuess(ch.bnd.game.gameplayercontrol.Game game, int guessId) {
        return gameAnalyzer.analyze(game);
    }
    
}
