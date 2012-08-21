import models.Contact;
import org.junit.Test;
import play.test.UnitTest;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: llun
 * Date: 6/9/11 AD
 * Time: 12:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class ContactTest extends UnitTest {

  @Test
  public void testImportContacts() {

    int success = Contact.importContacts(new File("test/sample.csv"));
    assertEquals(3, success);

  }

}
