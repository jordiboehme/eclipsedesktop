//Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.examem.core;


public class Memory {

  public static int getUsedMemory() {
    return getTotalMemory() - getFreeMemory();
  }
  
  public static int getFreeMemory() {
    return ( int )Runtime.getRuntime().freeMemory();
  }
  
  public static int getTotalMemory() {
    return ( int )Runtime.getRuntime().totalMemory();
  }
  
  public static int getMaxMemory() {
    return ( int )Runtime.getRuntime().maxMemory();
  }
  
  public static int getAvailableMemory() {
    return getMaxMemory() - getTotalMemory() + getFreeMemory();
  }
  
  public static void clearMemory() {
    System.gc();
    //System.runFinalization();
  }
}