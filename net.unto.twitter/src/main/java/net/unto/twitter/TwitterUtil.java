package net.unto.twitter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

final class TwitterUtil
{
  public final static DateTimeFormatter TWITTER_DATE_FORMATTER = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss Z yyyy");
  
  public final static DateTime parseTwitterDateTimeString(String twitterDateString) {
    try {
      return TWITTER_DATE_FORMATTER.parseDateTime(twitterDateString);
    } catch (IllegalArgumentException e) {
      System.err.println(String.format("Could not parse date string '%s'", twitterDateString));
      return null;
    }
  }
}
