package org.eclipsedesktop.eclipsemail.model;

import java.util.Date;

public interface IMailMessage {
  String getSubject();
  String getFrom();
  Date getDate();
  int getSize();
  String getContent();
  String[] getRecipients();
  String getFingerprint();
  String[] getAttachmentNames();
  
  
}
