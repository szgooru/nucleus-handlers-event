package org.gooru.nucleus.handlers.events.constants;

public final class EmailConstants {

  public static final String COLLECTION_COLLABORATOR_INVITE = "invite_collaborator";
  public static final String USER_INVITE_CLASS = "user_invite_class";
  public static final String USER_INVITE_OPEN_CLASS = "user_invite_open_class";

  public static final String MAIL_TEMPLATE_NAME = "mail_template_name";
  public static final String TO_ADDRESSES = "to_addresses";

  public static final String EMAIL_SENT = "email.sent";
  public static final String STATUS = "status";
  public static final String STATUS_SUCCESS = "success";
  public static final String STATUS_FAIL = "fail";

  public enum EmailTemplateConstants {
    EVT_COLLECTION_COLLABORATOR_UPDATE("invite_collaborator"),
    EVT_COURSE_COLLABORATOR_UPDATE("invite_collaborator");

    private final String templateName;

    EmailTemplateConstants(String templateName) {
      this.templateName = templateName;
    }

    public String getTemplateName() {
      return this.templateName;
    }
  }

  private EmailConstants() {
    throw new AssertionError();
  }
}
