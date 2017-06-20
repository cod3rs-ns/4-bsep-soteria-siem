package bsep.sw.util;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class MailSender {

    private Logger logger = Logger.getLogger(getClass().getName());

    private static final String HOST_NAME = "smtp.gmail.com";
    private static final int HOST_PORT = 587;
    private static final String AUTH_USER = "bacovakuhinja@gmail.com";
    private  static final String AUTH_PASS = "jedanjebaco";

    @Async
    public void sendLicense(final String name, final String key, final String address) {
        try {
            final String message = "Dear " + name + ",\n" +
                    "Congratulations! We've sent you license key for your agent.\n\n" +
                    "Please use provided license file and put it into folder where your agent is.";

            final PrintWriter writer;
            try {
                writer = new PrintWriter("license", "UTF-8");
                writer.println(key);
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            sendMailWithAttachment(address, message);
        } catch (MessagingException e) {
            logger.log(Level.WARNING, "", e);
        }
    }

    private void sendMailWithAttachment(final String address, final String message) throws MessagingException {
        final JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setDefaultEncoding("UTF-8");

        final Properties properties = new Properties();
        properties.put("mail.smtp.host", HOST_NAME);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", HOST_PORT);
        sender.setJavaMailProperties(properties);

        final Session mailSession = Session.getDefaultInstance(properties, null);
        final Multipart multipart = new MimeMultipart();
        final MimeMessage mailMessage = new MimeMessage(mailSession);

        final InternetAddress recipient = new InternetAddress(address);

        mailMessage.addRecipient(Message.RecipientType.TO, recipient);
        mailMessage.setSubject("Agent license key");

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(message);
        multipart.addBodyPart(messageBodyPart);

        messageBodyPart = new MimeBodyPart();
        final DataSource source = new FileDataSource("license");
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName("license");
        multipart.addBodyPart(messageBodyPart);

        mailMessage.setContent(multipart);

        final Transport transport = mailSession.getTransport("smtp");
        transport.connect(HOST_NAME, AUTH_USER, AUTH_PASS);
        transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
        transport.close();
    }
}
