package net.unto.twitter;

import org.joda.time.DateTime;

/**
 * Instances of the Api class provide access to the Twitter web service.
 * 
 * @author DeWitt Clinton <dewitt@unto.net>
 */
public class Api {

  private TwitterHttpManager twitterHttpManager = null;

  /**
   * Construct a new Api instance
   */
  public Api() {
  }

  /**
   * Construct a new Api instance with login credentials
   * 
   * @param username the Twitter username
   * @param password the Twitter password
   */
  public Api(String username, String password) {
    setCredentials(username, password);
  }

  /**
   * Returns the 20 most recent statuses from non-protected users who have set a
   * custom user icon. Does not require authentication.
   * 
   * @return an array of {@link Status} instances
   * @throws TwitterException
   */
  public Status[] getPublicTimeline() throws TwitterException {
    return getPublicTimeline(null);
  }

  /**
   * Returns the 20 most recent statuses from non-protected users who have set a
   * custom user icon. Does not require authentication.
   * 
   * @param sinceId Optional. Returns only public statuses with an ID greater
   *        than (that is, more recent than) the specified ID.
   * @return an array of {@link Status} instances
   * @throws TwitterException
   */
  public Status[] getPublicTimeline(Long sinceId) throws TwitterException {
    String url = "http://twitter.com/statuses/public_timeline.json";
    Parameter[] parameters = {new Parameter("since_id", sinceId)};
    String response = getTwitterHttpManager().get(url, parameters);
    return Status.newArrayFromJsonString(response);
  }

  /**
   * Returns the 20 most recent statuses posted in the last 24 hours from the
   * authenticating user and that user's friends.
   * 
   * @return an array of {@link Status} instances
   * @throws TwitterException
   */
  public Status[] getFriendsTimeline() throws TwitterException {
    return getFriendsTimeline(null, null, null);
  }

  /**
   * Returns the 20 most recent statuses posted in the last 24 hours from the
   * authenticating user and that user's friends.
   * 
   * @param id Optional. Specifies the ID or screen name of the user for whom to
   *        return the friends_timeline. Requires credentials if the id is not
   *        set, or if the id is private.
   * @return an array of {@link Status} instances
   * @throws TwitterException
   */
  public Status[] getFriendsTimeline(String id) throws TwitterException {
    return getFriendsTimeline(id, null, null);
  }

  /**
   * Returns the 20 most recent statuses posted in the last 24 hours from the
   * authenticating user and that user's friends.
   * 
   * @param id Optional. Specifies the ID or screen name of the user for whom to
   *        return the friends_timeline. Requires credentials if the id is not
   *        set, or if the id is private.
   * @param since Optional. Narrows the returned results to just those statuses
   *        created after the specified HTTP-formatted date.
   * @param page Optional. Gets the 20 next most recent statuses from the
   *        authenticating user and that user's friends.
   * @return an array of {@link Status} instances
   * @throws TwitterException
   */
  public Status[] getFriendsTimeline(String id, DateTime since, Integer page)
      throws TwitterException {
    String url;
    if (id == null) {
      requireCredentials();
      url = "http://twitter.com/statuses/friends_timeline.json";
    } else {
      url = String.format(
          "http://twitter.com/statuses/friends_timeline/%s.json", id);
    }
    Parameter[] parameters = {
        new Parameter("since", since), new Parameter("page", page)};
    String response = getTwitterHttpManager().get(url, parameters);
    return Status.newArrayFromJsonString(response);
  }

  /**
   * Returns the 20 most recent statuses posted in the last 24 hours from the
   * authenticating user.
   * 
   * @return an array of {@link Status} instances
   * @throws TwitterException
   */
  public Status[] getUserTimeline() throws TwitterException {
    return getUserTimeline(null, null, null, null);
  }

