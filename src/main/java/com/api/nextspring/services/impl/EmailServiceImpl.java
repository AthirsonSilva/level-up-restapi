package com.api.nextspring.services.impl;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.api.nextspring.dto.EmailDto;
import com.api.nextspring.entity.EmailEntity;
import com.api.nextspring.repositories.EmailRepository;
import com.api.nextspring.services.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * This class implements the EmailService interface and provides methods for
 * sending confirmation and password reset emails.
 * It uses an EmailRepository to save the emails and a JavaMailSender to send
 * them.
 * The class also uses a ModelMapper to map between EmailEntity and EmailDto
 * objects.
 * 
 * @author Athirson Silva
 * @implNote This class implements the UserService interface and provides the
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService {
	private final EmailRepository emailRepository;
	private final JavaMailSender emailSender;
	private final ModelMapper modelMapper;

	@Value("${spring.mail.username}") // get the email from application.properties
	private String fromEmail;

	/**
	 * Sends a confirmation email to the specified email address.
	 *
	 * @param email the email object containing the necessary information to send
	 *              the email
	 * @return the email object with the updated information after sending the email
	 * @throws RuntimeException if there was an error sending the email
	 */
	@Override
	public EmailDto sendConfirmationEmail(EmailDto email) {
		EmailEntity emailEntity = buildEmailEntity(email);

		try {
			MimeMessage mimeMessage = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

			helper.setText(buildConfirmationEmail(emailEntity.getUsername(), emailEntity.getContent()), true);
			helper.setTo(emailEntity.getDestination());
			helper.setSubject("Confirm your email registration");
			helper.setFrom("athirsonarceus@gmail.com");

			log.info("Sending email --> {}", helper.toString());

			emailSender.send(mimeMessage);

			return modelMapper.map(emailRepository.save(emailEntity), EmailDto.class);
		} catch (MailException e) {
			log.error("Failed to send email: {0}", e);

			throw new RuntimeException(e);
		} catch (MessagingException e) {
			log.error("Failed to send email: {0}", e);

			throw new RuntimeException(e);
		}
	}

	/**
	 * Sends a password reset email to the specified email address.
	 *
	 * @param email the email DTO containing the necessary information for the email
	 * @return the email DTO that was sent
	 * @throws RuntimeException if there was an error sending the email
	 */
	@Override
	public EmailDto sendPasswordResetEmail(EmailDto email) {
		EmailEntity emailEntity = buildEmailEntity(email);

		try {
			MimeMessage mimeMessage = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

			helper.setText(buildPasswordResetEmail(emailEntity.getUsername(), emailEntity.getContent()), true);
			helper.setTo(emailEntity.getDestination());
			helper.setSubject("Password reset");
			helper.setFrom("athirsonarceus@gmail.com");

			log.info("Sending email --> {}", helper.toString());

			emailSender.send(mimeMessage);

			return modelMapper.map(emailRepository.save(emailEntity), EmailDto.class);
		} catch (MailException e) {
			log.error("Failed to send email: {0}", e);

			throw new RuntimeException(e);
		} catch (MessagingException e) {
			log.error("Failed to send email: {0}", e);

			throw new RuntimeException(e);
		}
	}

	/**
	 * Builds an EmailEntity object from an EmailDto object.
	 *
	 * @param email the EmailDto object to be converted to an EmailEntity object
	 * @return the EmailEntity object built from the EmailDto object
	 */
	private EmailEntity buildEmailEntity(EmailDto email) {
		EmailEntity emailEntity = new EmailEntity();

		emailEntity.setUsername(email.getUsername());
		emailEntity.setDestination(email.getDestination());
		emailEntity.setSender(fromEmail);
		emailEntity.setSubject(email.getSubject());
		emailEntity.setContent(email.getContent());
		emailEntity.setCreatedAt(LocalDateTime.now());
		return emailEntity;
	}

	/**
	 * Builds a confirmation email with the given name and activation link.
	 * 
	 * @param name the name of the recipient
	 * @param link the activation link
	 * @return the confirmation email as a string
	 */
	@Override
	public String buildConfirmationEmail(String name, String link) {
		return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
				"\n" +
				"<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
				"\n" +
				"  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
				+
				"    <tbody><tr>\n" +
				"      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
				"        \n" +
				"        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n"
				+
				"          <tbody><tr>\n" +
				"            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
				"                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
				+
				"                  <tbody><tr>\n" +
				"                    <td style=\"padding-left:10px\">\n" +
				"                  \n" +
				"                    </td>\n" +
				"                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
				"                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n"
				+
				"                    </td>\n" +
				"                  </tr>\n" +
				"                </tbody></table>\n" +
				"              </a>\n" +
				"            </td>\n" +
				"          </tr>\n" +
				"        </tbody></table>\n" +
				"        \n" +
				"      </td>\n" +
				"    </tr>\n" +
				"  </tbody></table>\n" +
				"  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
				+
				"    <tbody><tr>\n" +
				"      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
				"      <td>\n" +
				"        \n" +
				"                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
				+
				"                  <tbody><tr>\n" +
				"                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
				"                  </tr>\n" +
				"                </tbody></table>\n" +
				"        \n" +
				"      </td>\n" +
				"      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
				"    </tr>\n" +
				"  </tbody></table>\n" +
				"\n" +
				"\n" +
				"\n" +
				"  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
				+
				"    <tbody><tr>\n" +
				"      <td height=\"30\"><br></td>\n" +
				"    </tr>\n" +
				"    <tr>\n" +
				"      <td width=\"10\" valign=\"middle\"><br></td>\n" +
				"      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n"
				+
				"        \n" +
				"            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name
				+ ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\""
				+ link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
				"        \n" +
				"      </td>\n" +
				"      <td width=\"10\" valign=\"middle\"><br></td>\n" +
				"    </tr>\n" +
				"    <tr>\n" +
				"      <td height=\"30\"><br></td>\n" +
				"    </tr>\n" +
				"  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
				"\n" +
				"</div></div>";
	}

	@Override
	public String buildPasswordResetEmail(String name, String content) {
		return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
				"\n" +
				"<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
				"\n" +
				"  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
				+
				"    <tbody><tr>\n" +
				"      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
				"        \n" +
				"        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n"
				+
				"          <tbody><tr>\n" +
				"            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
				"                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
				+
				"                  <tbody><tr>\n" +
				"                    <td style=\"padding-left:10px\">\n" +
				"                  \n" +
				"                    </td>\n" +
				"                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
				"                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n"
				+
				"                    </td>\n" +
				"                  </tr>\n" +
				"                </tbody></table>\n" +
				"              </a>\n" +
				"            </td>\n" +
				"          </tr>\n" +
				"        </tbody></table>\n" +
				"        \n" +
				"      </td>\n" +
				"    </tr>\n" +
				"  </tbody></table>\n" +
				"  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
				+
				"    <tbody><tr>\n" +
				"      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
				"      <td>\n" +
				"        \n" +
				"                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
				+
				"                  <tbody><tr>\n" +
				"                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
				"                  </tr>\n" +
				"                </tbody></table>\n" +
				"        \n" +
				"      </td>\n" +
				"      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
				"    </tr>\n" +
				"  </tbody></table>\n" +
				"\n" +
				"\n" +
				"\n" +
				"  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
				+
				"    <tbody><tr>\n" +
				"      <td height=\"30\"><br></td>\n" +
				"    </tr>\n" +
				"    <tr>\n" +
				"      <td width=\"10\" valign=\"middle\"><br></td>\n" +
				"      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n"
				+
				"        \n" +
				"            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name
				+ ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Here is your new password, you can use it to change to another password of your preference later: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">"
				+ content
				+ " <p>See you soon</p>" +
				"        \n" +
				"      </td>\n" +
				"      <td width=\"10\" valign=\"middle\"><br></td>\n" +
				"    </tr>\n" +
				"    <tr>\n" +
				"      <td height=\"30\"><br></td>\n" +
				"    </tr>\n" +
				"  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
				"\n" +
				"</div></div>";
	}

}