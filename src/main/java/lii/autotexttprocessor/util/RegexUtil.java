package lii.autotexttprocessor.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    public static List<String> findMatches(String text, String regex) {
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    public static String replaceMatches(String text, String regex, String replacement) {
        return text.replaceAll(regex, replacement);
    }

    public static boolean isValidRegex(String regex) {
        try {
            Pattern.compile(regex);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}