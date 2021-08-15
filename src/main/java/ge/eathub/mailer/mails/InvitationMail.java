package ge.eathub.mailer.mails;

import static ge.eathub.mailer.Mailer.ADDRESS;

public class InvitationMail extends Mail {
    private static final String URL = "http://" + ADDRESS + "/join-room";
    private static final String MESSAGE = """
             <html>
             <meta charset="utf-8">
             <body>
                 <h3>HI %s <br>user: %s invited you to room with ID: %d!</h3>
                 <p> %s </p>
             </body>
            </html>
             """;

    public InvitationMail(String fromUser, String toUser, String toEmail, Long roomID) {
        super(toEmail, "room invitation", MESSAGE.formatted(toUser, fromUser, roomID, URL));
    }
}
