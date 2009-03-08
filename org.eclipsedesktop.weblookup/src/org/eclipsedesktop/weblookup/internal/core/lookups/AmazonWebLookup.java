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
public class AmazonWebLookup implements IWebLookup {

  public URL getSearchURL( final String searchString ) 
                                                  throws MalformedURLException {
    String encodedSearchString = Util.doURLEncode( searchString, "UTF-8" );
    return new URL(   "http://www.amazon.de/exec/obidos/external-search?field-keywords="
                    + encodedSearchString 
                    + "&mode=blended");
  }
}
