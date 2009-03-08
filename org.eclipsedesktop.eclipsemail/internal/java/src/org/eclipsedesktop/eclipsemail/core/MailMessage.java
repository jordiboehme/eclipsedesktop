package org.eclipsedesktop.eclipsemail.core;

import java.util.Date;
import org.eclipsedesktop.eclipsemail.model.IMailMessage;

class MailMessage implements IMailMessage {

  private String subject;
  private String from;
  private Date date;
  private int size;
  private String content;
  private String[] recipients;
  private String fingerprint;
  private String[] attachmentNames;

  void setDate( final Date date ) {
    this.date = date;
  }
  
  void setFrom( final String from ) {
    this.from = from;
  }
  
  void setSubject( final String subject ) {
    this.subject = subject;
  }
  
  void setSize( final int size ) {
    this.size = size;
  }
  
  void setContent( final String content ) {
    this.content = content;
  }
  
  void setRecipients( final String[] recipients ) {
    this.recipients = recipients;
  }
  
  void setFingerprint( final String fingerprint ) {
    this.fingerprint = fingerprint;
  }
  
  public void setAttachmentNames( final String[] attachmentNames ) {
    this.attachmentNames = attachmentNames;
  }
  
  // interface methods of IMailMessage
  ////////////////////////////////////
  
  public String getSubject() {
    return subject;
  }

  public String getFrom() {
    return from;
  }

  public Date getDate() {
    return date;
  }

  public int getSize() {
    return size;
  }

  public String getContent() {
    return content;
  }

  public String[] getRecipients() {
    return recipients;
  }
  
  public String getFingerprint() {
    return this.fingerprint;
  }

  public String[] getAttachmentNames() {
    return attachmentNames;
  }
  
}