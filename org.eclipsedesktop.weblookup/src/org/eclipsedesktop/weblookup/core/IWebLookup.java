// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.weblookup.core;

import java.net.MalformedURLException;
import java.net.URL;

/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (jboehme@innoopract.de)
 */
public interface IWebLookup {

  URL getSearchURL( String searchString ) throws MalformedURLException;
}