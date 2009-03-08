package org.eclipsedesktop.eclipsemail;

import java.util.Properties;
import javax.mail.*;

public class GetMessageExample {
  
  public static void main (String args[]) throws Exception {
    String host = "mail5.netbeat.de";
    String username = "eclipsemailtest%eclipsedesktop.org";
    String password = "tester";

    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    // Get the store
    Store store = session.getStore("pop3");

    // Connect to store
    store.connect(host, username, password);

    // Get folder
    Folder folder = store.getFolder("INBOX");

    // Open read-only
    folder.open(Folder.READ_ONLY);

    Message message[] = folder.getMessages();
    for (int i=0, n=message.length; i<n; i++) {
       System.out.println(message[i].getFrom()[0] 
         + "\t" + message[i].getSubject());
    }

    // Close connection 
    folder.close(false);
    store.close();
  }
}
