package org.gooru.nucleus.handlers.events.processors;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.gooru.nucleus.handlers.events.processors.exceptions.InvalidRequestException;
import org.gooru.nucleus.handlers.events.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.AJContentRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MessageProcessor implements Processor {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);
  
  // collect all failed event transmissions in this logger....
  private static final Logger TRANSMIT_FAIL_LOGGER = LoggerFactory.getLogger("org.gooru.nucleus.transmission-errors");

  private Message<Object> message;
//  String userId;
//  JsonObject prefs;
//  JsonObject request;
  
  public MessageProcessor(Message<Object> message) {
    this.message = message;
  }
  
  @Override
  public JsonObject process() {
    JsonObject result = null;
    try {
      if (message == null) {
        LOGGER.error("Invalid message received, either null or body of message is not JsonObject ");
        throw new InvalidRequestException();
      }
      
      JsonObject msgObject = (JsonObject) message.body();
      final String msgOp = msgObject.getString(MessageConstants.MSG_EVENT_NAME);
      
      LOGGER.debug("MsgOp: " + msgOp);
      
      switch (msgOp) {      
        case MessageConstants.MSG_OP_EVT_RES_CREATE:
        case MessageConstants.MSG_OP_EVT_RES_UPDATE:
        case MessageConstants.MSG_OP_EVT_RES_COPY:
          result = processEventResourceCreateUpdateCopy();
          break;
        
        case MessageConstants.MSG_OP_EVT_RES_DELETE:
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
  
        case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
        case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
        case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
          result = processEventCollectionCreateUpdateCopy();
          break;
          
        case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
        case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
        case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
          result = processEventAssessmentCreateUpdateCopy();
          break;
          
        case MessageConstants.MSG_OP_EVT_USER_CREATE:
        case MessageConstants.MSG_OP_EVT_USER_UPDATE:
          result = processUserCreateUpdate();
          break;
          
        default:
          LOGGER.error("Invalid operation type passed in, not able to handle");
          throw new InvalidRequestException();
      }
      return result;
    } catch (InvalidRequestException e) {
      TRANSMIT_FAIL_LOGGER.error( ((JsonObject) message.body()).toString() ); 
    }
    return result;
  }


  /**
   * processEventResourceCreateUpdateCopy:
   *      Handles data collection for CREATE, UPDATE and COPY scenarios
   *      
   *      In all these cases, the event body will contain the following data items:
   *            id, title, description, url, created_at, updated_at, creator_id, original_creator_id, original_content_id, narration, 
   *            content_format, content_subformat, metadata, taxonomy, depth_of_knowledge, thumbnail, 
   *            course_id, unit_id, lesson_id, collection_id, sequence_id, 
   *            is_copyright_owner, copyright_owner, visible_on_profile, is_frame_breaker, is_broken
   *            
   *      Consumer needs to check for null / existence of values
   */
  private JsonObject processEventResourceCreateUpdateCopy() {
    JsonObject msgObject = (JsonObject) message.body();
    if (msgObject != null) {
      JsonObject msgBody = msgObject.getJsonObject(MessageConstants.MSG_EVENT_BODY);
      if (msgBody != null) {
        String contentId = msgBody.getString("id");
        LOGGER.debug("processEventResourceCreateUpdateCopy: getResource(Id) :" + contentId);
        
        JsonObject result = new RepoBuilder().buildContentRepo().getResource(contentId);
        if (result != null) {
          LOGGER.debug("processEventResourceCreateUpdateCopy: getResource(Id) returned:" + result); 
          return buildResponseObject(result);        
        }
      }
    }
    
    LOGGER.error("processEventResourceCreateUpdateCopy: Failed to generate event for resource!! Input data received: " + message.body());
    TRANSMIT_FAIL_LOGGER.error( msgObject.toString() );
    return null;    
  }
  
  /**
   * processEventResourceDelete:
   *      Handles data collection for Resource DELETE scenario
   *      
   *      In all these cases, the event body will contain the following data items:
   *            id, title, description, url, created_at, updated_at, creator_id, original_creator_id, original_content_id,
   *            content_format, content_subformat,  
   *            course_id, unit_id, lesson_id, collection_id, sequence_id, 
   *            is_copyright_owner, copyright_owner
   *            
   *      Consumer needs to check for null / existence of values
   */
  private JsonObject processEventResourceDelete() {
    JsonObject msgObject = (JsonObject) message.body();
    if (msgObject != null) {
      JsonObject msgBody = msgObject.getJsonObject(MessageConstants.MSG_EVENT_BODY);
      if (msgBody != null) {
        String contentId = msgBody.getString("id");
        LOGGER.debug("processEventResourceDelete: getDeletedResource(Id) :" + contentId);
        
        JsonObject result = new RepoBuilder().buildContentRepo().getDeletedResource(contentId);
        if (result != null) {
          LOGGER.debug("processEventResourceDelete: getDeletedResource(Id) returned:" + result);
          return buildResponseObject(result);        
        }
      }
    }
    
    LOGGER.error("processEventResourceDelete: Failed to generate event for resource!! Input data received: " + message.body());
    TRANSMIT_FAIL_LOGGER.error( msgObject.toString() );
    return null;    
  } 
  
  /**
   * processEventQuestionCreateUpdateCopy:
   *      Handles data collection for CREATE, UPDATE and COPY scenarios
   *      
   *      In all these cases, the event body will contain ALL the data items from DB
   *            
   *      Consumer needs to check for null / existence of values
   */
  private JsonObject processEventQuestionCreateUpdateCopy() {
    JsonObject msgObject = (JsonObject) message.body();
    if (msgObject != null) {
      JsonObject msgBody = msgObject.getJsonObject(MessageConstants.MSG_EVENT_BODY);
      if (msgBody != null) {
        String contentId = msgBody.getString("id");
        LOGGER.debug("processEventQuestionCreateUpdateCopy: getQuestion(Id) :" + contentId);
        
        JsonObject result = new RepoBuilder().buildContentRepo().getQuestion(contentId);
        if (result != null) {
          LOGGER.debug("processEventQuestionCreateUpdateCopy: getQuestion(Id) returned:" + result);        
          return buildResponseObject(result);        
        }
      }
    }
    
    LOGGER.error("processEventQuestionCreateUpdateCopy: Failed to generate event for resource!! Input data received: " + message.body());
    TRANSMIT_FAIL_LOGGER.error( msgObject.toString() );
    return null;    
  }
  
  /**
   * processEventQuestionDelete:
   *      Handles data collection for Resource DELETE scenario
   *      
   *      In all these cases, the event body will contain the following data items:
   *            id, title, description, url, created_at, updated_at, creator_id, original_creator_id, original_content_id, short_title,
   *            content_format, content_subformat,  
   *            course_id, unit_id, lesson_id, collection_id, sequence_id 
   *            
   *      Consumer needs to check for null / existence of values
   */
  private JsonObject processEventQuestionDelete() {
    JsonObject msgObject = (JsonObject) message.body();
    if (msgObject != null) {
      JsonObject msgBody = msgObject.getJsonObject(MessageConstants.MSG_EVENT_BODY);
      if (msgBody != null) {
        String contentId = msgBody.getString("id");
        LOGGER.debug("processEventQuestionDelete: getDeletedQuestion(Id) :" + contentId);
        
        JsonObject result = new RepoBuilder().buildContentRepo().getDeletedQuestion(contentId);
        if (result != null) {
          LOGGER.debug("processEventQuestionDelete: getDeletedQuestion(Id) returned:" + result);
          return buildResponseObject(result);
        }
      }
    }
    
    LOGGER.error("processEventQuestionDelete: Failed to generate event for resource!! Input data received: " + message.body());
    TRANSMIT_FAIL_LOGGER.error( msgObject.toString() );
    return null;    
  }
  
  private JsonObject processEventCollectionCreateUpdateCopy() {
    JsonObject msgObject = (JsonObject) message.body();
    if (msgObject != null) {
      JsonObject msgBody = msgObject.getJsonObject(MessageConstants.MSG_EVENT_BODY);
      if (msgBody != null) {
        String contentId = msgBody.getString("id");
        LOGGER.debug("processEventCollectionCreateUpdateCopy: getCollection(Id) :" + contentId);
        
        JsonObject result = new RepoBuilder().buildCollectionRepo().getCollection(contentId);
        if (result != null) {
          LOGGER.debug("processEventCollectionCreateUpdateCopy: getCollection(Id) returned:" + result); 
          return buildResponseObject(result);        
        }
      }
    }
    
    LOGGER.error("processEventCollectionCreateUpdateCopy: Failed to generate event for collection!! Input data received: " + message.body());
    TRANSMIT_FAIL_LOGGER.error( msgObject.toString() );
    return null;    
  }  
  
  private JsonObject processEventAssessmentCreateUpdateCopy() {
    JsonObject msgObject = (JsonObject) message.body();
    if (msgObject != null) {
      JsonObject msgBody = msgObject.getJsonObject(MessageConstants.MSG_EVENT_BODY);
      if (msgBody != null) {
        String contentId = msgBody.getString("id");
        LOGGER.debug("processEventAssessmentCreateUpdateCopy: getAssessment(Id) :" + contentId);
        
        JsonObject result = new RepoBuilder().buildCollectionRepo().getAssessment(contentId);
        if (result != null) {
          LOGGER.debug("processEventAssessmentCreateUpdateCopy: getAssessment(Id) returned:" + result); 
          return buildResponseObject(result);        
        }
      }
    }
    
    LOGGER.error("processEventAssessmentCreateUpdateCopy: Failed to generate event for assessment!! Input data received: " + message.body());
    TRANSMIT_FAIL_LOGGER.error( msgObject.toString() );
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
  private JsonObject processUserCreateUpdate() {
    JsonObject msgObject = (JsonObject) message.body();
    if (msgObject != null) {
      JsonObject msgBody = msgObject.getJsonObject(MessageConstants.MSG_EVENT_BODY);
      if (msgBody != null) {
        String userId = msgBody.getString("user_id");
        LOGGER.debug("processUserCreateUpdateCopy: getUser(Id) :" + userId);
        
        JsonObject result = new RepoBuilder().buildUserRepo().getUser(userId);
        if (result != null) {
          LOGGER.debug("processUserCreateUpdateCopy: getUser(Id) returned:" + result);        
          return buildResponseObject(result);        
        }
      }
    }
    
    LOGGER.error("processUserCreateUpdateCopy: Failed to generate event for resource!! Input data received: " + message.body());
    TRANSMIT_FAIL_LOGGER.error( msgObject.toString() );
    return null;  
  }
  
  private JsonObject buildResponseObject(JsonObject inputData) {
    if (inputData != null) {
      JsonObject returnValue = new JsonObject();
      returnValue.put(MessageConstants.MSG_EVENT_NAME, ((JsonObject) message.body()).getString(MessageConstants.MSG_EVENT_NAME));
      returnValue.put(MessageConstants.MSG_EVENT_BODY, inputData);
      
      LOGGER.debug("buildResponseObject: returning json:" + returnValue.toString());
      
      return returnValue;
    } else {
      TRANSMIT_FAIL_LOGGER.error( ((JsonObject) message.body()).toString() );      
      return null;
    }    
  }

}
