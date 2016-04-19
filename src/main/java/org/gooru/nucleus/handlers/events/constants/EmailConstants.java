package org.gooru.nucleus.handlers.events.constants;

public final class EmailConstants {

    public static final String TEMPLATE_COLLECTION_COLLABORATOR_INVITE = "invite_collaborator_collection";
    public static final String TEMPLATE_CLASS_COLLABORATOR_INVITE = "invite_collaborator_class";
    public static final String TEMPLATE_COURSE_COLLABORATOR_INVITE = "invite_collaborator_course";
    public static final String TEMPLATE_RESOURCE_DELETE = "resource_delete";
    public static final String TEMPLATE_USER_INVITE_CLASS = "user_invite_class";
    public static final String TEMPLATE_USER_INVITE_OPEN_CLASS = "user_invite_open_class";
    public static final String TEMPLATE_PROFILE_FOLLOW = "profile_follow";

    public static final String MAIL_TEMPLATE_NAME = "mail_template_name";
    public static final String MAIL_TEMPLATE_CONTEXT = "mail_template_context";
    public static final String TO_ADDRESSES = "to_addresses";

    public static final String EMAIL_SENT = "email.sent";
    public static final String STATUS = "status";
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAIL = "fail";
    
    public static final String USERNAME = "username";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String COLLECTION_ID = "collection_id";
    public static final String COURSE_ID = "course_id";
    public static final String CLASS_ID = "class_id";
    public static final String CLASS_TITLE = "class_title";
    public static final String RESOURCE_TITLE = "resource_title";
    public static final String TEACHER_USERNAME = "teacher_username";
    public static final String CLASS_CODE = "class_code";
    public static final String EMAIL_ID = "email_id";
    public static final String FOLLOW_ON_USERNAME = "follow_on_username";
    
    private EmailConstants() {
        throw new AssertionError();
    }
}
