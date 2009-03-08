package org.eclipsedesktop.beam.core;



public class TransferStatus {

  private static final int SIZE_GB = 1073741824;
  private static final int SIZE_MB = 1048576;
  private static final int SIZE_KB = 1024;
  private long start;
  private long total;
  private long worked = 0;

  public TransferStatus( final long total ) {
    this.start = System.currentTimeMillis() - 1;
    this.total = total;
  }
  
  public void worked( final long worked ) {
    this.worked += worked;
  }
  
  public String toString() {
    long now = System.currentTimeMillis();
    String result =   format( worked ) 
                    + " of "
                    + format( total )
                    + " @ "
                    + computeThroughput( now )
                    + "/sec";
    return result;
  }

  private String computeThroughput( final long now ) {
    long result = 0;
    long spent = ( now - this.start ) / 1000;
    result = ( spent > 0 ) ? ( worked / spent ) : worked;
    return format( result );
  }

  private String format( final float value ) {
    String result = "";
    if( value > SIZE_GB ) {
      result = Math.round( value / SIZE_GB ) + "GB";
    } else if( value > SIZE_MB ) {
      result = Math.round( value / SIZE_MB ) + "MB";
    } else if( value > SIZE_KB ) {
      result = Math.round( value / SIZE_KB ) + "KB";
    }
    return result;
  }
}
