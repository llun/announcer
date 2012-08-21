package jobs;

import org.apache.commons.mail.HtmlEmail;
import play.Logger;
import play.jobs.Job;
import play.libs.Mail;

/**
 * User: llun
 * Date: 6/13/11 AD Time: 3:14 PM
 */
public class SendOnceJob extends Job {

  private String to;
  private String from;
  private String subject;
  private String htmlContent;
  private String textContent;

  public SendOnceJob(String to, String from,
                     String subject,
                     String htmlContent, String textContent) {
    this.to = to;
    this.from = from;
    this.subject = subject;
    this.htmlContent = htmlContent;
    this.textContent = textContent;
  }

  public void doJob() {
    Logger.debug("Prepare mail for %s", to);

    try {
      HtmlEmail mail = new HtmlEmail();
      mail.addTo(to);
      mail.setFrom(from, "Sender");
      mail.setSubject(subject);
      mail.setCharset("UTF-8");

      if (htmlContent != null) {
        mail.setHtmlMsg(htmlContent);
      }

      if (textContent != null && textContent.length() > 0) {
        mail.setTextMsg(textContent);
      }

      Mail.send(mail);

      Logger.debug("Sent mail to: %s", to);
    } catch (Exception e) {
      Logger.debug(e, "Something wrong");
    }
  }

}