  /**
   * Returns the 20 most recent statuses posted in the last 24 hours from the
   * authenticating user.
   * 
   * @param id Optional. Specifies the ID or screen name of the user for whom to
   *        return the friends_timeline.
   * @param count Optional. Specifies the number of statuses to retrieve. May
   *        not be greater than 20 for performance purposes.
   * @param since Optional. Narrows the returned results to just those statuses
   *        created after the specified HTTP-formatted date.
   * @param page Optional. Retrieves the 20 next most recent direct messages
   * @return an array of {@link Status} instances
   * @throws TwitterException
   */
  public Status[] getUserTimeline(String id, Integer count, DateTime since,
      Integer page) throws TwitterException {
    requireCredentials();
    String url;
    if (id == null) {
      url = "http://twitter.com/statuses/user_timeline.json";
    } else {
      url = String.format("http://twitter.com/statuses/user_timeline/%s.json",
          id);
    }
    Parameter[] parameters = {
        new Parameter("since", since), new Parameter("count", count),
        new Parameter("page", page)};
    String response = getTwitterHttpManager().get(url, parameters);
    return Status.newArrayFromJsonString(response);
  }

  /**
   * Returns a single status, specified by the id parameter below. The status's
   * author will be returned inline.
   * 
   * @param id Required. The numerical ID of the status you're trying to
   *        retrieve.
   * @return a {@link Status} instance
   * @throws TwitterException
   */
  public Status showStatus(long id) throws TwitterException {
    String url = String.format("http://twitter.com/statuses/show/%d.json", id);
    String response = getTwitterHttpManager().get(url);
    return Status.newFromJsonString(response);
  }

  /**
   * Updates the authenticating user's status.
   * 
   * @param status Required. The text of your status update. Must not be more
   *        than 160 characters and should not be more than 140 characters to
   *        ensure optimal display.
   * @return a {@link Status} instance
   * @throws TwitterException
   */
  public Status updateStatus(String status) throws TwitterException {
    assert (status != null);
    requireCredentials();
    String url = "http://twitter.com/statuses/update.json";
    Parameter[] parameters = {new Parameter("status", status)};
    String response = getTwitterHttpManager().post(url, parameters);
    return Status.newFromJsonString(response);
  }

  /**
   * Returns the 20 most recent replies (status updates prefixed with
   * 
   * @username posted by users who are friends with the user being replied to)
   *           to the authenticating user. Replies are only available to the
   *           authenticating user; you can not request a list of replies to
   *           another user whether public or protected.
   * 
   * @return an array of {@link Status} instances
   * @throws TwitterException
   */
  public Status[] getReplies() throws TwitterException {
    return getReplies(null);
  }

  /**
   * Returns the 20 most recent replies (status updates prefixed with
   * 
   * @username posted by users who are followers with the user being replied to)
   *           to the authenticating user. Replies are only available to the
   *           authenticating user; you can not request a list of replies to
   *           another user whether public or protected.
   * 
   * @param page Optional. Retrieves the 20 next most recent replies.
   * @return an array of {@link Status} instances
   * @throws TwitterException
   */
  public Status[] getReplies(Integer page) throws TwitterException {
    requireCredentials();
    String url = "http://twitter.com/statuses/replies.json";
    Parameter[] parameters = {new Parameter("page", page)};
    String response = getTwitterHttpManager().get(url, parameters);
    return Status.newArrayFromJsonString(response);
  }

  /**
   * Destroys the status specified by the required ID parameter. The
   * authenticating user must be the author of the specified status.
   * 
   * @param id Required. The ID of the status to destroy.
   * @return a {@link Status} instance
   * @throws TwitterException
   */
  public Status destroyStatus(long id) throws TwitterException {
    requireCredentials();
    String url = String.format("http://twitter.com/statuses/destroy/%d.json",
        id);
    String response = getTwitterHttpManager().post(url);
    return Status.newFromJsonString(response);
  }

  /**
   * Returns up to 100 of the people the authenticating user follows who have
   * most recently updated, each with current status inline.
   * 
   * @return An array of {@link User} instances
   * @throws TwitterException
   */
  public User[] getFollowing() throws TwitterException {
    return getFollowing(null);
  }

