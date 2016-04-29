package org.gooru.nucleus.handlers.events.constants;

public final class EventRequestConstants {

    public static final String EVENT_NAME = "event.name";
    public static final String EVENT_BODY = "event.body";
    public static final String SESSION_TOKEN = "session.token";

    public static final String ID = "id";
    public static final String CONTENT_ID = "content.id";
    public static final String COLLABORATORS = "collaborators";
    public static final String COLLABORATORS_ADDED = "collaborators.added";
    public static final String COLLABORATORS_REMOVED = "collaborators.removed";
    public static final String COLLECTION_ID = "collection_id";

    public static final String INVITEES = "invitees";
    public static final String USER_ID = "user_id";
    public static final String FOLLOW_ON_USER_ID = "follow_on_user_id";

    public static final String SOURCE = "source";
    public static final String TARGET = "target";
    public static final String COURSE_ID = "course_id";
    public static final String UNIT_ID = "unit_id";
    public static final String LESSON_ID = "lesson_id";

    private EventRequestConstants() {
        throw new AssertionError();
    }
}
