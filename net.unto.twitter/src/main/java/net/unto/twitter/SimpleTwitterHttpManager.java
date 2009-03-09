package net.unto.twitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * An implementation of the TwitterHttpManager interface using the Apache Commons HttpClient library.
 * 
 * @author DeWitt Clinton <dewitt@unto.net>
 */
class SimpleTwitterHttpManager implements TwitterHttpManager
{
  private final AuthScope AUTH_SCOPE = new AuthScope("twitter.com", 80, AuthScope.ANY_REALM);
  
  private Credentials credentials = null;
 
  private HttpConnectionManager manager = null;
  
  /**
   * Construct a new {@link SimpleTwitterHttpManager} instance.
   */
  public SimpleTwitterHttpManager() { }

  private HttpConnectionManager getHttpConnectionManager() {
    if (this.manager == null) {
      this.manager = new MultiThreadedHttpConnectionManager();
    }
    return this.manager;
  }
  
  protected void setHttpConnectionManager(HttpConnectionManager manager) {
    this.manager = manager;
  }
  
  /* (non-Javadoc)
   * @see net.unto.twitter.TwitterHttpManager#get(java.lang.String)
   */
  public String get( String url ) throws TwitterException {
    return get(url, null);
  }

  /* (non-Javadoc)
   * @see net.unto.twitter.TwitterHttpManager#get(java.lang.String, net.unto.twitter.Parameter[])
   */
  public String get(String url, Parameter[] parameters) throws TwitterException {
    return execute(new GetMethod(url), parameters);
  }

  /* (non-Javadoc)
   * @see net.unto.twitter.TwitterHttpManager#post(java.lang.String)
   */
  public String post(String url) throws TwitterException {
    return post(url, null);
  }

  /* (non-Javadoc)
   * @see net.unto.twitter.TwitterHttpManager#post(java.lang.String, net.unto.twitter.Parameter[])
   */
  public String post(String url, Parameter[] parameters) throws TwitterException {
    return execute(new PostMethod(url), parameters);
  }

  /* (non-Javadoc)
   * @see net.unto.twitter.TwitterHttpManager#setCredentials(java.lang.String, java.lang.String)
   */
  public void setCredentials(String username, String password) {
    assert(username != null);
    assert(password != null);
    setCredentials(new UsernamePasswordCredentials(username, password));
  }

  private void setCredentials(Credentials credentials) {
     this.credentials = credentials;
  }
  
  /* (non-Javadoc)
   * @see net.unto.twitter.TwitterHttpManager#clearCredentials()
   */
  public void clearCredentials( ) {
    this.credentials = null;
  }

  /* (non-Javadoc)
   * @see net.unto.twitter.TwitterHttpManager#hasCredentials()
   */
  public boolean hasCredentials( ) {
    return credentials != null;
  }
  
  private Credentials getCredentials() {
    return this.credentials;
  }

  private NameValuePair[] toNameValuePairArray(Parameter[] in) {
    if (in == null) {
      return new NameValuePair[] { };
    }
    List<NameValuePair> out = new ArrayList<NameValuePair>();
    for (Parameter parameter : in) {
      if (parameter.hasName() && parameter.hasValue()) {
        out.add(new NameValuePair(parameter.getName(), parameter.getValue().toString()));
      }
    }
    return (NameValuePair[])out.toArray(new NameValuePair[out.size()]);
  }
  
  private String execute(HttpMethod method, Parameter[] parameters) throws TwitterException {
    method.getParams( ).setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
    method.setQueryString(toNameValuePairArray(parameters));
    HttpClient httpClient = new HttpClient(getHttpConnectionManager());
    if (hasCredentials()) {
      httpClient.getState( ).setCredentials(AUTH_SCOPE, getCredentials());
      httpClient.getParams().setAuthenticationPreemptive(true);
    } else {
      httpClient.getParams().setAuthenticationPreemptive(false);
    }
    try {
      int statusCode = httpClient.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK) {
        String error = String.format("Expected 200 OK. Received %d %s", statusCode, HttpStatus.getStatusText(statusCode));
        throw new TwitterException(error);
      }
      String responseBody = method.getResponseBodyAsString();
      if (responseBody == null) {
        throw new TwitterException("Expected response body, got null");
      }
      return responseBody;
    } catch (HttpException e) {
      throw new TwitterException(e);
    } catch (IOException e) {
      throw new TwitterException(e);
    } finally {
      method.releaseConnection();
    }
  }
}
