package net.unto.twitter;

/**
 * An exception wrapper for the Twitter API library.
 * 
 * @author DeWitt Clinton <dewitt@unto.net>
 */
public class TwitterException extends Exception {

  private static final long serialVersionUID = -7004865779218982263L;

  /**
   * Construct a new TwitterException
   * 
   * @param string the error message
   */
  public TwitterException(String string) {
    super(string);
  }

  /**
   * Construct a new TwitterException.
   * 
   * @param e the existing exception to wrap
   */
  public TwitterException(Exception e) {
    super(e);
  }

}
