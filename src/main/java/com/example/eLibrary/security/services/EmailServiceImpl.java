package com.example.eLibrary.security.services;

import com.example.eLibrary.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPasswordResetEmail(User user, String newPassword) {
        String recipientEmail = user.getEmail();
        String subject = "Moonlight Hotel & Spa password reset";
        String message = "Hello " + user.getFirstName() + ",\n\n" +
                " This is your new password for logging into the Moonlight Hotel & Spa system:" + "\n\n" +
                newPassword + "\n\n" +
                " Once you have logged in, please change your password in the profile settings section." + "\n" + "\n" +
                "Best regards," + "\n" +
                "Moonlight Hotel & Spa";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setSentDate(new Date());
        email.setFrom("bootcamp@nasbg.com");
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message);


        try {
            mailSender.send(email);
        } catch (Exception ex) {
            System.err.println("Error sending registration email: " + ex.getMessage());
        }
    }

}
