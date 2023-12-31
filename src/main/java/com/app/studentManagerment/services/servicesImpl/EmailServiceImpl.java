package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.MailProRepository;
import com.app.studentManagerment.entity.MailPro;
import com.app.studentManagerment.entity.user.Student;
import com.app.studentManagerment.services.MailProService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Service
public class EmailServiceImpl implements MailProService {
	private MailProRepository mailProRepository;

	@Autowired
	public EmailServiceImpl(MailProRepository mailProRepository) {
		this.mailProRepository = mailProRepository;
	}

	@Override
	public JavaMailSender getJavaMailSender(MailPro mailPro) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(mailPro.getHost());
		mailSender.setPort(mailPro.getPort());
		mailSender.setUsername(mailPro.getUsername());
		mailSender.setPassword(mailPro.getPassword());
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", mailPro.getSmtp_auth());
		props.put("mail.smtp.starttls.enable", mailPro.getSmtp_starttls_enable());
		props.put("mail.debug", "true");
		return mailSender;
	}

	@Async
	@Override
	public void sendSimpleMessage(String type, List<Objects> list, MailPro mailPro, JavaMailSender sender, String to, String subject, String text) {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom(mailPro.getUsername());
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text);
			sender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@Async
	@Transactional
	@Override
	public void sendMessageWithHtmlTemplate(List<String> emails, MailPro mailPro, JavaMailSender sender, String subject, String pathToHTMLFile) {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = null;
		Resource resource = new DefaultResourceLoader().getResource("classpath:" + pathToHTMLFile);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(resource.getURL().getPath()), "UTF-8"))) {
			String line;
			StringBuilder html = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				html.append(line);
			}
			helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(mailPro.getUsername());
			helper.setTo(emails.get(0));
			if (emails.size() >= 2) {
				helper.setBcc(emails.subList(1, emails.size()).toArray(new String[]{}));
			}
			helper.setSubject(subject);
			helper.setText(html.toString(), true);
			sender.send(message);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}

