package org.gooru.nucleus.handlers.events.constants;

public final class EventRequestConstants {

  public static final String EVENT_NAME = "event.name";
  public static final String EVENT_BODY = "event.body";
  public static final String SESSION_TOKEN = "session.token";

  public static final String ID = "id";
  public static final String COLLABORATORS_ADDED = "collaborators.added";
  public static final String COLLABORATORS_REMOVED = "collaborators.removed";
  public static final String COLLECTION_ID = "collection_id";

  private EventRequestConstants() {
    throw new AssertionError();
  }
}
