// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.weblookup.internal.core.lookups;

import java.net.MalformedURLException;
import java.net.URL;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.weblookup.core.IWebLookup;


/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (jboehme@innoopract.de)
 */
public class SelfHtmlWebLookup implements IWebLookup {

  public URL getSearchURL( final String searchString ) 
                                                  throws MalformedURLException {
    String encodedSearchString = Util.doURLEncode( searchString, "" );
    return new URL(   "http://suche.de.selfhtml.org/cgi-bin/such.pl?suchausdruck="
                      + encodedSearchString
                    + "&umlaute=on&feld=alle&index_1=on&index_2=on&index_4=on&hits=1000#SELFHTML%20V8.0" );
  }
}
