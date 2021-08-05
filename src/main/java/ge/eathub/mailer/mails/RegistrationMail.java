package ge.eathub.mailer.mails;

public class RegistrationMail extends Mail {
    private static final String ADDRESS = "localhost:8888";
    private static final String URL = "http://" + ADDRESS + "/confirm?confirm-token=";
    private static final String SUBJECT = "EatHub - Confirm registration";
    private static final String MESSAGE = """
             <html>
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
    private String token;

    public RegistrationMail(String username, String email, String token) {
        super(email, SUBJECT, MESSAGE.formatted(username, token, URL, token));
    }
}
