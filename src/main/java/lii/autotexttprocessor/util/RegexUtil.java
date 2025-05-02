package lii.autotexttprocessor.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexUtil {

    // Method to find all matches of a regex pattern in a given text and return them as a list
    public static List<String> findMatches(String text, String regex) {
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    // Method to replace all matches of a regex pattern in a given text with a replacement string and return the modified text
    public static String replaceMatches(String text, String regex, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll(replacement);
    }

    // Method to validate a regex pattern
    public static boolean isValidRegexPattern(String regex) {
        try {
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    // Method to provide match information as a Map of match and paired to its index
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