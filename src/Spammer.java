import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class Spammer {
    public List<String> inputEmails() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        List<String> emails = new ArrayList<>();
        System.out.println("Enter please recipient email");
        String recipientEmail;
        while ((recipientEmail = bufferedReader.readLine()) != null) {
            if (recipientEmail.equals("")) {
                System.out.println("You've entered " + emails.size() + " emails");
                System.out.println("Please wait...");
                break;
            }
            emails.add(recipientEmail);
        }
        return emails;
    }

    public void sendMassage(String userEmail, String password, String subject, String messageBody,
                            String emailTo) throws IOException {

        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.host", "smtp.gmail.com");
        properties.put("mail.smtps.auth", "true");
        properties.put("mail.smtps.user", userEmail);

        Session session = Session.getDefaultInstance(properties);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setSubject(subject);
            message.setText(messageBody);
            message.setFrom(new InternetAddress(userEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            message.setSentDate(new Date());

            Transport transport = session.getTransport();
            transport.connect("smtp.gmail.com", 465, userEmail, password);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));

            Transport.send(message);
            transport.close();
        } catch (MessagingException e) {}
    }

    public static void main(String[] args) {
        Spammer spammer = new Spammer();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter please your email");
            String userEmail = bufferedReader.readLine();
            System.out.println("Enter please your password");
            String password = bufferedReader.readLine();
            System.out.println("Enter please subject");
            String subject = bufferedReader.readLine();
            System.out.println("Enter please message");
            String messageBody = bufferedReader.readLine();

            List<String> emails = spammer.inputEmails();

            emails.forEach(email -> {
                try {
                    spammer.sendMassage(userEmail, password, subject, messageBody, email);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("Your messages have sent!");
        } catch (IOException e) {
            System.out.println("Some incorrect data");
        }
    }
}
