package jobs;

import models.Contact;
import org.apache.commons.mail.HtmlEmail;
import play.Logger;
import play.jobs.Job;
import play.libs.Mail;

import java.io.File;
import java.util.List;

import javax.mail.internet.InternetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: llun
 * Date: 6/9/11 AD
 * Time: 1:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class SendMailJob extends Job {

  private String from;
  private String subject;
  private String htmlContent;
  private String textContent;
  private File attachments;

  public SendMailJob(String from, String subject, String htmlContent, String textContent, File attachments) {
    this.from = from;
    this.subject = subject;
    this.htmlContent = htmlContent;
    this.textContent = textContent;
    this.attachments = attachments;
  }

  public void doJob() {

    Logger.debug("Send mail job begin");

    List<Contact> contacts = Contact.all().fetch();
    for (Contact contact : contacts) {
      Logger.debug("Prepare mail for %s", contact.email);

      try {
        HtmlEmail mail = new HtmlEmail();
        mail.addTo(contact.email);
        mail.setFrom(from, "Sender");
        mail.setSubject(subject);
        mail.setCharset("UTF-8");

        if (attachments != null) {
          String cid = mail.embed(attachments);
          htmlContent += "<img src=\"cid:" + cid + "\" />";
        }

        if (htmlContent != null) {
          mail.setHtmlMsg(htmlContent);
        }

        if (textContent != null && textContent.length() > 0) {
          mail.setTextMsg(textContent);
        }

        Mail.send(mail);

        Logger.debug("Sent mail to: %s", contact.email);
      } catch (Exception e) {
        Logger.debug(e, "Something wrong");
        continue;
      }

    }
  }

}
