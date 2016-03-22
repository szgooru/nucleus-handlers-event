package org.gooru.nucleus.handlers.events.processors;

import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.gooru.nucleus.handlers.events.processors.exceptions.InvalidRequestException;
import org.gooru.nucleus.handlers.events.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.events.processors.responseobject.ResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

class MessageProcessor implements Processor {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);

  // collect all failed event transmissions in this logger....
  private static final Logger TRANSMIT_FAIL_LOGGER = LoggerFactory.getLogger("org.gooru.nucleus.transmission-errors");

  private final Message<Object> message;
  private JsonObject request;

  public MessageProcessor(Message<Object> message) {
    this.message = message;
  }

  @Override
  public JsonObject process() {
    JsonObject result = null;
    try {
      if (!validateAndInitialize()) {
        LOGGER.error("Invalid message received, either null or body of message is not JsonObject");
        throw new InvalidRequestException();
      }

      final String msgOp = request.getString(MessageConstants.MSG_EVENT_NAME);
      LOGGER.debug("## Processing Event: " + msgOp);

      switch (msgOp) {
        case MessageConstants.MSG_OP_EVT_COURSE_CREATE:
        case MessageConstants.MSG_OP_EVT_COURSE_UPDATE:
        case MessageConstants.MSG_OP_EVT_COURSE_COPY:
          result = processEventCourseCreateUpdateCopy();
          break;
          
        case MessageConstants.MSG_OP_EVT_COURSE_DELETE:
          result = processEventCourseDelete();
          break;
          
        case MessageConstants.MSG_OP_EVT_COURSE_REORDER:
          result = processEventCourseReorder();
          break;
          
        case MessageConstants.MSG_OP_EVT_COURSE_CONTENT_REORDER:
          result = processEventCourseContentReorder();
          break;
          
        case MessageConstants.MSG_OP_EVT_COURSE_COLLABORATOR_UPDATE:
          result = processEventCourseCollaboratorUpdate();
          break;
          
        case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
        case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
        case MessageConstants.MSG_OP_EVT_UNIT_COPY:
          result = processEventUnitCreateUpdateCopy();
          break;
          
        case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
          result = processEventUnitDelete();
          break;
          
        case MessageConstants.MSG_OP_EVT_UNIT_MOVE:
          result = processEventUnitMove();
          break;
          
        case MessageConstants.MSG_OP_EVT_UNIT_CONTENT_REORDER:
          result = processEventUnitContentReorder();
          break;
          
        case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
        case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
        case MessageConstants.MSG_OP_EVT_LESSON_COPY:
          result = processEventLessonCreateUpdateCopy();
          break;
          
        case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
          result = processEventLessonDelete();
          break;
          
        case MessageConstants.MSG_OP_EVT_LESSON_MOVE:
          result = processEventLessonMove();
          break;
          
        case MessageConstants.MSG_OP_EVT_LESSON_CONTENT_REORDER:
          result = processEventLessonContentReorder();
          break;
          
        case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
        case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
        case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
          result = processEventCollectionCreateUpdateCopy();
          break;
          
        case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
          result = processEventCollectionDelete();
          break;
          
        case MessageConstants.MSG_OP_EVT_COLLECTION_COLLABORATOR_UPDATE:
          result = processEventCollectionCollaboratorUpdate();
          break;
          
        case MessageConstants.MSG_OP_EVT_COLLECTION_CONTENT_ADD:
          result = processEventCollectionContentAdd();
          break;
          
        case MessageConstants.MSG_OP_EVT_COLLECTION_MOVE:
          result = processEventCollectionMove();
          break;
          
        case MessageConstants.MSG_OP_EVT_COLLECTION_REORDER:
          result = processEventCollectionReorder();
          
        case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
        case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
        case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
          result = processEventAssessmentCreateUpdateCopy();
          break;
          
        case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
          result = processEventAssessmentDelete();
          break;
          
        case MessageConstants.MSG_OP_EVT_ASSESSMENT_COLLABORATOR_UPDATE:
          result = processEventAssessmentCollaboratorUpdate();
          break;
          
        case MessageConstants.MSG_OP_EVT_ASSESSMENT_QUESTION_ADD:
          result = processEventAssessmentQuestionAdd();
          break;
          
        case MessageConstants.MSG_OP_EVT_ASSESSMENT_REORDER:
          result = processEventAssessmentReorder();
          
        case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
        case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
        case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
          result = processEventResourceCreateUpdateCopy();
          break;
          
        case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
          result = processEventResourceDelete();
          break;
          
        case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
        case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
        case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
          result = processEventQuestionCreateUpdateCopy();
          break;
          
        case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
          result = processEventQuestionDelete();
          break;
          
        case MessageConstants.MSG_OP_EVT_CLASS_CREATE:
        case MessageConstants.MSG_OP_EVT_CLASS_UPDATE:
          result = processEventClassCreateUpdate();
          break;
          
        case MessageConstants.MSG_OP_EVT_CLASS_DELETE:
          result = processEventClassDelete();
          break;
          
        case MessageConstants.MSG_OP_EVT_CLASS_COLLABORATOR_UPDATE:
          result = processEventClassCollaboratorUpdate();
          break;
          
        case MessageConstants.MSG_OP_EVT_CLASS_COURSE_ASSIGNED:
          result = processEventClassCourseAssigned();
          break;
          
        case MessageConstants.MSG_OP_EVT_CLASS_STUDENT_INVITE:
          result = processEventClassStudentInvite();
          break;
          
        case MessageConstants.MSG_OP_EVT_CLASS_STUDENT_JOIN:
          result = processEventClassStudentJoin();
          break;
          
        case MessageConstants.MSG_OP_EVT_USER_CREATE:
        case MessageConstants.MSG_OP_EVT_USER_UPDATE:
          result = processEventUserCreateUpdate();
          break;
          
        case MessageConstants.MSG_OP_EVT_USER_AUTHENTICATION:
          result = processEventUserAuthentication();
          break;
          
        case MessageConstants.MSG_OP_EVT_USER_AUTHORIZE:
          result = processEventUserAuthorize();
          break;
          
        case MessageConstants.MSG_OP_EVT_USER_RESET_PASSWORD:
          result = processEventUserResetPassword();
          break;
          
        case MessageConstants.MSG_OP_EVT_USER_RESEND_CONFIRM_EMAIL:
          result = processEventUserResendConfirmEmail();
          break;
          
        case MessageConstants.MSG_OP_EVT_USER_PREFS_UPDATE:
          result = processEventUserPrefsUpdate();
          break;
          
        case MessageConstants.MSG_OP_EVT_USER_UPDATE_EMAIL_CONFIRM:
          result = processEventUserUpdateEmailConfirm();
          break;
          
        default:
          LOGGER.error("Invalid operation type passed in, not able to handle");
          throw new InvalidRequestException();
      }
      return result;
    } catch (InvalidRequestException e) {
      TRANSMIT_FAIL_LOGGER.error( ResponseFactory.generateErrorResponse((JsonObject) (message != null ? message.body() : null)).toString() );
    }
    return result;
  }
  
  private boolean validateAndInitialize() {
    if (message == null || !(message.body() instanceof JsonObject)) {
      LOGGER.error("Invalid message received, either null or body of message is not JsonObject ");
      return false;
    }
    
    request = (JsonObject) message.body();
    if (request == null) {
      LOGGER.error("Invalid JSON payload on message bus. aborting");
      return false;
    }
    
    return true;
  }
  
  private ProcessorContext createContext() {
    String eventName = request.getString(MessageConstants.MSG_EVENT_NAME);
    JsonObject eventBody = request.getJsonObject(MessageConstants.MSG_EVENT_BODY);
    String id = eventBody.getString(MessageConstants.MSG_EVENT_CONTENT_ID);
    return new ProcessorContext(eventName, eventBody, id);
  }

  private JsonObject processEventCourseCreateUpdateCopy() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildCourseRepo(context).getCourse();
      if (result != null) {
        LOGGER.debug("getCourse() returned: {}", result);
        return ResponseFactory.generateItemCreateResponse(request, result);
      }
    } catch (Throwable t) {
      LOGGER.error("Error while getting content from database:", t);
    }
    LOGGER.error("Failed to generate event. Input data received {}", request);
    TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
    return null;
  }
  
  private JsonObject processEventCourseDelete() {
    // TODO Auto-generated method stub
    return null;
  }
  
  private JsonObject processEventCourseReorder() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventCourseContentReorder() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventCourseCollaboratorUpdate() {
    // TODO Auto-generated method stub
    return null;
  }
  
  private JsonObject processEventUnitCreateUpdateCopy() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildUnitRepo(context).getUnit();
      if (result != null) {
        LOGGER.debug("getUnit() returned: {}", result);
        return ResponseFactory.generateItemCreateResponse(request, result);
      }
    } catch (Throwable t) {
      LOGGER.error("Error while getting content from database:", t);
    }
    LOGGER.error("Failed to generate event. Input data received {}", request);
    TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
    return null;
  }
  
  private JsonObject processEventUnitDelete() {
    // TODO Auto-generated method stub
    return null;
  }
  
  private JsonObject processEventUnitMove() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventUnitContentReorder() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventLessonCreateUpdateCopy() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildLessonRepo(context).getLesson();
      if (result != null) {
        LOGGER.debug("getLesson() returned: {}", result);
        return ResponseFactory.generateItemCreateResponse(request, result);
      }
    } catch (Throwable t) {
      LOGGER.error("Error while getting content from database:", t);
    }
    LOGGER.error("Failed to generate event. Input data received {}", request);
    TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
    return null;
  }
  
  private JsonObject processEventLessonDelete() {
    // TODO Auto-generated method stub
    return null;
  }
  
  private JsonObject processEventLessonMove() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventLessonContentReorder() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventCollectionCreateUpdateCopy() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildCollectionRepo(context).getCollection();
      if (result != null) {
        LOGGER.debug("getCollection() returned: {}", result);
        return ResponseFactory.generateItemCreateResponse(request, result);
      }
    } catch (Throwable t) {
      LOGGER.error("Error while getting content from database:", t);
    }
    LOGGER.error("Failed to generate event. Input data received {}", request);
    TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
    return null;
  }
  
  private JsonObject processEventCollectionDelete() {
    // TODO Auto-generated method stub
    return null;
  }
  
  private JsonObject processEventCollectionCollaboratorUpdate() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventCollectionContentAdd() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventCollectionMove() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventCollectionReorder() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventAssessmentCreateUpdateCopy() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildCollectionRepo(context).getAssessment();
      if (result != null) {
        LOGGER.debug("getAssessment() returned: {}", result);
        return ResponseFactory.generateItemCreateResponse(request, result);
      }
    } catch (Throwable t) {
      LOGGER.error("Error while getting content from database:", t);
    }
    LOGGER.error("Failed to generate event. Input data received {}", request);
    TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
    return null;
  }
  
  private JsonObject processEventAssessmentDelete() {
    // TODO Auto-generated method stub
    return null;
  }
  
  private JsonObject processEventAssessmentCollaboratorUpdate() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventAssessmentQuestionAdd() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventAssessmentReorder() {
    // TODO Auto-generated method stub
    return null;
  }
  
  private JsonObject processEventResourceCreateUpdateCopy() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildContentRepo(context).getResource();
      if (result != null) {
        LOGGER.debug("getResource() returned: {}", result);
        return ResponseFactory.generateItemCreateResponse(request, result);
      }
    } catch (Throwable t) {
      LOGGER.error("Error while getting content from database:", t);
    }
    LOGGER.error("Failed to generate event. Input data received {}", request);
    TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
    return null;
  }
  
  private JsonObject processEventResourceDelete() {
    // TODO Auto-generated method stub
    return null;
  }
  
  private JsonObject processEventQuestionCreateUpdateCopy() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildContentRepo(context).getQuestion();
      if (result != null) {
        LOGGER.debug("getQuestion() returned: {}", result);
        return ResponseFactory.generateItemCreateResponse(request, result);
      }
    } catch (Throwable t) {
      LOGGER.error("Error while getting content from database:", t);
    }
    LOGGER.error("Failed to generate event. Input data received {}", request);
    TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
    return null;
  }

  private JsonObject processEventQuestionDelete() {
    // TODO Auto-generated method stub
    return null;
  }
  
  private JsonObject processEventClassCreateUpdate() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventClassDelete() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventClassCollaboratorUpdate() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventClassCourseAssigned() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventClassStudentInvite() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventClassStudentJoin() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventUserCreateUpdate() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildUserRepo(context).getUser();
      if (result != null) {
        LOGGER.debug("getUser() returned: {}", result);
        return ResponseFactory.generateItemCreateResponse(request, result);
      }
    } catch (Throwable t) {
      LOGGER.error("Error while getting content from database:", t);
    }
    LOGGER.error("Failed to generate event. Input data received {}", request);
    TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
    return null;
  }

  private JsonObject processEventUserAuthentication() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventUserAuthorize() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventUserResetPassword() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventUserResendConfirmEmail() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventUserPrefsUpdate() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventUserUpdateEmailConfirm() {
    // TODO Auto-generated method stub
    return null;
  }
  

  /**
   * processUserCreateUpdateCopy:
   *      Handles data collection for CREATE & UPDATE scenarios
   *
   *      In all these cases, the event body will contain the following data items:
   *            user_id, firstname, lastname, parent_user_id, user_category,
   *            created_at, updated_at, last_login, birth_date, grade,
   *            thumbnail_path, gender, email, school_id, school_name,
   *            school_district_id, school_district_name, country_id, country_name, state_id, state_name
   *
   *      Consumer needs to check for null / existence of values
   */
/*  private JsonObject processUserCreateUpdate() {
    JsonObject msgObject = (JsonObject) message.body();
    if (msgObject != null) {
      JsonObject msgBody = msgObject.getJsonObject(MessageConstants.MSG_EVENT_BODY);
      if (msgBody != null) {
        String userId = msgBody.getString("user_id");
        LOGGER.debug("processUserCreateUpdateCopy: getUser(Id) :" + userId);

        JsonObject result = RepoBuilder.buildUserRepo().getUser(userId);
        if (result != null) {
          LOGGER.debug("processUserCreateUpdateCopy: getUser(Id) returned:" + result);
          return ResponseFactory.generateItemCreateResponse(msgObject, result);
        }
      }
    }

    LOGGER.error("processUserCreateUpdateCopy: Failed to generate event for resource!! Input data received: " + message.body());
    TRANSMIT_FAIL_LOGGER.error( ResponseFactory.generateErrorResponse((JsonObject)message.body()).toString() );
    return null;
  }*/

}
