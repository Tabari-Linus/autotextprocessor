package lii.autotexttprocessor.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexUtil {

    /**
     * Utility class for regex operations
     * This class provides methods to find, replace, and validate regex patterns in text.
     *@param text The input text to search within.
     *@param regex The regex pattern to search for.
     *@return A list of matches found in the text.
     */
    public static List<String> findMatches(String text, String regex) {
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    /**
     * Method to replace all matches of a regex pattern in a given text with a replacement string.
     * @param text The input text to search within.
     * @param regex The regex pattern to search for.
     * @param replacement The string to replace the matches with.
     * @return The modified text with replacements made.
     */
    public static String replaceMatches(String text, String regex, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll(replacement);
    }

    /**
     * Method to validate a regex pattern.
     * @param regex The regex pattern to validate.
     * @return true if the regex is valid, false otherwise.
     */
    public static boolean isValidRegexPattern(String regex) {
        try {
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    /**
     * Method to get match information including start and end indices.
     * @param text The input text to search within.
     * @param regex The regex pattern to search for.
     * @return A list of strings containing match information.
     */
    public static List<String> getMatchInfo(String text, String regex) {
        List<String> matchInfo = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            matchInfo.add("Match: " + matcher.group() + ", Start: " + matcher.start() + ", End: " + matcher.end());
        }
        return matchInfo;
    }

}