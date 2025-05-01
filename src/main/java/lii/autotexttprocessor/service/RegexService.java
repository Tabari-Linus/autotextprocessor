package lii.autotexttprocessor.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexService {
    public String searchAndReplace(String input, String regex, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll(replacement);
    }
}