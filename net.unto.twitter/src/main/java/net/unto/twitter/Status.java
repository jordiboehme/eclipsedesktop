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


public class Status implements Serializable {

  private static final long serialVersionUID = 1L;

  protected Status() {
  }

  private DateTime createdAt;

  public boolean hasCreatedAt() {
    return createdAt != null;
  }

  public DateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(DateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setCreatedAt(String createdAtString) {
    setCreatedAt(TwitterUtil.parseTwitterDateTimeString(createdAtString));
  }

  public String getRelativeCreatedAt() {
    // TODO(dewitt): Create relative_created_at string
    return null;
  }

  private Long id;

  public boolean hasId() {
    return id != null;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  private String source;

  public boolean hasSource() {
    return source != null;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  private String text;

  public boolean hasText() {
    return text != null;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  private User user;

  public boolean hasUser() {
    return user != null;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  protected final static Status[] newArrayFromJsonString(String jsonString)
      throws TwitterException {
    return newArrayFromJsonArray(JSONArray.fromObject(jsonString));
  }

  protected final static Status[] newArrayFromJsonArray(JSONArray jsonArray)
      throws TwitterException {
    List<Status> statusList = new ArrayList<Status>();
    for (int i = 0; i < jsonArray.size(); i++) {
      statusList.add(newFromJsonObject(jsonArray.getJSONObject(i)));
    }
    return statusList.toArray(new Status[statusList.size()]);
  }

  protected final static Status newFromJsonString(String jsonString)
      throws TwitterException {
    return newFromJsonObject(JSONObject.fromObject(jsonString));
  }

  protected final static Status newFromJsonObject(JSONObject jsonObject)
      throws TwitterException {
    if (jsonObject == null) {
      return null;
    }
    Status status = new Status();
    if (jsonObject.has("created_at")) {
      status.setCreatedAt(jsonObject.getString("created_at"));
    }
    if (jsonObject.has("id")) {
      status.setId(jsonObject.getLong("id"));
    }
    if (jsonObject.has("source")) {
      status.setSource(jsonObject.getString("source"));
    }
    if (jsonObject.has("user")) {
      status.setUser(User.newFromJsonObject(jsonObject.getJSONObject("user")));
    }
    if (jsonObject.has("text")) {
      status.setText(jsonObject.getString("text"));
    }

    return status;
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
