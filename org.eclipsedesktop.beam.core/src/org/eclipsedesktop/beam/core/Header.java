package org.eclipsedesktop.beam.core;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


public class Header {

  private Document document;
  private String fileName;
  private String fileSize;
  private String checksum;
  
  public static void parse( final Header header, final String raw ) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating( false );
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      ByteArrayInputStream is = new ByteArrayInputStream( raw.getBytes() );
      header.document = builder.parse( is );
    } catch( ParserConfigurationException PCE ) {
      System.out.println( PCE.toString() );
    } catch( IOException IOE ) {
      System.out.println( IOE.toString() );
    } catch( SAXException SE ) {
      System.out.println( SE.toString() );
    }
    header.process();
  }

  public String getFileName() {
    return this.fileName;
  }

  public String getFileSize() {
    return this.fileSize;
  }
  
  public String getChecksum() {
    return this.checksum;
  }
  
  // helping methods
  //////////////////
  
  private void process() {
    Element root = this.document.getDocumentElement();
    NodeList nodes = root.getChildNodes();
    for( int i = 0; i < nodes.getLength(); i++ ) {
      Node node = nodes.item( i );
      String nodeName = node.getNodeName().toLowerCase();
      if( nodeName.equals( "file" ) ) {
        processFileNode( node );
      }
    }
  }
  
  private void processFileNode( final Node node ) {
    try {
      NamedNodeMap attributes = node.getAttributes();
      this.fileName = attributes.getNamedItem( "name" ).getNodeValue();
      this.fileSize = attributes.getNamedItem( "size" ).getNodeValue();
      this.checksum = attributes.getNamedItem( "checksum" ).getNodeValue();
    } catch( DOMException e ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

  public static String computeHeader( final File fileToSend ) {
    StringBuffer result = new StringBuffer();
    String name = fileToSend.getName();
    long fileSize = fileToSend.length();
    String checksum = Util.createChecksum( fileToSend );
    result.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" );
    result.append( "<peerdepot>" );
    result.append( "<file" );
    result.append( " name=\"" + name + "\"" );
    result.append( " checksum=\"" + checksum + "\"" );
    result.append( " size=\"" + fileSize + "\"" );
    result.append( " />" );
    result.append( "</peerdepot>" );
    return result.toString();
  }

}
