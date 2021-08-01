package ge.eathub.mailer;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.MessagingException;

public class Mailer {

    private static String sender = "sender@gmail.com";
    private static String password = "gautexeli";

    public static void send(Session session, String[] receivers, String sub, String msg) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        for (String to : receivers) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        }
        message.setSubject(sub);
        message.setText(msg);
        //send message
        Transport.send(message);
        System.out.println("message sent successfully");

    }

    private static Session getSession(String from, String password){
        String hostServer = "smtp.gmail.com";
        String port = "587";

        Properties props = new Properties();
        props.put("mail.smtp.host", hostServer);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", "true"); // send with TLS

        return Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });
    }

    public static boolean sendMail(String email, String username, String msg, String sub) {
        String[] receivers = {email};
        try {
            send(getSession(sender, password), receivers, sub, msg);
        } catch (MessagingException e) {
            return false;
        }
        return true;
    }

}
