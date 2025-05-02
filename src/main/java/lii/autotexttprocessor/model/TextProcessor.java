package lii.autotexttprocessor.model;

import lii.autotexttprocessor.util.RegexUtil;

import java.util.List;

public class TextProcessor {

    private final RegexUtil regexUtil;

    public TextProcessor() {
        this.regexUtil = new RegexUtil();
    }

    // Method to find all matches of a regex pattern in a given text and return them as a list
    public List<String> findMatchPattern(String text, String regex) {
        if (!regexUtil.isValidRegexPattern(regex)) {
            throw new IllegalArgumentException("Not a valid regex pattern: " + regex);
        }
        return regexUtil.findMatches(text, regex);
    }

    // Method to replace all matches of a regex pattern in a given text
    // with a replacement string and return the modified text
    public String replaceMatches(String text, String regex, String replacement) {
        if (!regexUtil.isValidRegexPattern(regex)) {
            throw new IllegalArgumentException("Not a valid regex pattern: " + regex);
        }
        return regexUtil.replaceMatches(text, regex, replacement);
    }

}
