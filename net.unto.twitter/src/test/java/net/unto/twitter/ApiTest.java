package net.unto.twitter;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import org.junit.Test;

/**
 * Unit tests for the ApiTest class.
 * 
 * @author DeWitt Clinton <dewitt@unto.net>
 */
public class ApiTest {

  @Test
  public void testGetPublicTimeline() throws TwitterException, IOException {
    Api api = new Api();
    TwitterHttpManager mockTwitterHttpManager = getMockTwitterHttpManager();
    String json = readTestData("public-timeline.json");
    String url = "http://twitter.com/statuses/public_timeline.json";
    Parameter[] parameters = new Parameter[] {new Parameter("since_id", null)};
    expect(mockTwitterHttpManager.get(eq(url), aryEq(parameters))).andReturn(json);
    replay(mockTwitterHttpManager);
    api.setTwitterHttpManager(mockTwitterHttpManager);
    Status[] statuses = api.getPublicTimeline();
    assertEquals(20, statuses.length);
    assertEquals(301231062, (long)statuses[0].getId());
    assertEquals("I should sleep or else...", statuses[0].getText());
    assertEquals(3188291, (long)statuses[0].getUser().getId());
    verify(mockTwitterHttpManager);
  }
  
  @Test
  public void testGetFriendsTimeline() throws TwitterException, IOException {
    Api api = new Api();
    api.setCredentials("javaclient", "xxyzzy");
    TwitterHttpManager mockTwitterHttpManager = getMockTwitterHttpManager();
    String json = readTestData("friends-timeline-javaclient.json");
    String url = "http://twitter.com/statuses/friends_timeline.json";
    Parameter[] parameters = new Parameter[] {new Parameter("since", null), new Parameter("page", null)};
    expect(mockTwitterHttpManager.get(eq(url), aryEq(parameters))).andReturn(json);
    expect(mockTwitterHttpManager.hasCredentials()).andReturn(true);
    replay(mockTwitterHttpManager);
    api.setTwitterHttpManager(mockTwitterHttpManager);
    Status[] statuses = api.getFriendsTimeline();
    assertEquals(1, statuses.length);
    assertEquals(303230492, (long)statuses[0].getId());
    assertEquals(673483, (long)statuses[0].getUser().getId());
    verify(mockTwitterHttpManager);
  }
  
  @Test
  public void testGetFriendsTimelineString() throws TwitterException, IOException {
    Api api = new Api();
    TwitterHttpManager mockTwitterHttpManager = getMockTwitterHttpManager();
    String json = readTestData("friends-timeline-javaclient.json");
    String url = "http://twitter.com/statuses/friends_timeline/javaclient.json";
    Parameter[] parameters = new Parameter[] {new Parameter("since", null), new Parameter("page", null)};
    expect(mockTwitterHttpManager.get(eq(url), aryEq(parameters))).andReturn(json);
    replay(mockTwitterHttpManager);
    api.setTwitterHttpManager(mockTwitterHttpManager);
    Status[] statuses = api.getFriendsTimeline("javaclient");
    assertEquals(1, statuses.length);
    assertEquals(303230492, (long)statuses[0].getId());
    assertEquals(673483, (long)statuses[0].getUser().getId());
    verify(mockTwitterHttpManager);
  }
  
  private String TEST_DATA_DIR = "src/test/data/";
  
  private int MAX_TEST_DATA_FILE_SIZE = 16384;
  
  private String readTestData(String fileName) throws IOException {
    return readFromFile(new File(TEST_DATA_DIR, fileName));
  }
  
  private String readFromFile(File file) throws IOException {
    Reader reader = new FileReader(file);
    CharBuffer charBuffer = CharBuffer.allocate(MAX_TEST_DATA_FILE_SIZE);
    reader.read(charBuffer);
    charBuffer.position(0);
    return charBuffer.toString();
  }
  
  private TwitterHttpManager getMockTwitterHttpManager() {
    return createMock(TwitterHttpManager.class);
  }
}
