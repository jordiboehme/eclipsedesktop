package net.unto.twitter;

/**
 * A generic HTTP connection interface.
 * 
 * @author DeWitt Clinton <dewitt@unto.net>
 */
interface TwitterHttpManager
{
  /**
   * Set the HTTP basic auth credentials for subsequent requests.
   * 
   * @param username the HTTP basic auth username.
   * @param password the HTTP basic auth password.
   */
  void setCredentials(String username, String password);
  
  /**
   * Returns true if the HTTP basic auth credentials have been set.
   * 
   * @return true if the HTTP basic auth credentials have been set
   */
  boolean hasCredentials();
  
  /**
   * Clear the HTTP basic auth credentials if they have been set.
   */
  void clearCredentials();
  
  /**
   * Perform an HTTP GET request to the specified URL.
   * 
   * @param url the URL to HTTP GET.
   * @return a String containing the body of the HTTP GET response.
   * @throws TwitterException
   */
  String get(String url)  throws TwitterException;
  
  /**
   * Perform an HTTP GET request to the specified URL.
   * 
   * @param url the URL to HTTP GET.
   * @param parameters the key/value pairs to add to the HTTP GET query string 
   * @return a String containing the body of the HTTP GET response.
   * @throws TwitterException
   */
  String get(String url, Parameter[] parameters) throws TwitterException;
  
  /**
   * Perform an HTTP POST request to the specified URL.
   * 
   * @param url the URL to HTTP POST.
   * @return a String containing the body of the HTTP POST response.
   * @throws TwitterException
   */
  String post(String url)  throws TwitterException;
  
  /**
   * Perform an HTTP POST request to the specified URL.
   * 
   * @param url the URL to HTTP POST.
   * @param parameters the key/value pairs to add to the HTTP POST query string 
   * @return a String containing the body of the HTTP POST response.
   * @throws TwitterException
   */
  String post(String url, Parameter[] parameters)  throws TwitterException;
}
