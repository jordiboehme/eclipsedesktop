package org.eclipsedesktop.beam.core;

import java.io.File;


public class BeamItem {

  private String fileName;
  private String fileSize;
  private File file;

  public void setFileName( final String fileName ) {
    this.fileName = fileName;
  }

  public void setFileSize( final String fileSize ) {
    this.fileSize = fileSize;
  }

  public void setFile( final File file ) {
    this.file = file;
  }
  
  public String getFileName() {
    return fileName;
  }
  
  public String getFileSize() {
    return fileSize;
  }

  public File getFile() {
    return file;
  }
}
