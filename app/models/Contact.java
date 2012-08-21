package models;

import play.Logger;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: llun
 * Date: 6/9/11 AD
 * Time: 12:24 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Contact extends Model {

    public String name;
    @Column(unique = true)
    public String email;

    public Contact(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static int importContacts(File importFile) {

        int totalSuccess = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(importFile));

            String line = null;
            while ((line = reader.readLine()) != null) {
                // import file should have format name, email
                String format = "^[\\w ]+, *[\\w@.-]+ *$";

                Pattern pattern = Pattern.compile(format);
                Matcher matcher = pattern.matcher(line);
                boolean matches = matcher.matches();

                Logger.debug(String.format("Is pattern (%s) match (%s) : %s", format, line, matches ? "yes" : "no"));

                if (matches) {
                    String[] contact = line.split(",");
                    String name = contact[0].trim();
                    String email = contact[1].trim();

                    Contact model = new Contact(name, email);
                    model.save();

                    totalSuccess++;
                }
            }

        } catch (IOException e) {
            Logger.debug("Can't import contacts");
        }

        return totalSuccess;

    }
}
