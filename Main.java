import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class BirthdaySetter {

    public static void main(String[] args) {
        // Asking the user for the person's information
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter birthday (yyyy-MM-dd): ");
        String birthdayStr = scanner.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = null;
        try {
            birthday = dateFormat.parse(birthdayStr);
          } catch (ParseException e) {
            e.printStackTrace();
          }

        System.out.print("Enter email address: ");
        String email = scanner.nextLine();

        // Write data to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("people.txt", true))) {
            writer.write(name + "," + age + "," + birthdayStr + "," + email);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

      // Check if it's the person's birthday
        Calendar today = Calendar.getInstance();
        Calendar birthdayCalendar = Calendar.getInstance();
        birthdayCalendar.setTime(birthday);

      if (today.get(Calendar.MONTH) == birthdayCalendar.get(Calendar.MONTH)
            && today.get(Calendar.DAY_OF_MONTH) == birthdayCalendar.get(Calendar.DAY_OF_MONTH)) {
            sendBirthdayEmail(name, email);
        }
    }

  public static void birthdayEmailSender(String name, String email) {
        final host = "smtp.gmail.com";
        final port = "465";
        final String username = System.getEnv(username);
        final String password = System.getEnv(password);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Happy Birthday " + name);
            message.setText("Just wanted to wish you a happy birthday on your very special day!");

            Transport.send(message);

            System.out.println("Birthday email sent successfully to " + name);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}