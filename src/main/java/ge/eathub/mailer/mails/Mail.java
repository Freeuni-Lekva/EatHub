package ge.eathub.mailer.mails;

public class Mail {
    private final String to;
    private final String subject;
    private String message;

    public Mail(String to, String subject) {
        this.to = to;
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
