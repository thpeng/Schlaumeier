/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bd.schlau.meier;

import ch.bnd.game.gameplayercontrol.Game;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author caleb
 */
public class GameAnalyzerTest {
    
    private static final String DICTIONARY_GAME_MANAGER_PATH =  "GameManagerCombined.dic";
    private static final String DICTIONARY_WIN_EDT_PATH =  "CH_NEU.DIC";

    /**
     * Test of getBestCharForGivenInput method, of class GameAnalyzer.
     */
    @Test
    public void testGetBestCharForGivenInput() {
        GameAnalyzer instance = new GameAnalyzer(DICTIONARY_WIN_EDT_PATH, 1.0); 
        assertEquals("E", ""+instance.getBestCharForGivenInput("ABCE", instance.getCharOrder()));
        
    }
    @Test
    public void getUnderscoreRegexp()
    {
        GameAnalyzer instance = new GameAnalyzer(DICTIONARY_WIN_EDT_PATH, 1.0); 
        assertEquals("[ABCD]", instance.getUnderscoreRegexp("ABCD"));
    }
    @Test
    public void testGetRegexp()
    {
        GameAnalyzer instance = new GameAnalyzer(DICTIONARY_WIN_EDT_PATH, 1.0); 
        
        String patternchar = instance.getRegexp("AB_D", "[CE]"); 
        Pattern pattern = Pattern.compile(patternchar, Pattern.MULTILINE);
        assertTrue(pattern.matcher("A\nABCD").find());
    }
    
    @Test
    public void testGetRegexpNegative()
    {
        GameAnalyzer instance = new GameAnalyzer(DICTIONARY_WIN_EDT_PATH, 1.0); 
        Pattern pattern = Pattern.compile(instance.getRegexp("AB_D", "[CE]"));
        assertFalse(pattern.matcher("ABKD").matches());
    }
    
    
    @Test
    public void testgetPossibleMatches() throws Exception
    {
        GameAnalyzer instance = new GameAnalyzer(DICTIONARY_WIN_EDT_PATH, 1.0); 
        instance.startup();
       List<String> matches = instance.getPossibleMatches("ARBEITSBESCHAFFUN_SMASSNAHMEN", "G", 10);
       assertEquals(1, matches.size());
    }
    
    @Test
    public void testgetPossibleMatches2() throws Exception
    {
        GameAnalyzer instance = new GameAnalyzer(DICTIONARY_WIN_EDT_PATH, 1.0); 
        instance.startup();
       List<String> matches = instance.getPossibleMatches("ARBEITSBESCHAFFUN_SMASSNAHMEN", "GXYZ", 10);
       assertEquals(1, matches.size());
    }
    
    @Test
    public void testgetPossibleMatchesSeveral() throws Exception
    {
        GameAnalyzer instance = new GameAnalyzer(DICTIONARY_WIN_EDT_PATH, 1.0); 
        instance.startup();
       List<String> matches = instance.getPossibleMatches("LEICH______G", "FÜGÄNSEDRTIH", 10);
       assertEquals(5, matches.size());
    }
    
    @Test
    public void testgetPossibleMatchesTooMany() throws Exception
    {
        GameAnalyzer instance = new GameAnalyzer(DICTIONARY_WIN_EDT_PATH, 1.0); 
        instance.startup();
       List<String> matches = instance.getPossibleMatches("LEICH______G", "FÜGÄNSEDRTIH", 4);
       assertEquals(0, matches.size());
    }    

    
    @Test
    public void testgetPossibleMatchesALot() throws Exception
    {
       GameAnalyzer instance = new GameAnalyzer(DICTIONARY_GAME_MANAGER_PATH, 1.0); 
       instance.startup();
       List<String> matches = instance.getPossibleMatches("______________", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1000000);
       assertEquals(96557, matches.size());
    }
    
    @Test
    public void testgetPossibleMatchesNoMAtch() throws Exception
    {
       GameAnalyzer instance = new GameAnalyzer(DICTIONARY_GAME_MANAGER_PATH, 1.0); 
       instance.startup();
       List<String> matches = instance.getPossibleMatches("edjsdl", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 1000000);
       assertEquals(0, matches.size());
    }
    
    @Test
    public void testAnalyze() throws Exception
    {
        String result = "ARBEITSBESCHAFFUNGSMASSNAHMEN";
        GameAnalyzer instance = new GameAnalyzer(DICTIONARY_WIN_EDT_PATH, 1.0); 
        instance.startup();
        Game game = new Game();
        game.setCreditsReductionPerWrongWord(2);
        game.setYourCreditsLeft(8);
        game.setWordToGuess("ARBEITSBESCHAFFUN_SMASSNAHMEN");
        game.setAvailableCharacters("XYZGVW");
        assertEquals(result, instance.analyze(game));
    }
        
    @Test
    public void testAnalyzeProbabilityThreshold1() throws Exception
    {
        GameAnalyzer instance = new GameAnalyzer(DICTIONARY_WIN_EDT_PATH, 1.0); 
        instance.startup();
        Game game = new Game();
        game.setCreditsReductionPerWrongWord(2);
        game.setYourCreditsLeft(8);
        game.setWordToGuess("LEICH______G");
        game.setAvailableCharacters("ABGKLMOPQRFÜGÄNSEDRTIH");        
        String[] wrongWords = {"LEICHTFÜSSIG", "LEICHTHÄNDIG"};
        game.getWrongWords().addAll(Arrays.asList(wrongWords));
        assertEquals(12,instance.analyze(game).length());
        
        game.setYourCreditsLeft(6);
        assertEquals(12,instance.analyze(game).length());
        
        game.setYourCreditsLeft(5);
        assertEquals(12,instance.analyze(game).length());
        
        game.setYourCreditsLeft(4);
        assertEquals(1,instance.analyze(game).length());
        
        game.setYourCreditsLeft(1);
        assertEquals(1,instance.analyze(game).length());
    }
    
    @Test
    public void testAnalyzeProbabilityThreshold05() throws Exception
    {
        GameAnalyzer instance = new GameAnalyzer(DICTIONARY_WIN_EDT_PATH, 0.5); 
        instance.startup();
        Game game = new Game();
        game.setCreditsReductionPerWrongWord(2);
        game.setYourCreditsLeft(8);
        game.setWordToGuess("LEICH______G");
        game.setAvailableCharacters("ABGKLMOPQRFÜGÄNSEDRTIH");        
        String[] wrongWords = {"LEICHTFÜSSIG"};
        game.getWrongWords().addAll(Arrays.asList(wrongWords));
        assertEquals(12,instance.analyze(game).length());
        
        game.setYourCreditsLeft(3);
        assertEquals(12,instance.analyze(game).length());
        
        game.setYourCreditsLeft(2);
        assertEquals(1,instance.analyze(game).length());
        
        game.setYourCreditsLeft(1);
        assertEquals(1,instance.analyze(game).length());
    }    
}
