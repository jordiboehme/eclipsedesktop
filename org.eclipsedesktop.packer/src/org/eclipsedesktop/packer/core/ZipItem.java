package org.eclipsedesktop.packer.core;

import java.io.File;

public class ZipItem {
  
  private File fileObject;
  private String fullName; 

  public boolean isDirectory() {
    return fileObject.isDirectory();
  }
  
  public boolean isFile() {
    return fileObject.isFile();
  }
  
  public File getFileObject() {
    return fileObject;
  }
  
  public void setFileObject( final File fileObject ) {
    this.fileObject = fileObject;
  }
  
  public String getFullName() {
    return fullName;
  }
  
  public void setFullName( final String fullName ) {
    this.fullName = fullName;
  }
}
