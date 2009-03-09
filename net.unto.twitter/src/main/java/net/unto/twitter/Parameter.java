package net.unto.twitter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * A class to represent key/value request parameters in the query string and headers.
 * 
 * @author DeWitt Clinton <dewitt@unto.net>
 */
final class Parameter
{
  private String name = null;
  
  private Object value = null;
  
  protected Parameter(String name, Object value) {
    this.name = name;
    this.value = value;
  }
  
  public String getName() {
    return name;
  }
  
  public boolean hasName() {
    return name != null;
  }
  
  public Object getValue() {
    return value;
  }
  
  public boolean hasValue() {
    return value != null;
  }
  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
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
