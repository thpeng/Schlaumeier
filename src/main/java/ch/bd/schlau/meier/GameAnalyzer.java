/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bd.schlau.meier;

import ch.bnd.game.gameplayercontrol.Game;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.google.common.primitives.Chars;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    private String searchFilestr;

    @PostConstruct
    public void startup() throws IOException {
        //TODO umstellen auf resource stream zeugs. 
        searchFilestr = Files.toString(new File("C:/Users/caleb/Documents/NetBeansProjects/Schlaumeier/src/main/resources/CH_NEU.DIC"), Charset.defaultCharset());
    }

    protected List<String> getPossibleMatches(String word, String availableChars) {
        String regExp = getRegexp(word, getUnderscoreRegexp(availableChars));
        Pattern pattern = Pattern.compile(regExp, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(searchFilestr);
        List<String> matches = new ArrayList<String>();
        int laufNr = 0;
        while (matcher.find()) {
            if (laufNr > 9) {
                return Collections.emptyList();
            }
            matches.add(matcher.group());
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
        sb.append(Joiner.on('|').join(Chars.asList(availableChars.toCharArray())));
        sb.append("]");

        return sb.toString();
    }

    public String analyze(Game game) {
        Preconditions.checkNotNull(game);
        Preconditions.checkNotNull(searchFilestr, "startup method was not called!");
        if (game.getWordToGuess().contains("_")) {
            List<String> possibleMatches = getPossibleMatches(game.getWordToGuess(), game.getAvailableCharacters());
            if (possibleMatches.size() != 1) {
                return "" + getBestCharForGivenInput(game.getAvailableCharacters(), getCharOrder());
            }
            return possibleMatches.get(0);
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
