// Copyright (c) 2004 by eclipsedesktop.org
// Leif Frenzel (himself@leiffrenzel.de)
// Jordi Boehme Lopez (mail@jordi-boehme.de)
package org.eclipsedesktop.eclipsesetimon.core;

/**
 * @author Jordi Boehme Lopez
 */
public interface IStatusFile {

    void setProgress(int progress);

    void setElapsed(String elapsed);

    void setEstimated(String estimated);

    int getProgress();

    String getElapsed();

    String getEstimated();

}