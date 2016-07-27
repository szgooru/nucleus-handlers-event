package org.gooru.nucleus.handlers.events.constants;

public final class MessageConstants {
    
    public static final String MSG_HEADER_OP = "mb.operation";

    // Operation names: Also need to be updated in corresponding handlers
    public static final String MSG_OP_EVENT_PUBLISH = "event.publish";
    
    // Content related events
    public static final String MSG_OP_EVT_RESOURCE_CREATE = "event.resource.create";
    public static final String MSG_OP_EVT_RESOURCE_UPDATE = "event.resource.update";
    public static final String MSG_OP_EVT_RESOURCE_DELETE = "event.resource.delete";
    public static final String MSG_OP_EVT_RESOURCE_COPY = "event.resource.copy";

    public static final String MSG_OP_EVT_QUESTION_CREATE = "event.question.create";
    public static final String MSG_OP_EVT_QUESTION_UPDATE = "event.question.update";
    public static final String MSG_OP_EVT_QUESTION_DELETE = "event.question.delete";
    public static final String MSG_OP_EVT_QUESTION_COPY = "event.question.copy";

    public static final String MSG_OP_EVT_COLLECTION_CREATE = "event.collection.create";
    public static final String MSG_OP_EVT_COLLECTION_UPDATE = "event.collection.update";
    public static final String MSG_OP_EVT_COLLECTION_DELETE = "event.collection.delete";
    public static final String MSG_OP_EVT_COLLECTION_COPY = "event.collection.copy";
    public static final String MSG_OP_EVT_COLLECTION_CONTENT_REORDER = "event.collection.content.reorder";
    public static final String MSG_OP_EVT_COLLECTION_CONTENT_ADD = "event.collection.content.add";
    public static final String MSG_OP_EVT_COLLECTION_COLLABORATOR_UPDATE = "event.collection.collaborator.update";
    public static final String MSG_OP_EVT_COLLECTION_MOVE = "event.collection.move";
    public static final String MSG_OP_EVT_COLLECTION_REMOVE = "event.collection.remove";

    public static final String MSG_OP_EVT_ASSESSMENT_CREATE = "event.assessment.create";
    public static final String MSG_OP_EVT_ASSESSMENT_UPDATE = "event.assessment.update";
    public static final String MSG_OP_EVT_ASSESSMENT_DELETE = "event.assessment.delete";
    public static final String MSG_OP_EVT_ASSESSMENT_COPY = "event.assessment.copy";
    public static final String MSG_OP_EVT_ASSESSMENT_QUESTION_ADD = "event.assessment.question.add";
    public static final String MSG_OP_EVT_ASSESSMENT_CONTENT_REORDER = "event.assessment.content.reorder";
    public static final String MSG_OP_EVT_ASSESSMENT_COLLABORATOR_UPDATE = "event.assessment.collaborator.update";

    public static final String MSG_OP_EVT_COURSE_CREATE = "event.course.create";
    public static final String MSG_OP_EVT_COURSE_UPDATE = "event.course.update";
    public static final String MSG_OP_EVT_COURSE_DELETE = "event.course.delete";
    public static final String MSG_OP_EVT_COURSE_COLLABORATOR_UPDATE = "event.course.collaborator.update";
    public static final String MSG_OP_EVT_COURSE_REORDER = "event.course.reorder";
    public static final String MSG_OP_EVT_COURSE_CONTENT_REORDER = "event.course.content.reorder";
    public static final String MSG_OP_EVT_COURSE_COPY = "event.course.copy";

    public static final String MSG_OP_EVT_UNIT_CREATE = "event.unit.create";
    public static final String MSG_OP_EVT_UNIT_UPDATE = "event.unit.update";
    public static final String MSG_OP_EVT_UNIT_DELETE = "event.unit.delete";
    public static final String MSG_OP_EVT_UNIT_CONTENT_REORDER = "event.unit.content.reorder";
    public static final String MSG_OP_EVT_UNIT_MOVE = "event.unit.move";
    public static final String MSG_OP_EVT_UNIT_COPY = "event.unit.copy";

    public static final String MSG_OP_EVT_LESSON_CREATE = "event.lesson.create";
    public static final String MSG_OP_EVT_LESSON_UPDATE = "event.lesson.update";
    public static final String MSG_OP_EVT_LESSON_DELETE = "event.lesson.delete";
    public static final String MSG_OP_EVT_LESSON_MOVE = "event.lesson.move";
    public static final String MSG_OP_EVT_LESSON_CONTENT_REORDER = "event.lesson.content.reorder";
    public static final String MSG_OP_EVT_LESSON_COPY = "event.lesson.copy";

    public static final String MSG_OP_EVT_CLASS_CREATE = "event.class.create";
    public static final String MSG_OP_EVT_CLASS_UPDATE = "event.class.update";
    public static final String MSG_OP_EVT_CLASS_DELETE = "event.class.delete";
    public static final String MSG_OP_EVT_CLASS_COLLABORATOR_UPDATE = "event.class.collaborator.update";
    public static final String MSG_OP_EVT_CLASS_STUDENT_JOIN = "event.class.student.join";
    public static final String MSG_OP_EVT_CLASS_STUDENT_INVITE = "event.class.student.invite";
    public static final String MSG_OP_EVT_CLASS_COURSE_ASSIGNED = "event.class.course.assigned";
    public static final String MSG_OP_EVT_CLASS_CONTENT_VISIBLE = "event.class.content.visible";
    public static final String MSG_OP_EVT_CLASS_STUDENT_REMOVAL = "event.class.student.remove";

    public static final String MSG_OP_EVT_PROFILE_FOLLOW = "event.profile.follow";
    public static final String MSG_OP_EVT_PROFILE_UNFOLLOW = "event.profile.unfollow";

    // Event Structure Type
    public static final int EST_ERROR = -1;
    public static final int EST_ITEM_CREATE = 0;
    public static final int EST_ITEM_EDIT = 1;
    public static final int EST_ITEM_COPY = 2;
    public static final int EST_ITEM_MOVE = 3;
    public static final int EST_ITEM_DELETE = 4;
    public static final int EST_ITEM_REORDER = 5;
    public static final int EST_ITEM_CONTENT_REORDER = 6;
    public static final int EST_ITEM_COLLABORATOR_UPDATE = 7;
    public static final int EST_ITEM_CONTENT_ADD = 8;
    public static final int EST_PROFILE_FOLLOW_UNFOLLOW = 9;
    public static final int EST_INVITE_STUDENT = 10;
    public static final int EST_JOIN_CLASS = 11;
    public static final int EST_ASSIGN_CLASS_COURSE = 12;
    public static final int EST_CLASS_CONTENT_VISIBILITY = 13;
    public static final int EST_REMOVE_STUDENT = 14;
    public static final int EST_ITEM_REMOVE = 15;

    private MessageConstants() {
        throw new AssertionError();
    }
}
