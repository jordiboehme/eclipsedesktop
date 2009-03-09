package net.unto.twitter;


public class StatusTest {

  public StatusTest() {
  }
//
//  private Status newSampleStatus() {
//    return new Status(new Date(1169853434000L), "4391023",
//        "about 10 minutes ago", "Canvas. JC Penny. Three ninety-eight.",
//        newSampleUser());
//  }
//
//  private User newSampleUser() {
//    User user = new User();
//    user.setId("718443");
//    return user;
//  }
//
//  @Test
//  public void testId() {
//    Status status = new Status();
//    status.setId("4391023");
//    assertEquals("4391023", status.getId());
//  }
//
//  @Test
//  public void testCreatedAtAsString() throws ParseException {
//    Status status = new Status();
//    status.setCreatedAt("Fri Jan 26 23:17:14 +0000 2007");
//    assertEquals(1169853434000L, status.getCreatedAt().getTime());
//  }
//
//  @Test
//  public void testCreatedAtAsDate() {
//    Status status = new Status();
//    status.setCreatedAt(new Date(1169853434000L));
//    assertEquals(1169853434000L, status.getCreatedAt().getTime());
//  }
//
//  @Test
//  public void testText() {
//    Status status = new Status();
//    status.setText("Canvas. JC Penny. Three ninety-eight.");
//    assertEquals("Canvas. JC Penny. Three ninety-eight.", status.getText());
//  }
//
//  @Test
//  public void testRelativeCreatedAt() {
//    Status status = new Status();
//    assertEquals("about 10 minutes ago", status.getRelativeCreatedAt());
//  }
//
//  @Test
//  public void testUser() {
//    Status status = new Status();
//    status.setUser(newSampleUser());
//    assertEquals(718443L, status.getUser().getId());
//  }
//
//  @Test
//  public void testEquals() {
//    Status status = new Status();
//    assertEquals(status, status);
//    assertEquals(status, new Status());
//    assertEquals(status, new Status(null, null, null, null, null));
//    assertEquals(new Status(null, null, null, null, null), new Status(null,
//        null, null, null, null));
//    assertEquals(newSampleStatus(), newSampleStatus());
//  }
//
//  @Test
//  public void testNotEquals() {
//    assertFalse(new Status().equals(null));
//    Status lhs = newSampleStatus();
//    Status rhs = newSampleStatus();
//    rhs.setId(lhs.getId() + 1);
//    assertFalse(lhs.equals(rhs));
//  }
//
//  @Test
//  public void testHashCode() {
//    Status status = new Status();
//    assertEquals(status.hashCode(), status.hashCode());
//    assertEquals(status.hashCode(), new Status().hashCode());
//    assertEquals(status.hashCode(), new Status(null, null, null, null, null)
//        .hashCode());
//    assertEquals(new Status(null, null, null, null, null).hashCode(),
//        new Status(null, null, null, null, null).hashCode());
//    assertEquals(newSampleStatus().hashCode(), newSampleStatus().hashCode());
//  }

}
