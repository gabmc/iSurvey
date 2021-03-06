package utils;

import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class EnviarCorreo
{
   public void enviar(String to, String subject, String text)
   {    
      // Recipient's email ID needs to be mentioned.

      // Sender's email ID needs to be mentioned
      String from = "info@pgagroup.co";
      String nombreFrom = "iSurvey";

      // Assuming you are sending email from localhost
      String host = "localhost";

      // Get system properties
      Properties properties = System.getProperties();

      // Setup mail server
      properties.setProperty("mail.smtp.host", host);

      // Get the default Session object.
      Session session = Session.getDefaultInstance(properties);

      try{
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from, nombreFrom));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

         // Set Subject: header field
         message.setSubject(subject);

         // Now set the actual message
         message.setContent(text, "text/html");

         // Send message
         Transport.send(message);
      }catch (MessagingException mex) {
         mex.printStackTrace();
      } catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
}