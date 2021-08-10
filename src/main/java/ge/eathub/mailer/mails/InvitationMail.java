package ge.eathub.mailer.mails;

public class InvitationMail extends Mail {

    private static final String MESSAGE = """
             <html>
             <body>
                 <h3>HI %s <br>user: %s invited you to room with ID: %d!</h3>
             </body>
            </html>
             """;

    public InvitationMail(String fromUser, String toUser, String toEmail, Long roomID) {
        super(toEmail, "room invitation", MESSAGE.formatted(toUser, fromUser, roomID));
    }
}
