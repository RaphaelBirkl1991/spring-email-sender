package io.getarrays.springemailsender.service.implementation;

import io.getarrays.springemailsender.service.EmailService;
import io.getarrays.springemailsender.utils.EmailUtils;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Map;

import static io.getarrays.springemailsender.utils.EmailUtils.*;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "emailTemplate";
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getEmailMessage(name, host, token));
            emailSender.send(message);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithAttachments(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(getEmailMessage(name, host, token));
            // Add attachments
            FileSystemResource fort = new FileSystemResource(new File(System.getProperty("user.home") + "/OneDrive/Desktop/sp-email/fort.jpg"));
            FileSystemResource donkey = new FileSystemResource(new File(System.getProperty("user.home") + "/OneDrive/Desktop/sp-email/donkey.jpg"));
            FileSystemResource ppt = new FileSystemResource(new File(System.getProperty("user.home") + "/OneDrive/Desktop/sp-email/ppt.pptx"));
            helper.addAttachment(fort.getFilename(), fort);
            helper.addAttachment(donkey.getFilename(), donkey);
            helper.addAttachment(ppt.getFilename(), ppt);
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }


    @Override
    @Async
    public void sendMimeMessageWithEmbeddedFiles(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(getEmailMessage(name, host, token));
            // Add attachments
            FileSystemResource fort = new FileSystemResource(new File(System.getProperty("user.home") + "/OneDrive/Desktop/sp-email/fort.jpg"));
            FileSystemResource donkey = new FileSystemResource(new File(System.getProperty("user.home") + "/OneDrive/Desktop/sp-email/donkey.jpg"));
            FileSystemResource ppt = new FileSystemResource(new File(System.getProperty("user.home") + "/OneDrive/Desktop/sp-email/ppt.pptx"));
            helper.addInline(getContentId(fort.getFilename()), fort);
            helper.addInline(getContentId(donkey.getFilename()), donkey);
            helper.addInline(getContentId(ppt.getFilename()), ppt);
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }


    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {
        try {
            Context context = new Context();
//            context.setVariable("name", name);
//            context.setVariable("url", getVerificationUrl(host, token));
            // name + url are in html template -> thymeleaf
            context.setVariables(Map.of("name", name, "url", getVerificationUrl(host, token)));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {

    }


    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }

    private String getContentId(String filename){
        return "<" + filename + ">";
    }
}
