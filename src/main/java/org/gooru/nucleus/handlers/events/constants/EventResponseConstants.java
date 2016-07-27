package org.gooru.nucleus.handlers.events.constants;

public final class EventResponseConstants {

    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String EVENT_ID = "eventId";
    public static final String EVENT_NAME = "eventName";
    public static final String SUB_EVENT_NAME = "subEventName";

    public static final String EVENT_ITEM_CREATE = "item.create";
    public static final String EVENT_ITEM_UPDATE = "item.update";
    public static final String EVENT_ITEM_DELETE = "item.delete";
    public static final String EVENT_ITEM_ADD = "item.add";
    public static final String EVENT_ITEM_COPY = "item.copy";
    public static final String EVENT_ITEM_MOVE = "item.move";
    public static final String EVENT_COLLABORATOR_UPDATE = "collaborators.update";
    public static final String EVENT_CLASS_JOIN = "class.join";
    public static final String EVENT_CLASS_INVITE = "class.invite";
    public static final String EVENT_CONTENT_REORDER = "content.reorder";
    public static final String EVENT_PROFILE_FOLLOW = "profile.follow";
    public static final String EVENT_PROFILE_UNFOLLOW = "profile.unfollow";
    public static final String EVENT_COURSE_REORDER = "course.reorder";
    public static final String EVENT_CLASS_COURSE_ASSIGNED = "class.course.assigned";
    public static final String EVEBT_CLASS_CONTENT_VISIBLE = "class.content.visible";
    public static final String EVEBT_CLASS_REMOVE_STUDENT = "class.student.remove";
    public static final String EVEBT_ITEM_REMOVE = "item.remove";

    public static final String METRICS = "metrics";

    public static final String CONTEXT = "context";
    public static final String CONTENT_GOORU_ID = "contentGooruId";
    public static final String PARENT_GOORU_ID = "parentGooruId";
    public static final String SOURCE_GOORU_ID = "sourceGooruId";
    public static final String CLIENT_SOURCE = "clientSource";
    public static final String COURSE_HIERARCHY = "courseHierarchy";

    public static final String PAYLOAD_OBJECT = "payLoadObject";
    public static final String DATA = "data";
    public static final String COURSE_GOORU_ID = "courseGooruId";
    public static final String UNIT_GOORU_ID = "unitGooruId";
    public static final String LESSON_GOORU_ID = "lessonGooruId";
    public static final String COLLECTION_GOORU_ID = "collectionGooruId";
    public static final String CLASS_GOORU_ID = "classGooruId";
    public static final String CONTENT_ID = "contentId";
    public static final String MODE = "mode";
    public static final String ITEM_TYPE = "itemType";
    public static final String TYPE = "type";
    public static final String ITEM_SEQUENCE = "itemSequence";
    public static final String ITEM_ID = "itemId";
    public static final String PARENT_CONTENT_ID = "parentContentId";
    public static final String ORIGINAL_CONTENT_ID = "originalContentId";
    public static final String CONTENT_FORMAT = "contentFormat";
    public static final String REFERENCE_PARENT_GOORU_IDS = "referenceParentGooruIds";

    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public static final String SESSION = "session";
    public static final String API_KEY = "apiKey";
    public static final String SESSION_TOKEN = "sessionToken";
    public static final String ORGANIZATION_UID = "organizationUId";

    public static final String USER = "user";
    public static final String USER_IP = "userIp";
    public static final String USER_AGENT = "userAgent";
    public static final String GOORU_UID = "gooruUId";

    public static final String VERSION = "version";
    public static final String LOG_API = "logApi";
    public static final String API_VERSION = "0.1";

    public static final String EMAIL = "email_id";

    public static final String FORMAT_RESOUCE = "resource";
    public static final String FORMAT_QUESTION = "question";
    public static final String FORMAT_COLLECTION = "collection";
    public static final String FORMAT_ASSESSMENT = "assessment";
    public static final String FORMAT_LESSON = "lesson";
    public static final String FORMAT_UNIT = "unit";
    public static final String FORMAT_COURSE = "course";
    public static final String FORMAT_CLASS = "class";

    public static final String ITEM_TYPE_COLLECTION_RESOURCE = "collection.resource";
    public static final String ITEM_TYPE_COLLECTION_QUESTION = "collection.question";
    public static final String ITEM_TYPE_LESSON_COLLECTION = "lesson.collection";
    public static final String ITEM_TYPE_LESSON_ASSESSMENT = "lesson.assessment";
    public static final String ITEM_TYPE_UNIT_LESSON = "unit.lesson";
    public static final String ITEM_TYPE_COURSE_UNIT = "course.unit";

    public static final String MODE_CREATE = "create";
    public static final String MODE_UPDATE = "update";
    public static final String MODE_DELETE = "delete";
    public static final String MODE_COPY = "copy";
    public static final String MODE_MOVE = "move";

    public static final String EVENT_TIMESTAMP = "event.timestamp";
    public static final String EVENT_DUMP = "event.dump";
    public static final String ID = "id";

    private EventResponseConstants() {
        throw new AssertionError();
    }

}
