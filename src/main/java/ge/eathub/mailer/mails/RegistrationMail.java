package ge.eathub.mailer.mails;

import static ge.eathub.mailer.Mailer.ADDRESS;

public class RegistrationMail extends Mail {
    private static final String URL = "http://" + ADDRESS + "/confirm?confirm-token=";
    private static final String SUBJECT = "EatHub - Confirm registration";
    private static final String MESSAGE = """
             <html>
             <meta charset="utf-8">
             <body>
                 <h3>%s, welcome to EatHub!!!</h3>
                 <p>
                     your token is:%s
                 </p>
                 <p>%s%s</p>
                 <p> If it was not you, just don't concern yourself with this mail</p>
             </body>
            </html>
             """;

    public RegistrationMail(String username, String email, String token) {
        super(email, SUBJECT, MESSAGE.formatted(username, token, URL, token));
    }
}
