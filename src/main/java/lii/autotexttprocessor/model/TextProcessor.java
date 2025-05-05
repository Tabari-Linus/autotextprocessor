package lii.autotexttprocessor.model;

import lii.autotexttprocessor.util.RegexUtil;

import java.util.List;

public class TextProcessor {

    private final RegexUtil regexUtil;

    public TextProcessor() {
        this.regexUtil = new RegexUtil();
    }

    /**
     * Finds all matches of the regex pattern in the text.
     *
     * @param text  The input text to search.
     * @param regex The regex pattern to match.
     * @return A list of strings that match the regex pattern.
     */
    public List<String> findMatchPattern(String text, String regex) {
        if (!regexUtil.isValidRegexPattern(regex)) {
            throw new IllegalArgumentException("Not a valid regex pattern: " + regex);
        }
        return regexUtil.findMatches(text, regex);
    }

    /**
     * Replaces all matches of the regex pattern in the text with the specified replacement string.
     *
     * @param text        The input text to process.
     * @param regex       The regex pattern to match.
     * @param replacement The string to replace the matches with.
     * @return The modified text with replacements made.
     */
    public String replaceMatches(String text, String regex, String replacement) {
        if (!regexUtil.isValidRegexPattern(regex)) {
            throw new IllegalArgumentException("Not a valid regex pattern: " + regex);
        }
        return regexUtil.replaceMatches(text, regex, replacement);
    }

}