  /**
   * Returns up to 100 of the people the authenticating user follows who have
   * most recently updated, each with current status inline.
   * 
   * @param id Optional. The ID or screen name of the user for whom to request a
   *        list of people they follow
   * @return An array of {@link User} instances
   * @throws TwitterException
   */
  public User[] getFollowing(String id) throws TwitterException {
    requireCredentials();
    String url;
    if (id == null) {
      url = "http://twitter.com/statuses/friends.json";
    } else {
      url = String.format("http://twitter.com/statuses/friends/%s.json", id);
    }
    String response = getTwitterHttpManager().get(url);
    return User.newArrayFromJsonString(response);
  }

  /**
   * Returns the authenticating user's followers, each with current status
   * inline.
   * 
   * @return An array of {@link User} instances
   * @throws TwitterException
   */
  public User[] getFollowers() throws TwitterException {
    return getFollowers(false);
  }

  /**
   * Returns the authenticating user's followers, each with current status
   * inline.
   * 
   * @param lite Optional. Prevents the inline inclusion of current status.
   * @return An array of {@link User} instances
   * @throws TwitterException
   */
  public User[] getFollowers(Boolean lite) throws TwitterException {
    requireCredentials();
    String url = "http://twitter.com/statuses/followers.json";
    Parameter[] parameters = {new Parameter("lite", lite)};
    String response = getTwitterHttpManager().get(url, parameters);
    return User.newArrayFromJsonString(response);
  }

  /**
   * Returns a list of the users currently featured on the site with their
   * current statuses inline.
   * 
   * @return An array of {@link User} instances
   * @throws TwitterException
   */
  public User[] getFeatured() throws TwitterException {
    String url = "http://twitter.com/statuses/featured.json";
    String response = getTwitterHttpManager().get(url);
    return User.newArrayFromJsonString(response);
  }

  /**
   * Returns extended information of a given user, specified by ID or screen
   * name as per the required id parameter below.
   * 
   * @param id Required. The ID or screen name of a user.
   * @return A {@link User} instance
   * @throws TwitterException
   */
  public User showUser(String id) throws TwitterException {
    assert (id != null);
    String url = String.format("http://twitter.com/users/show/%s.json", id);
    String response = getTwitterHttpManager().get(url);
    return User.newFromJsonString(response);
  }

  /**
   * Returns a list of the 20 most recent direct messages sent to the
   * authenticating user.
   * 
   * @return An array of {@link DirectMessage} instances
   * @throws TwitterException
   */
  public DirectMessage[] getDirectMessages() throws TwitterException {
    return getDirectMessages(null, null, null);
  }

  /**
   * Returns a list of the 20 most recent direct messages sent to the
   * authenticating user.
   * 
   * @param since Optional. Narrows the resulting list of direct messages to
   *        just those sent after the specified HTTP-formatted date.
   * @param sinceId Optional. Returns only direct messages with an ID greater
   *        than (that is, more recent than) the specified ID.
   * @param page Optional. Retrieves the 20 next most recent direct messages.
   * @return An array of {@link DirectMessage} instances
   * @throws TwitterException
   */
  public DirectMessage[] getDirectMessages(DateTime since, String sinceId,
      Integer page) throws TwitterException {
    requireCredentials();
    String url = "http://twitter.com/direct_messages.json";
    Parameter[] parameters = {
        new Parameter("since", since), new Parameter("since_id", sinceId),
        new Parameter("page", page)};
    String response = getTwitterHttpManager().get(url, parameters);
    return DirectMessage.newArrayFromJsonString(response);
  }

  /**
   * Returns a list of the 20 most recent direct messages sent by the
   * authenticating user.
   * 
   * @throws TwitterException
   */
  public DirectMessage[] getSentDirectMessages() throws TwitterException {
    return getSentDirectMessages(null, null, null);
  }

