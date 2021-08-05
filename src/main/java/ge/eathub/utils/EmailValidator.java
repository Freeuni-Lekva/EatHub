package ge.eathub.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    // RFC 5322
    private static final String REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static boolean validate(String email) {
        Matcher matcher = PATTERN.matcher(email);
        return matcher.matches();
    }

}
