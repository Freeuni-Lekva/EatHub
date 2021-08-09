package ge.eathub.mailer.mails;

public class Mail {
    private String to;
    private String subject;
    private String message;

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public Mail(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }


}
