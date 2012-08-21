package jobs;

import models.Contact;
import play.jobs.Job;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: llun
 * Date: 6/9/11 AD
 * Time: 12:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportContactsJob extends Job<Integer> {

    private File contactFile = null;

    public ImportContactsJob(File contactFile) {
        this.contactFile = contactFile;
    }

    public Integer doJobWithResult() {
        return Contact.importContacts(contactFile);
    }

}