  /**
   * Returns a list of the 20 most recent direct messages sent by the
   * authenticating user.
   * 
   * @param since Optional. Narrows the resulting list of direct messages to
   *        just those sent after the specified HTTP-formatted date.
   * @param sinceId Optional. Returns only sent direct messages with an ID
   *        greater than (that is, more recent than) the specified ID.
   * @param page Optional. Retrieves the 20 next most recent direct messages
   *        sent.
   * @return An array of {@link DirectMessage} instances
   * @throws TwitterException
   */
  public DirectMessage[] getSentDirectMessages(DateTime since, String sinceId,
      Integer page) throws TwitterException {
    requireCredentials();
    String url = "http://twitter.com/direct_messages/sent.json";
    Parameter[] parameters = {
        new Parameter("since", since), new Parameter("since_id", sinceId),
        new Parameter("page", page)};
    String response = getTwitterHttpManager().get(url, parameters);
    return DirectMessage.newArrayFromJsonString(response);
  }

  /**
   * Sends a new direct message to the specified user from the authenticating
   * user.
   * 
   * @param user Required. The ID or screen name of the recipient user.
   * @param text Required. The text of your direct message.
   * @return A {@link DirectMessage} instance
   */
  public DirectMessage newDirectMessage(String user, String text)
      throws TwitterException {
    assert (user != null);
    assert (text != null);
    requireCredentials();
    String url = "http://twitter.com/direct_messages/new.json";
    Parameter[] parameters = {
        new Parameter("user", user), new Parameter("text", text)};
    String response = getTwitterHttpManager().post(url, parameters);
    return DirectMessage.newFromJsonString(response);
  }


  /**
   * Destroys the direct message specified in the required ID parameter. The
   * authenticating user must be the recipient of the specified direct message.
   * 
   * @param id Required. The ID of the direct message to destroy.
   * @return A {@link DirectMessage} instance
   * @throws TwitterException
   */
  public DirectMessage destroyDirectMessage(long id) throws TwitterException {
    requireCredentials();
    String url = String.format(
        "http://twitter.com/direct_messages/destroy/%d.json", id);
    String response = getTwitterHttpManager().post(url);
    return DirectMessage.newFromJsonString(response);
  }


  /**
   * Befriends the user specified in the ID parameter as the authenticating
   * user.
   * 
   * This method is probably equivalent to the newer <code>follow</code> api
   * method, but it is unclear in the documentation.
   * 
   * @param id Required. The ID or screen name of the user to befriend.
   * @return A {@link User} instance representing the befriended user
   * @throws TwitterException
   */
  public User createFriendship(String id) throws TwitterException {
    assert (id != null);
    requireCredentials();
    String url = String.format("http://twitter.com/friendships/create/%s.json",
        id);
    String response = getTwitterHttpManager().post(url);
    return User.newFromJsonString(response);
  }

  /**
   * Discontinues friendship with the user specified in the ID parameter as the
   * authenticating user.
   * 
   * This method is probably equivalent to the newer <code>leave</code> api
   * method, but it is unclear in the documentation.
   * 
   * @param id Required. The ID or screen name of the user with whom to
   *        discontinue friendship.
   * @return A {@link User} instance
   * @throws TwitterException
   */
  public User destroyFriendship(String id) throws TwitterException {
    assert (id != null);
    requireCredentials();
    String url = String.format(
        "http://twitter.com/friendships/destroy/%s.json", id);
    String response = getTwitterHttpManager().post(url);
    return User.newFromJsonString(response);
  }

  /**
   * Returns the 20 most recent favorite statuses for the authenticating user or
   * user specified by the ID parameter.
   * 
   * @return an array of {@link Status} instances
   */
  public Status[] getFavorites() throws TwitterException {
    return getFavorites(null, null);
  }

