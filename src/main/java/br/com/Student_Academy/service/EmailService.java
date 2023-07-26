package br.com.Student_Academy.service;

import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${support.mail}")
	private String supportMail;
	
	//Método de envio de e-mails, recebendo assunto, destinatário e conteudo.
	public void sendEmailToClient(String subject, String email, String content){
		try {
		//Criando o email.
		MimeMessage mail = mailSender.createMimeMessage();
		
		//Assunto, texto, envado de: , para: .
		MimeMessageHelper message = new MimeMessageHelper(mail);
		message.setSubject(subject);
		message.setText(content, true);
		message.setFrom(supportMail);
		message.setTo(email);
		
		mailSender.send(mail);
		}catch (MessagingException e) {
			e.getMessage();
		}
	}
	
	public String contentMail(String name) {
		return "<p>Ol&aacute; "+name+" este &eacute; o email de confirm&ccedil;&atilde;o do seu cadastro"
				+ " no sistema, ficamos muito satisfeito em te receber e saber que tenha escolhido nosso"
				+ " sistema para auxiliar no seus projetos daqui em diante, que fa&ccedil;amos juntos uma"
				+ " otima trilha, um grande abra&ccedil;o de toda nossa equipe do <strong>Student Academy."
				+ "</strong></p>";
	}
}
