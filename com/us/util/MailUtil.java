/**
 * 
 */

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;

/**
 * The Class MailUtil.
 *
 * @author able<br>
 * @version 5.0<br>
 * @CreateDate 16 6 2016 <br>
 */
public class MailUtil {

	/**
	 * 邮件发送工具，默认开启SSL和TSL.
	 *
	 * @param host            邮件发送服务器
	 * @param emailUser the email user
	 * @param pswd the pswd
	 * @param toEmails            邮件接收者，支持多个接收者
	 * @param emailImage the email image
	 * @param subject            标题
	 * @param htmlText the html text
	 * @throws Exception the exception
	 */
	public static void sendEmail(String host, String emailUser, String pswd, Set<String> toEmails, String emailImage,
			String subject, String htmlText) throws Exception {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", host);
		props.setProperty("mail.user", emailUser);
		props.setProperty("mail.password", pswd);
		props.setProperty("mail.smtp.auth", "true");
		Session mailSession = Session.getDefaultInstance(props, null);
		mailSession.setDebug(true);
		Transport transport = mailSession.getTransport();
		MimeMessage message = new MimeMessage(mailSession);
		message.setSubject(subject);
		message.setFrom(new InternetAddress(emailUser));

		InternetAddress[] Email = new InternetAddress[toEmails.size()];
		Iterator<String> it = toEmails.iterator();
		for (int i = 0; i < toEmails.size(); i++) {
			Email[i] = new InternetAddress(it.next());
		}

		message.addRecipients(Message.RecipientType.TO, Email);

		MimeBodyPart text = new MimeBodyPart();
		// setContent(“邮件的正文内容”,”设置邮件内容的编码方式”)
		text.setContent(htmlText, "text/html;charset=UTF-8");
		// 创建图片
		MimeBodyPart img = new MimeBodyPart();

		DataHandler dh = new DataHandler(new FileDataSource(emailImage));// 图片路径
		img.setDataHandler(dh);
		// 创建图片的一个表示用于显示在邮件中显示
		img.setContentID("<image>");

		MimeMultipart mm = new MimeMultipart();
		mm.addBodyPart(text);
		mm.addBodyPart(img);
		mm.setSubType("related");// 设置正文与图片之间的关系
		// 图班与正文的 body
		MimeBodyPart all = new MimeBodyPart();
		all.setContent(mm);
		// 附件与正文（text 和 img）的关系
		MimeMultipart mm2 = new MimeMultipart();
		mm2.addBodyPart(all);
		mm2.setSubType("mixed");// 设置正文与附件之间的关系

		message.setContent(mm2);
		message.saveChanges(); // 保存修改

		transport.connect(null, emailUser, pswd);
		transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
		transport.close();

	}

	/**
	 * Get the mail text detail .
	 *
	 * @param freeMarkerConfigurer the free marker configurer
	 * @param templateName the template name
	 * @param map the map
	 * @return the String
	 * @throws Exception the exception
	 */
	public static String getMailText(FreeMarkerConfigurer freeMarkerConfigurer, String templateName,
			Map<String, Object> map) throws Exception {
		String htmlText = "";
		// 通过指定模板名获取FreeMarker模板实例
		Template tpl = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);
		// FreeMarker通过Map传递动态数据
		// 解析模板并替换动态数据，最终content将替换模板文件中的${content}标签。
		htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, map);
		return htmlText;
	}

}
