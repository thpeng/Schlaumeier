/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bd.schlau.meier;

import ch.bnd.game.gameplayercontrol.Game;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author caleb
 */
public class GameAnalyzerTest {
    



    /**
     * Test of getBestCharForGivenInput method, of class GameAnalyzer.
     */
    @Test
    public void testGetBestCharForGivenInput() {
        GameAnalyzer instance = new GameAnalyzer(); 
        assertEquals("E", ""+instance.getBestCharForGivenInput("ABCE", instance.getCharOrder()));
        
    }
    @Test
    public void getUnderscoreRegexp()
    {
        GameAnalyzer instance = new GameAnalyzer(); 
        assertEquals("[a|b|c|d]", instance.getUnderscoreRegexp("abcd"));
    }
    @Test
    public void testGetRegexp()
    {
        GameAnalyzer instance = new GameAnalyzer(); 
        
        String patternchar = instance.getRegexp("ab_d", "(c|e)"); 
        Pattern pattern = Pattern.compile(patternchar, Pattern.MULTILINE);
        assertTrue(pattern.matcher("a\nabcd").find());
    }
    @Test
    public void testGetRegexpNegative()
    {
        GameAnalyzer instance = new GameAnalyzer(); 
        Pattern pattern = Pattern.compile(instance.getRegexp("ab_d", "(c|e)"));
        assertFalse(pattern.matcher("abkd").matches());
    }
    
    
    @Test
    public void testgetPossibleMatches() throws Exception
    {
        GameAnalyzer instance = new GameAnalyzer(); 
        instance.startup();
       List<String> matches = instance.getPossibleMatches("Arbeitsbeschaffun_smassnahmen", "g");
       assertEquals(1, matches.size());
        
    }
    @Test
    public void testgetPossibleMatches2() throws Exception
    {
        GameAnalyzer instance = new GameAnalyzer(); 
        instance.startup();
       List<String> matches = instance.getPossibleMatches("Arbeitsbeschaffun_smassnahmen", "gxyz");
       assertEquals(1, matches.size());
        
    }
    @Test
    public void testAnalyze() throws Exception
    {
        String result = "Arbeitsbeschaffungsmassnahmen";
        GameAnalyzer instance = new GameAnalyzer(); 
        instance.startup();
        Game game = new Game(); 
        game.setWordToGuess("Arbeitsbeschaffun_smassnahmen");
        game.setAvailableCharacters("gxyzvw");
        assertEquals(result, instance.analyze(game));
    }
}
