package net.unto.twitter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Represents private messages passed directly between Twitter users.
 * </p>
 * 
 * <p>
 * Instances of DirectMessage are created automatically via the Api. End users
 * are not expected to construct instances of DirectMessage.
 * </p>
 * 
 * <p>
 * This class exposes the following properties:
 * </p>
 * 
 * <ul>
 * <li>getSenderScreenName()</li>
 * <li>getRecipientId()</li>
 * <li>getSender()</li>
 * <li>getCreatedAt()</li>
 * <li>getRecipientScreenName()</li>
 * <li>getRecipient()</li>
 * <li>getText()</li>
 * <li>getSenderId()</li>
 * <li>getId()</li>
 * </ul>
 * 
 * @author DeWitt Clinton <dewitt@unto.net>
 */
public class DirectMessage implements Serializable {

  private static final long serialVersionUID = 1L;

  protected DirectMessage() {
  }

  private String senderScreenName;

  private Long recipientId;

  private User sender;

  private DateTime createdAt;

  private String recipientScreenName;

  private User recipient;

  private String text;

  private Long senderId;

  private Long id;

  /**
   * Return the screen name of the sender of this direct message.
   * 
   * @return the screen name of the sender of this direct message.
   */
  public String getSenderScreenName() {
    return senderScreenName;
  }

  protected void setSenderScreenName(String senderScreenName) {
    this.senderScreenName = senderScreenName;
  }

  /**
   * Return true of the screen name of the sender of this direct message has
   * been set.
   * 
   * @return true of the screen name of the sender of this direct message has
   *         been set.
   */
  public boolean hasSenderScreenName() {
    return senderScreenName != null;
  }

  /**
   * Return the user id of the recipient of this direct message.
   * 
   * @return the user id of the recipient of this direct message.
   */
  public Long getRecipientId() {
    return recipientId;
  }

  protected void setRecipientId(Long recipientId) {
    this.recipientId = recipientId;
  }

  /**
   * Return true if the recipient id of this direct message has been set.
   * 
   * @return true if the recipient id of this direct message has been set.
   */
  public boolean hasRecipientId() {
    return recipientId != null;
  }

  /**
   * Return the {@link User} instance representing the sender of this direct message.
   * 
   * @return the {@link User} instance representing the sender of this direct message.
   */
  public User getSender() {
    return sender;
  }

  protected void setSender(User sender) {
    this.sender = sender;
  }

  /**
   * Return true if the {@link User} instance representing the sender of this direct message has been set.
   *  
   * @return true if the {@link User} instance representing the sender of this direct message has been set.
   */
  public boolean hasSender() {
    return sender != null;
  }

  /**
   * Return the {@link DateTime} instance representing the time at which this direct message was sent.
   * 
   * @return the {@link DateTime} instance representing the time at which this direct message was sent.
   */
  public DateTime getCreatedAt() {
    return createdAt;
  }

  protected void setCreatedAt(DateTime createdAt) {
    this.createdAt = createdAt;
  }

  protected void setCreatedAt(String createdAtString) {
    setCreatedAt(TwitterUtil.parseTwitterDateTimeString(createdAtString));
  }

  public boolean hasCreatedAt() {
    return createdAt != null;
  }

  public String getRecipientScreenName() {
    return recipientScreenName;
  }

  protected void setRecipientScreenName(String recipientScreenName) {
    this.recipientScreenName = recipientScreenName;
  }

  public boolean hasRecipientScreenName() {
    return recipientScreenName != null;
  }

  public User getRecipient() {
    return recipient;
  }

  public void setRecipient(User recipient) {
    this.recipient = recipient;
  }

  public boolean hasRecipient() {
    return recipient != null;
  }

  public String getText() {
    return text;
  }

  protected void setText(String text) {
    this.text = text;
  }

  public boolean hasText() {
    return text != null;
  }

  public Long getSenderId() {
    return senderId;
  }

  protected void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  public boolean hasSenderId() {
    return senderId != null;
  }

  public Long getId() {
    return id;
  }

  protected void setId(Long id) {
    this.id = id;
  }

  public boolean hasId() {
    return id != null;
  }

  protected final static DirectMessage[] newArrayFromJsonString(
      String jsonString) throws TwitterException {
    return newArrayFromJsonArray(JSONArray.fromObject(jsonString));
  }

  protected final static DirectMessage[] newArrayFromJsonArray(
      JSONArray jsonArray) throws TwitterException {
    List<DirectMessage> directMessageList = new ArrayList<DirectMessage>();
    for (int i = 0; i < jsonArray.size(); i++) {
      directMessageList.add(newFromJsonObject(jsonArray.getJSONObject(i)));
    }
    return directMessageList
        .toArray(new DirectMessage[directMessageList.size()]);
  }

  protected final static DirectMessage newFromJsonString(String jsonString)
      throws TwitterException {
    return newFromJsonObject(JSONObject.fromObject(jsonString));
  }

  protected final static DirectMessage newFromJsonObject(JSONObject jsonObject)
      throws TwitterException {
    if (jsonObject == null) {
      return null;
    }
    DirectMessage directMessage = new DirectMessage();
    if (jsonObject.has("sender_screen_name")) {
      directMessage.setSenderScreenName(jsonObject
          .getString("sender_screen_name"));
    }
    if (jsonObject.has("recipient_id")) {
      directMessage.setRecipientId(jsonObject.getLong("recipient_id"));
    }
    if (jsonObject.has("sender")) {
      directMessage.setSender(User.newFromJsonObject(jsonObject
          .getJSONObject("sender")));
    }
    if (jsonObject.has("created_at")) {
      directMessage.setCreatedAt(jsonObject.getString("created_at"));
    }
    if (jsonObject.has("recipient_screen_name")) {
      directMessage.setRecipientScreenName(jsonObject
          .getString("recipient_screen_name"));
    }
    if (jsonObject.has("recipient")) {
      directMessage.setRecipient(User.newFromJsonObject(jsonObject
          .getJSONObject("recipient")));
    }
    if (jsonObject.has("text")) {
      directMessage.setText(jsonObject.getString("text"));
    }
    if (jsonObject.has("sender_id")) {
      directMessage.setSenderId(jsonObject.getLong("sender_id"));
    }
    if (jsonObject.has("id")) {
      directMessage.setId(jsonObject.getLong("id"));
    }
    return directMessage;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this,
        ToStringStyle.MULTI_LINE_STYLE);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
