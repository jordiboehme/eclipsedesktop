//Copyright (c) 2004 by Eclipsedesktop.org
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.eclipsemail.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipsedesktop.eclipsemail.EclipsemailPlugin;
import org.eclipsedesktop.eclipsemail.IEclipsemailConstants;
import org.eclipsedesktop.eclipsemail.model.IMailMessage;

/**
 * @author Leif Frenzel, Jordi Böhme López
 */
public class Inbox implements IEclipsemailConstants {

  private static Inbox _instance;
  private List<MailMessage> messageList;
  private Inbox() {
    // prevent instantiation from outside
    messageList = new ArrayList<MailMessage>();
  }
  
  public static synchronized Inbox getInstance() {
    if( _instance == null ) {
      _instance = new Inbox();
    }
    return _instance;
  }
  
  public synchronized IMailMessage[] getMessages() {
    IPreferenceStore preferenceStore 
      = EclipsemailPlugin.getDefault().getPreferenceStore();
    
    String host = preferenceStore.getString( POP_HOST_KEY );     // "mail5.netbeat.de";
    String username = preferenceStore.getString( POP_USER_KEY ); // "eclipsemailtest%eclipsedesktop.org";
    int port = preferenceStore.getInt( POP_PORT_KEY );           // 110;
    String password = preferenceStore.getString( POP_PSWD_KEY ); // "tester";

    Message[] messages = new Message[ 0 ];
    try {
      Properties props = new Properties();
      Session session = Session.getDefaultInstance( props, null );
      Store store = session.getStore( "pop3" );
      store.connect( host, port, username, password );
      Folder folder = store.getFolder( "INBOX" );
      folder.open( Folder.READ_ONLY );
      messages = folder.getMessages();
      for( int i = 0; i < messages.length; i++ ) {
        if( !massageExists( messages[ i ], this.messageList ) ) {
          addMessage( messages[ i ], this.messageList );
        }
      }
      folder.close( false );
      store.close();
    } catch( Exception ex ) {
      ex.printStackTrace();
      //TODO: Connect Error Message 
    }
    return messageList.toArray( new IMailMessage[ messageList.size() ] );
  }

  public synchronized void deleteMessage( final IMailMessage message ) {
    IPreferenceStore preferenceStore 
      = EclipsemailPlugin.getDefault().getPreferenceStore();
    
    String host = preferenceStore.getString( POP_HOST_KEY );     // "mail5.netbeat.de";
    String username = preferenceStore.getString( POP_USER_KEY ); // "eclipsemailtest%eclipsedesktop.org";
    int port = preferenceStore.getInt( POP_PORT_KEY );           // 110;
    String password = preferenceStore.getString( POP_PSWD_KEY ); // "tester";

    try {
      Properties props = new Properties();
      Session session = Session.getDefaultInstance( props, null );
      Store store = session.getStore( "pop3" );
      store.connect( host, port, username, password );
      Folder folder = store.getFolder( "INBOX" );
      folder.open( Folder.READ_WRITE );
      Message[] messages = folder.getMessages();
      Set<String> currentMessages = new HashSet<String>(); 
      for( int i = 0; i < messages.length; i++ ) {
        String fingerprint = createFingerprint( messages[ i ] );
        currentMessages.add( fingerprint );
        if( fingerprint.equals( message.getFingerprint() ) ) {
          messages[ i ].setFlag( Flags.Flag.DELETED, true );
        }
      }
      //this.messageList.remove( message );
      
      Iterator<MailMessage> listIter = this.messageList.iterator();
      while( listIter.hasNext() ) {
        IMailMessage mm = listIter.next();
        if( !currentMessages.contains( mm.getFingerprint() ) ) {
          this.messageList.remove( mm );
        }
      }
      folder.close( true );
      store.close();
    } catch( Exception ex ) {
      //TODO: Connect Error Message 
    }
  }


  // helping methods
  //////////////////
  
  private boolean massageExists( final Message message, final List<MailMessage> list ) {
    boolean result = false;
    Iterator<MailMessage> listIter = list.iterator();
    while( listIter.hasNext() && !result ) {
      IMailMessage mm = listIter.next();
      result = mm.getFingerprint().equals( createFingerprint( message ) );
    }
    return result;
  }
  
  private void addMessage( final Message message, final List<MailMessage> list ) {
    MailMessage mm = new MailMessage();
    try {
      mm.setSubject( message.getSubject() );
      mm.setDate( message.getSentDate() );
      mm.setFrom( MimeUtility.decodeText( message.getFrom()[ 0 ].toString() ) );
      mm.setSize( message.getSize() );
      if( message.getContent() instanceof MimeMultipart ) {
        List<String> attList = new ArrayList<String>();
        MimeMultipart mimeMultipart = ( MimeMultipart )message.getContent();
        mimeMultipart.getCount();
        for( int i = 0; i < mimeMultipart.getCount(); i++ ) {
          if( mimeMultipart.getBodyPart( i ).isMimeType( "text/plain" ) ) {
            mm.setContent( mimeMultipart.getBodyPart( i ).getContent().toString() );
          } else {
            String fileName = mimeMultipart.getBodyPart( i ).getFileName();
            if( fileName != null ) {
              attList.add( fileName );
            }
          }
        }
        if( attList.size() > 0 ) {
          String[] atts = new String[ attList.size() ] ;
          attList.toArray( atts );
          mm.setAttachmentNames( atts ); 
        } else {
          mm.setAttachmentNames( new String[ 0 ] );
        }
      } else {
        mm.setContent( message.getContent().toString() );
        mm.setAttachmentNames( new String[ 0 ] );
      }
      mm.setFingerprint( createFingerprint( message ) );
      String[] recipients = new String[ message.getAllRecipients().length ];
      Address[] addresses = message.getAllRecipients();
      for( int i = 0; i < addresses.length; i++ ) {
        recipients[ i ] = MimeUtility.decodeText( addresses[ i ].toString() );
      }
      mm.setRecipients( recipients );
    } catch( Exception ex ) {
      ex.printStackTrace();
    }
    list.add( mm );
  }
  
  private String createFingerprint( final Message message ) {
    String result = "";
    try {
      result = (   message.getSentDate().toString() 
                 + message.getSubject() 
                 + message.getFrom()[0].toString() );
    } catch( Exception e ) {
    }
    return result;
  }
}
