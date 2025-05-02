package lii.autotexttprocessor.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.regex.Matcher;

public class RegexPatterns {

    // Method to check if a string is a valid regex pattern
    public static boolean isValidRegexPattern(String regex) {
        try {
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }



}