  /**
   * Returns the 20 most recent favorite statuses for the authenticating user or
   * user specified by the ID parameter.
   * 
   * @param id Optional. The ID or screen name of the user for whom to request a
   *        list of favorite statuses.
   * @param page Optional. Retrieves the 20 next most recent favorite statuses.
   * @return an array of {@link Status} instances
   */
  public Status[] getFavorites(String id, Integer page) throws TwitterException {
    String url;
    if (id == null) {
      url = "http://twitter.com/favorites.json";
    } else {
      url = String.format("http://twitter.com/favorites/%s.json", id);
    }
    Parameter[] parameters = {new Parameter("page", page)};
    String response = getTwitterHttpManager().get(url, parameters);
    return Status.newArrayFromJsonString(response);
  }

  /**
   * Favorites the status specified in the ID parameter as the authenticating
   * user.
   * 
   * @param id Required. The ID of the status to favorite.
   * @return a {@link Status} instance
   * @throws TwitterException
   */
  public Status createFavorite(long id) throws TwitterException {
    requireCredentials();
    String url = String.format("http://twitter.com/favorites/create/%d.json",
        id);
    String response = getTwitterHttpManager().post(url);
    return Status.newFromJsonString(response);
  }

  /**
   * Un-favorites the status specified in the ID parameter as the authenticating
   * user.
   * 
   * @param id Required. The ID of the status to un-favorite.
   * @return a {@link Status} instance
   * @throws TwitterException
   */
  public Status destroyFavorite(long id) throws TwitterException {
    requireCredentials();
    String url = String.format("http://twitter.com/favorites/destroy/%d.json",
        id);
    String response = getTwitterHttpManager().post(url);
    return Status.newFromJsonString(response);
  }

  /**
   * Enables notifications for updates from the specified user to the
   * authenticating user.
   * 
   * @param id Required. The ID or screen name of the user to follow.
   * @return A {@link User} instance representing the specified user
   * @throws TwitterException
   */
  public User follow(String id) throws TwitterException {
    assert (id != null);
    requireCredentials();
    String url = String.format(
        "http://twitter.com/notifications/follow/%s.json", id);
    String response = getTwitterHttpManager().post(url);
    return User.newFromJsonString(response);
  }

  /**
   * Disables notifications for updates from the specified user to the
   * authenticating user.
   * 
   * @param id Required. The ID or screen name of the user to leave.
   * @return A {@link User} instance representing the specified user
   * @throws TwitterException
   */
  public User leave(String id) throws TwitterException {
    assert (id != null);
    requireCredentials();
    String url = String.format(
        "http://twitter.com/notifications/leave/%s.json", id);
    String response = getTwitterHttpManager().post(url);
    return User.newFromJsonString(response);
  }


  /**
   * Use the specified username and password to authenticate as a user.
   * 
   * @param username the Twitter username
   * @param password the Twitter password
   */
  public void setCredentials(String username, String password) {
    getTwitterHttpManager().setCredentials(username, password);
  }

  /**
   * Clear the stored username and password.
   */
  public void clearCredentials() {
    getTwitterHttpManager().clearCredentials();
  }

  /**
   * Throw an exception if setCredentials(username, password) has not been
   * called first.
   * 
   * @throws TwitterException
   */
  private void requireCredentials() throws TwitterException {
    if (!getTwitterHttpManager().hasCredentials()) {
      throw new TwitterException(
          "Authentication required.  Please call api.setCredentials first.");
    }
  }

  /**
   * Get an instance of a TwitterHttpManager. Instantiates a new
   * SimpleTwitterHttpManager if none is set.
   * 
   * @return an instance of a TwitterHttpManager.
   */
  private TwitterHttpManager getTwitterHttpManager() {
    if (twitterHttpManager == null) {
      twitterHttpManager = new SimpleTwitterHttpManager();
    }
    return twitterHttpManager;
  }

  /**
   * Set the private instance of the TwitterHttpManager.
   * 
   * @param twitterHttpManager the TwitterHttpManager for this client to use.
   */
  protected void setTwitterHttpManager(TwitterHttpManager twitterHttpManager) {
    this.twitterHttpManager = twitterHttpManager;
  }
}
