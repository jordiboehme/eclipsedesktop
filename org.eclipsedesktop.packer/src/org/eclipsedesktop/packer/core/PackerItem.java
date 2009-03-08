package org.eclipsedesktop.packer.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class PackerItem {

  private boolean isDirectory;
  private String name;
  private long compressedSize;
  private long size;
  private long time;
  private Image image;
  private String fileName;
  private String path;
  private int compression;
  private String sTime;
  private String sSize;
  private String sCompressedSize;
  private String sCompression;


  public PackerItem( final boolean isDirectory,
                     final String name,
                     final long compressedSize,
                     final long size,
                     final long time )
  {
    this.isDirectory = isDirectory;
    this.name = name;
    this.compressedSize = compressedSize;
    this.size = size;
    this.time = time;
    this.compression = Integer.MIN_VALUE;
  }

  public PackerItem( final ZipEntry zipEntry ) {
    this.isDirectory = zipEntry.isDirectory();
    this.name = zipEntry.getName();
    this.compressedSize = zipEntry.getCompressedSize();
    this.size = zipEntry.getSize();
    this.time = zipEntry.getTime();
    this.compression = Integer.MIN_VALUE;
  }
  
  public boolean isDirectory() {
    return isDirectory;
  }

  public Image getImage() {
    if( this.image == null ) {
      this.image = createImage( this );
    }
    return this.image;
  }
  
  public String getFileName() {
    if( fileName == null ) {
      if( this.isDirectory() ) {
        fileName = new Path( this.name ).lastSegment();
      } else {
        fileName = new Path( this.name ).lastSegment();
      }
    }
    return fileName;
  }

  public String getPath() {
    if( path == null ) {
      if( this.isDirectory() ) {
        path = new Path( this.name ).removeTrailingSeparator().toPortableString();
      } else {
        path = new Path( this.name ).removeLastSegments( 1 ).removeTrailingSeparator().toPortableString();
      }
    }
    return path;
  }

  public String getFullPath() {
    return this.name;
  }
  
  public int getCompression() {
    if( compression == Integer.MIN_VALUE ) {
      float compRel = ( float )this.compressedSize / ( float )this.size;
      compression = Math.round( 100 - ( compRel * 100 ) );
    }
    return compression;
  }

  public String getCompressionStr() {
    if( sCompression == null ) {
      sCompression = Integer.toString( this.getCompression() ) + "%";
    }
    return sCompression;
  }

  public long getTime() {
    return this.time;
  }

  public String getTimeStr() {
    if( sTime == null ) {
      Date date = new Date( this.getTime() );
      SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      sTime = sdf.format( date );
    }
    return sTime;
  }

  public long getSize() {
    return this.size;
  }

  public String getSizeStr() {
    if( sSize == null ) {
      sSize = Long.toString( this.getSize() );
    }
    return sSize;
  }

  public long getCompressedSize() {
    return this.compressedSize;
  }

  public String getCompressedSizeStr() {
    if( sCompressedSize == null ) {
      sCompressedSize = Long.toString( this.getCompressedSize() );
    }
    return sCompressedSize;
  }

  public static Image createImage( final PackerItem item ) {
    Image result = null;
    IWorkbench workbench = PlatformUI.getWorkbench();
    if( item.isDirectory() ) {
      result = workbench.getSharedImages().getImage( ISharedImages.IMG_OBJ_FOLDER );
    } else {
      ImageDescriptor descriptor = workbench.getEditorRegistry().getImageDescriptor( item.getFileName() );
      if( descriptor == null ) {
        result = workbench.getSharedImages().getImage( ISharedImages.IMG_OBJ_FILE );
      } else {
        result = descriptor.createImage();
      }
      if( result == null ) {
        result = workbench.getSharedImages().getImage( ISharedImages.IMG_OBJ_FILE );
      }
    }
    return result;
  }
  
}
