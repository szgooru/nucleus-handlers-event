package org.gooru.nucleus.handlers.events.processors;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.gooru.nucleus.handlers.events.processors.exceptions.InvalidRequestException;
import org.gooru.nucleus.handlers.events.processors.repositories.RepoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

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
      LOGGER.debug("MsgObject: " + msgObject.toString());
      LOGGER.debug("MsgObject HEADERS: " + message.headers().toString());

      final String msgOp = msgObject.getString(MessageConstants.MSG_EVENT_NAME);
      
      LOGGER.debug("MsgOp: " + msgOp);
      
      switch (msgOp) {      
        case MessageConstants.MSG_OP_EVT_RES_CREATE:
        case MessageConstants.MSG_OP_EVT_RES_UPDATE:
        case MessageConstants.MSG_OP_EVT_RES_COPY:
        case MessageConstants.MSG_OP_EVT_RES_GET:
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
      TRANSMIT_FAIL_LOGGER.error( buildFailureResponseObject().toString() ); 
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
    TRANSMIT_FAIL_LOGGER.error( buildFailureResponseObject().toString() );
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
    TRANSMIT_FAIL_LOGGER.error( buildFailureResponseObject().toString() );
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
    TRANSMIT_FAIL_LOGGER.error( buildFailureResponseObject().toString() );
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
    TRANSMIT_FAIL_LOGGER.error( buildFailureResponseObject().toString() );
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
    TRANSMIT_FAIL_LOGGER.error( buildFailureResponseObject().toString() );
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
    TRANSMIT_FAIL_LOGGER.error( buildFailureResponseObject().toString() );
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
    TRANSMIT_FAIL_LOGGER.error( buildFailureResponseObject().toString() );
    return null;  
  }
  
  
  /*
X   * {"startTime" : 1451994610328,
X   *  "eventId"   : "df970f1c-2988-4a57-b297-ac315d46ab3f",
X   *  "metrics"   : "{ "totalTimeSpentInMs" : 402 },
X   *  "session"   : "{ "sessionToken"    : "fa768c5d-2924-4dc8-9c82-3355e9512789", 
   *                   "organizationUId" : "4261739e-ccae-11e1-adfb-5404a609bd14", 
   *                   "apiKey"          : "ASERTYUIOMNHBGFDXSDWERT123RTGHYT"
   *                 }",
X   *  "context"   : "{ "registerType"    : "google", 
   *                   "clientSource"    : "web", 
   *                   "url"             : "/gooruapi/rest/v2/account/authenticate"
   *                 }",
V   *  "eventName" : "user.register",
X   *  "endTime"   : 1451994610730,
X   *  "user"      : "{ "userIp"          : "54.219.20.89", 
   *                   "gooruUId"        : "9f0d4b91-4c7b-4fd2-8bbf-defadc86f122", 
   *                   "userAgent"       : "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36"
   *                 }",
   *  "payLoadObject" : "{ "data"        : "{ "accountCreatedType" : "google", 
   *                                          "accountTypeId"      : 3,
   *                                          "active"             : 1, 
   *                                          "confirmStatus"      : 1,
   *                                          "createdOn"          : "Tue Jan 05 11:50:10 UTC 2016",
   *                                          "emailId"            : "",
   *                                          "firstName"          : "Renu", 
   *                                          "gooruUId"           : "9f0d4b91-4c7b-4fd2-8bbf-defadc86f122", 
   *                                          "lastLogin"          : 1451994610706, 
   *                                          "lastName"           : "Walia", 
   *                                          "loginType"          : "google", 
   *                                          "organizationName"   : "Gooru", 
   *                                          "partyUid"           : "9f0d4b91-4c7b-4fd2-8bbf-defadc86f122", 
   *                                          "profileImageUrl"    : "http://profile-images-goorulearning-org.s3.amazonaws.com/9f0d4b91-4c7b-4fd2-8bbf-defadc86f122.png",
   *                                          "registeredOn"       : 1451994610348,
   *                                          "token"              : "fa768c5d-2924-4dc8-9c82-3355e9512789",
   *                                          "userRoleSetString"  : "User", 
   *                                          "username"           : "RenuW", 
   *                                          "usernameDisplay"    : "RenuW",
   *                                          "viewFlag"           : 0 
   *                                        }",
X   *                       "requestMethod" : "POST",
X   *                       "IdpName"       : "gmail.com",
X   *                       "created_type"  : "google" 
   *                     }",
   *  "version"    : "{"logApi"  : "0.1"}"
   * }
   */
  
  private final String LOG_API = "logApi";
  private final String API_VERSION = "0.1";
  
  private JsonObject buildResponseObject(JsonObject inputData) {
    LOGGER.debug("buildResponseObject: inputData : " + inputData );
    
    if (inputData == null) {
        TRANSMIT_FAIL_LOGGER.error( buildFailureResponseObject().toString() );      
        return null;
    }
    
    JsonObject retVal = new JsonObject();    
    
    JsonObject msgObject = (JsonObject) message.body();
    JsonObject msgBody = msgObject.getJsonObject(MessageConstants.MSG_EVENT_BODY);
    String contentId = msgBody.getString("id");
    
    // add mandatory top-level items: startTime, endTime, eventId, eventName, metrics, session, context, user, payLoadObject
    long timeinMS = System.currentTimeMillis();
    retVal.put("startTime", timeinMS);  // cannot be null
    retVal.put("endTime", timeinMS);    // cannot be null
    retVal.put("eventId", UUID.randomUUID().toString() );    
    retVal.put("eventName", msgObject.getString(MessageConstants.MSG_EVENT_NAME));
    LOGGER.debug("buildResponseObject: retVal : " + retVal.toString() );
    
    // TBD : get these values from message object / request object
    retVal.put("metrics", new JsonObject());  // can be null
    LOGGER.debug("buildResponseObject: retVal : " + retVal.toString() );
    
    // TBD : get these values from message object / request object
    JsonObject sessionObj = new JsonObject();
    sessionObj.put("apiKey", (Object)null);         // can be null
    sessionObj.put("sessionToken", message.headers().get(MessageConstants.MSG_HEADER_TOKEN));   // cannot be null
    sessionObj.put("organizationUId", (Object)null);// can be null
    retVal.put("session", sessionObj);
    LOGGER.debug("buildResponseObject: retVal : " + retVal.toString() );
    
    // TBD : get these values from message object / request object
    JsonObject contextObj = new JsonObject();
    contextObj.put("contentGooruId", contentId); // cannot be null
    contextObj.put("clientSource", "web");  
    retVal.put("context", contextObj);
    LOGGER.debug("buildResponseObject: retVal : " + retVal.toString() );
    
    // TBD : get these values from message object / request object
    JsonObject userObj = new JsonObject();
    userObj.put("userIp", (Object)null);    
    userObj.put("userAgent", "Chrome");    
    userObj.put("gooruUId", message.headers().get(MessageConstants.MSG_USER_ID));   // cannot be null 
    retVal.put("user", userObj);
    LOGGER.debug("buildResponseObject: retVal : " + retVal.toString() );
    
    JsonObject payloadObj = new JsonObject();
    payloadObj.put("data", inputData);
    retVal.put("payLoadObject", payloadObj);
    LOGGER.debug("buildResponseObject: retVal : " + retVal.toString() );
    
    retVal.put("version", new JsonObject().put(LOG_API, API_VERSION));
    
    LOGGER.debug("buildResponseObject: returning json:" + retVal.toString());
        
    return retVal;
  }
  
  private JsonObject buildFailureResponseObject() {
    JsonObject returnValue = new JsonObject();
    returnValue.put(MessageConstants.MSG_EVENT_TIMESTAMP, new Date().toString());
    returnValue.put(MessageConstants.MSG_EVENT_DUMP, (JsonObject) message.body());
    
    LOGGER.debug("buildFailureResponseObject: returning json:" + returnValue.toString());
    
    return returnValue;
  }
  
}
