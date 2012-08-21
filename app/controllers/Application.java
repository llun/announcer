package controllers;

import jobs.ImportContactsJob;
import jobs.SendMailJob;
import jobs.SendOnceJob;
import models.Contact;
import play.Logger;
import play.Play;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.F;
import play.mvc.Controller;

import java.io.File;
import java.util.List;

public class Application extends Controller {

  public static void index() {
    long totalContacts = Contact.count();

    Object sender = Play.configuration.get("mail.smtp.user");
    if (sender != null) {
      renderArgs.put("sender", sender);
    }

    render(totalContacts);
  }

  public static void importPage() {
    render();
  }

  public static void importContacts(@Required File importFile) {
    Logger.debug("Is import file null: %s", importFile == null ? "yes" : "no");

    if (validation.hasErrors()) {
      flash.keep();
      importPage();
    }

    F.Promise<Integer> promise = new ImportContactsJob(importFile).now();
    Integer success = await(promise);

    flash("importCount", success);
    index();

  }

  public static void removeAllContacts() {
    int success = Contact.deleteAll();
    flash("deleteCount", success);
    index();
  }

  public static void listContacts() {
    List<Contact> contacts = Contact.all().fetch();
    renderJSON(contacts);
  }

  public static void sendOnce(@Required String to,
                              @Required String from,
                              @Required String subject,
                              String htmlContent,
                              String textContent) {
    if (Validation.hasErrors()) {
      params.flash();
      Validation.keep();
      index();
    }

    F.Promise promise = new SendOnceJob(to, from, subject, htmlContent, textContent).now();
    await(promise);

    renderText("OK");
  }

  public static void send(@Required String from,
                          @Required String subject,
                          File attachment,
                          String htmlContent,
                          String textContent) {
    if (validation.hasErrors()) {
      params.flash();
      validation.keep();
      index();
    }

    File realAttachment = null;
    if (attachment != null) {
      File uploadDir = new File("uploads");
      if (!uploadDir.exists()) {
        uploadDir.mkdir();
      }

      File target = new File(uploadDir, attachment.getName());
      if (target.exists()) {
        target.delete();
      }

      attachment.renameTo(target);
      realAttachment = target;
    }

    F.Promise promise = new SendMailJob(from, subject, htmlContent, textContent, realAttachment).now();
    await(promise);

    index();
  }

}