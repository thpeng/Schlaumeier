/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bd.schlau.meier;

import ch.bnd.game.gameplayercontrol.Game;
import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author caleb
 */
@ApplicationScoped
public class GameAnalyzer implements Serializable {

    private String dictionaryPath;
    private double probabilityThreshold;
    
    private String searchFilestr;
    Locale swissGerman = new Locale("de", "CH");

    public GameAnalyzer() {
        this("/GameManagerCombined.dic", 0.5);
    }

    public GameAnalyzer(String dictionaryPath, double probabilityThreshold) {
        this.dictionaryPath = dictionaryPath;
        this.probabilityThreshold = probabilityThreshold;
    }

    @PostConstruct
    public void startup() throws IOException {
        InputSupplier<? extends InputStream> supplier = new InputSupplier<InputStream>() {
            @Override
            public InputStream getInput() throws IOException {
                return getClass().getClassLoader().getResourceAsStream(dictionaryPath);
            }
        };
        searchFilestr = CharStreams.toString(CharStreams.newReaderSupplier(supplier, Charset.defaultCharset()));
    }

    protected List<String> getPossibleMatches(String word, String availableChars, int maxMatches) {
        String regExp = getRegexp(word, getUnderscoreRegexp(availableChars));
        Pattern pattern = Pattern.compile(regExp, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(searchFilestr);
        List<String> matches = new ArrayList<String>();
        while (matcher.find()) {
            matches.add(matcher.group().toUpperCase(swissGerman));
            if (matches.size() > maxMatches) {
                return Collections.emptyList();
            }
        }
        return matches;
    }

    protected String getRegexp(String word, String underscoreRegexp) {
        return "^" + word.replace("_", underscoreRegexp) + "$";
    }

    protected String getUnderscoreRegexp(String availableChars) {
        Preconditions.checkNotNull(availableChars);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(availableChars);
        sb.append("]");

        return sb.toString();
    }

    public String analyze(Game game) {
        Preconditions.checkNotNull(game);
        Preconditions.checkNotNull(searchFilestr, "startup method was not called!");
        Preconditions.checkArgument(game.getYourCreditsLeft() > 0);
        Preconditions.checkArgument(game.getCreditsReductionPerWrongWord() > 0);
        if (game.getWordToGuess().contains("_")) {
            List<String> candidates = getPossibleMatches(game.getWordToGuess(), game.getAvailableCharacters(), 10);
            candidates.removeAll(game.getWrongWords()); // remove past bad guesses from candidate list          
            if (candidates.size() > 0) {
                double availableWordGuesses = Math.ceil(game.getYourCreditsLeft() / (double) game.getCreditsReductionPerWrongWord());
                double probability = availableWordGuesses / candidates.size();
                if (probability >= probabilityThreshold) {
                    return candidates.get((int) (Math.random() * candidates.size())); // Of couse, randomizing here is completely pointless...
                }
            }
            return "" + getBestCharForGivenInput(game.getAvailableCharacters(), getCharOrder());
        } else {
            return game.getWordToGuess();
        }
    }

    public String getCharOrder() {
        return "ENIRSTAHDULCGMOBWFKZVPÜÄßÖJYXQÈÉÀÂÎç";
    }

    public char getBestCharForGivenInput(String availableCharacters, String charOrder) {
        for (char charr : charOrder.toCharArray()) {
            if (availableCharacters.contains("" + charr)) {
                return charr;
            }
        }
        return availableCharacters.charAt(0);
    }
}
