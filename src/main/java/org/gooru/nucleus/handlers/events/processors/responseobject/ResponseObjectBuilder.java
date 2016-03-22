package org.gooru.nucleus.handlers.events.processors.responseobject;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.gooru.nucleus.handlers.events.constants.EventResoponseConstants;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;


/**
 * @author Subbu-Gooru
 *
 */
public final class ResponseObjectBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(ResponseObjectBuilder.class);

  private JsonObject body = null;
  private JsonObject response = null;
  private int eventType = MessageConstants.EST_ERROR;

  public ResponseObjectBuilder() {
  }

  // Setters for headers, body and response
  public ResponseObjectBuilder setBody(JsonObject input) {
    this.body = input.copy();
    return this;
  }

  public ResponseObjectBuilder setResponse(JsonObject input) {
    this.response = input.copy();
    return this;
  }

  public ResponseObjectBuilder setEventType(int type) {
    this.eventType = type;
    return this;
  }

  public static final int EST_ITEM_REORDER = 5;
  public static final int EST_ITEM_CONTENT_REORDER = 6;
  public static final int EST_ITEM_COLLABORATOR_UPDATE = 7;
  public static final int EST_ITEM_CONTENT_ADD = 8;
  
  public JsonObject build() {
    JsonObject result;
    if ((this.response == null) || (this.body == null)) {
      LOGGER.error("Can't create response with invalid response. Will return internal error");
      result = buildFailureResponseObject();
    } else {
      switch (this.eventType) {
        case MessageConstants.EST_ERROR :
          result = buildFailureResponseObject();
          break;
        case MessageConstants.EST_ITEM_CREATE :
        case MessageConstants.EST_ITEM_EDIT :
        case MessageConstants.EST_ITEM_COPY :
          result = buildItemCreateUpdateCopyResponseObject();
          break;
        case MessageConstants.EST_ITEM_MOVE :
          result = buildItemMoveResponseObject();
          break;
        case MessageConstants.EST_ITEM_DELETE :
          result = buildItemDeleteResponseObject();
          break;
        case MessageConstants.EST_ITEM_REORDER:
          result = buildItemReorderResponseObject();
          break;
        case MessageConstants.EST_ITEM_CONTENT_REORDER:
          result = buildItemContentReorderResponseObject();
          break;
        case MessageConstants.EST_ITEM_COLLABORATOR_UPDATE:
          result = buildItemCollaboratorUpdate();
          break;
        case MessageConstants.EST_ITEM_CONTENT_ADD:
          result = buildItemContentAdd();
          break;
        default :
          LOGGER.error("Invalid event type seen. Do not know how to handle. Will return failure object.");
          result = buildFailureResponseObject();
          break;
      }
    }

    return result;
  }

  private JsonObject buildItemMoveResponseObject() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject buildItemDeleteResponseObject() {
    LOGGER.debug("buildItemCreateUpdateCopyResponseObject: inputData : " + this.response );

    String contentId = this.body.getJsonObject(MessageConstants.MSG_EVENT_BODY).getString(MessageConstants.MSG_EVENT_CONTENT_ID);

    // add mandatory top-level items: startTime, endTime, eventId, eventName, metrics, session, user, version
    JsonObject retVal = createEventStructureWithGenericData();

    // add specific items: context, payLoadObject
    // TBD : get these values from message object / request object
    JsonObject contextObj = new JsonObject();
    contextObj.put(EventResoponseConstants.CONTENT_GOORU_ID, contentId); // cannot be null
    contextObj.put(EventResoponseConstants.PARENT_GOORU_ID, getParentIDFromResponse());
    contextObj.put(EventResoponseConstants.SOURCE_GOORU_ID, getSourceIDFromResponse());
    contextObj.put(EventResoponseConstants.CLIENT_SOURCE, "web");
    retVal.put(EventResoponseConstants.CONTEXT, contextObj);

    JsonObject payloadObj = createPayloadFromResponse();
    retVal.put(EventResoponseConstants.PAYLOAD_OBJECT, payloadObj);
    return retVal;  
  }

  private JsonObject buildItemReorderResponseObject() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject buildItemContentReorderResponseObject() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject buildItemCollaboratorUpdate() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject buildItemContentAdd() {
    // TODO Auto-generated method stub
    return null;
  }

  private  JsonObject buildFailureResponseObject() {
    JsonObject returnValue = new JsonObject();
    returnValue.put(MessageConstants.MSG_EVENT_TIMESTAMP, new Date().toString());
    returnValue.put(MessageConstants.MSG_EVENT_DUMP, this.body);

    LOGGER.debug("buildFailureResponseObject: returning json:" + returnValue.toString());
    return returnValue;
  }

  /*
   * buildItemCreateResponseObject() builds the response object in the structure below.
   *
   * EVENT Structure for Item.Create is as below.
   * {"startTime" : 1451994610328,
   *  "eventId"   : "df970f1c-2988-4a57-b297-ac315d46ab3f",
   *  "metrics"   : "{ "totalTimeSpentInMs" : 402 },
   *  "session"   : "{ "sessionToken"    : "fa768c5d-2924-4dc8-9c82-3355e9512789",
   *                   "organizationUId" : "4261739e-ccae-11e1-adfb-5404a609bd14",
   *                   "apiKey"          : "ASERTYUIOMNHBGFDXSDWERT123RTGHYT"
   *                 }",
   *  "context"   : "{ "registerType"    : "google",
   *                   "clientSource"    : "web",
   *                   "url"             : "/gooruapi/rest/v2/account/authenticate"
   *                 }",
   *  "eventName" : "user.register",
   *  "endTime"   : 1451994610730,
   *  "user"      : "{ "userIp"          : "54.219.20.89",
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
   *                       "requestMethod" : "POST",
   *                       "IdpName"       : "gmail.com",
   *                       "created_type"  : "google"
   *                     }",
   *  "version"    : "{"logApi"  : "0.1"}"
   * }
   */
  private JsonObject buildItemCreateUpdateCopyResponseObject() {
    LOGGER.debug("buildItemCreateUpdateCopyResponseObject: inputData : " + this.response );

    String contentId = this.body.getJsonObject(MessageConstants.MSG_EVENT_BODY).getString(MessageConstants.MSG_EVENT_CONTENT_ID);

    // add mandatory top-level items: startTime, endTime, eventId, eventName, metrics, session, user, version
    JsonObject retVal = createEventStructureWithGenericData();

    // add specific items: context, payLoadObject
    // TBD : get these values from message object / request object
    JsonObject contextObj = new JsonObject();
    contextObj.put(EventResoponseConstants.CONTENT_GOORU_ID, contentId); // cannot be null
    contextObj.put(EventResoponseConstants.PARENT_GOORU_ID, getParentIDFromResponse());
    contextObj.put(EventResoponseConstants.SOURCE_GOORU_ID, getSourceIDFromResponse());
    contextObj.put(EventResoponseConstants.CLIENT_SOURCE, "web");
    retVal.put(EventResoponseConstants.CONTEXT, contextObj);

    JsonObject payloadObj = createPayloadFromResponse();
    retVal.put(EventResoponseConstants.PAYLOAD_OBJECT, payloadObj);
    return retVal;
  }

  private JsonObject createEventStructureWithGenericData() {

    JsonObject retVal = new JsonObject();

    // add mandatory top-level items: startTime, endTime, eventId, eventName, metrics, session, user, version
    long timeinMS = System.currentTimeMillis();
    retVal.put(EventResoponseConstants.START_TIME, timeinMS);  // cannot be null
    retVal.put(EventResoponseConstants.END_TIME, timeinMS);    // cannot be null
    retVal.put(EventResoponseConstants.EVENT_ID, UUID.randomUUID().toString() );
    retVal.put(EventResoponseConstants.EVENT_NAME, this.body.getString(MessageConstants.MSG_EVENT_NAME));

    // TBD : get these values from message object / request object
    retVal.put(EventResoponseConstants.METRICS, new JsonObject());  // can be null

    // TBD : get these values from message object / request object
    // extract session token and base64decode should give us tokenID and UserId 
    String sessionToken, userId;
    sessionToken = this.body.getString(MessageConstants.MSG_HEADER_TOKEN);
    String decodedVal = getDecodedUserIDFromSession(sessionToken);
    if (decodedVal != null) userId = decodedVal;
    else userId = sessionToken;
    
    //TODO: parse session and extract details
    JsonObject sessionObj = new JsonObject();
    sessionObj.put(EventResoponseConstants.API_KEY, (Object)null);         // can be null
    sessionObj.put(EventResoponseConstants.SESSION_TOKEN, sessionToken);   // cannot be null
    sessionObj.put(EventResoponseConstants.ORGANIZATION_UID, (Object)null);// can be null
    retVal.put(EventResoponseConstants.SESSION, sessionObj);

    // TBD : get these values from message object / request object
    JsonObject userObj = new JsonObject();
    userObj.put(EventResoponseConstants.USER_IP, (Object)null); // can be null
    userObj.put(EventResoponseConstants.USER_AGENT, "Chrome"); // can be null
    // TODO: from session
    userObj.put(EventResoponseConstants.GOORU_UID, userId);   // cannot be null
    retVal.put(EventResoponseConstants.USER, userObj);

    retVal.put(EventResoponseConstants.VERSION, new JsonObject().put(EventResoponseConstants.LOG_API, EventResoponseConstants.API_VERSION));
    
    LOGGER.debug("createEventStructureWithGenericData: returning json:" + retVal.toString());
    return retVal;
  }

  private Object getParentIDFromResponse() {
    String retVal = null;
    final String msgOp = this.body.getString(MessageConstants.MSG_EVENT_NAME);
    switch (msgOp) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
        retVal = this.response.getString("collection_id");
        break;

      case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
      case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
        retVal = this.response.getString("lesson_id");
        break;

      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
        retVal = this.response.getString("unit_id");
        break;

      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
        retVal = this.response.getString("course_id");
        break;

      case MessageConstants.MSG_OP_EVT_USER_CREATE:
      case MessageConstants.MSG_OP_EVT_USER_UPDATE:
        retVal = this.response.getString("parent_user_id");
        break;

      default:
        // technically we should not land here...
        LOGGER.error("Invalid operation type passed in, not able to handle");
    }
    
    return retVal;
  }

  private Object getSourceIDFromResponse() {
    String retVal = null;
    final String msgOp = this.body.getString(MessageConstants.MSG_EVENT_NAME);
    switch (msgOp) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
        retVal = this.response.getString("original_content_id");
        break;

      case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
      case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
        retVal = this.response.getString("original_collection_id");
        break;

      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
        retVal = this.response.getString("original_lesson_id");
        break;

      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
        retVal = this.response.getString("original_unit_id");
        break;

      case MessageConstants.MSG_OP_EVT_COURSE_CREATE:
      case MessageConstants.MSG_OP_EVT_COURSE_UPDATE:
      case MessageConstants.MSG_OP_EVT_COURSE_DELETE:
        retVal = this.response.getString("original_course_id");
        break;

      case MessageConstants.MSG_OP_EVT_USER_CREATE:
      case MessageConstants.MSG_OP_EVT_USER_UPDATE:
        retVal = this.response.getString("parent_user_id");
        break;

      default:
        // technically we should not land here...
        LOGGER.error("Invalid operation type passed in, not able to handle");
    }
    return retVal;
  }
  
  private Object getItemTypeFromResponse() {
    String retVal = null;
    String retType = null;
    final String msgOp = this.body.getString(MessageConstants.MSG_EVENT_NAME);
    switch (msgOp) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
        retVal = this.response.getString("collection_id");
        if (retVal != null) {
          if (AJEntityContent.CONTENT_FORMAT_RESOURCE.equalsIgnoreCase(this.response.getString("content_format"))) {
            retType = "collection.resource";
          } else {
            retType = "collection.question";
          }
        }
        break;
            
      case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
      case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
        retVal = this.response.getString("lesson_id");
        if (retVal != null) {
          retType = "lesson.collection";
        }
        break;
        
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
        retVal = this.response.getString("lesson_id");
        if (retVal != null) {
          retType = "lesson.assessment";
        }
        break;

      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
        retVal = this.response.getString("unit_id");
        if (retVal != null) {
          retType = "unit.lesson";
        }
        break;

      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
        retVal = this.response.getString("course_id");
        if (retVal != null) {
          retType = "course.unit";
        }
        break;

      case MessageConstants.MSG_OP_EVT_USER_CREATE:
      case MessageConstants.MSG_OP_EVT_USER_UPDATE:
        retVal = this.response.getString("parent_user_id");
        break;

      default:
        // technically we should not land here...
        LOGGER.error("Invalid operation type passed in, not able to handle");
    }
    return retType;
  }

  private Object getTypeFromResponse() {
    String retVal = null;
    final String msgOp = this.body.getString(MessageConstants.MSG_EVENT_NAME);
    switch (msgOp) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
        retVal = "resource";
        break;
        
      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
        retVal = "question";
        break;
            
      case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
      case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
        retVal = "collection";
        break;
        
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
        retVal = "assessment";
        break;
    
      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
        retVal = "lesson";
        break;
    
      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
        retVal = "unit";
        break;
    
      case MessageConstants.MSG_OP_EVT_COURSE_CREATE:
        retVal = "course";
        break;
        
      case MessageConstants.MSG_OP_EVT_USER_CREATE:
      case MessageConstants.MSG_OP_EVT_USER_UPDATE:
        retVal = "user";
        break;
    
      default:
        break;
    }

    return retVal;
  } 
  
  private Object getItemSequenceFromResponse() {
    Integer retVal = this.response.getInteger("sequence_id");  // all tables consistently use this as "sequence_id" so we should be good.
    return retVal;
  }
  
  private Object getModeFromResponse() {
    String retVal = null;
    final String msgOp = this.body.getString(MessageConstants.MSG_EVENT_NAME);
    switch (msgOp) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_USER_CREATE:
        retVal = "create";
        break;
        
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_USER_UPDATE:
        retVal = "update";
        break;
        
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
      case MessageConstants.MSG_OP_EVT_COURSE_COPY:
      case MessageConstants.MSG_OP_EVT_UNIT_COPY:
      case MessageConstants.MSG_OP_EVT_LESSON_COPY:
        retVal = "copy";
        break;
      
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
        retVal = "delete";
        break;

      default:
        break;
    }
    
    return retVal;
  }  
  
  private void updateJsonForCULCInfo(JsonObject inoutObj) {    
    String courseId = null, unitId = null, lessonId = null, collectionId = null;
    
    final String msgOp = this.body.getString(MessageConstants.MSG_EVENT_NAME);
    switch (msgOp) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
        collectionId = this.response.getString("collection_id");
        lessonId = this.response.getString("lesson_id");
        unitId = this.response.getString("unit_id");
        courseId = this.response.getString("course_id");
        break;

      case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
      case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
        lessonId = this.response.getString("lesson_id");
        unitId = this.response.getString("unit_id");
        courseId = this.response.getString("course_id");
        break;
        
      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
        unitId = this.response.getString("unit_id");
        courseId = this.response.getString("course_id");
        break;

      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
        courseId = this.response.getString("course_id");
        break;
            
      default:
        break;
    }
    
    inoutObj.put(EventResoponseConstants.COURSE_GOORU_ID, (Object)courseId );
    inoutObj.put(EventResoponseConstants.UNIT_GOORU_ID, (Object)unitId );
    inoutObj.put(EventResoponseConstants.LESSON_GOORU_ID, (Object)lessonId );
    inoutObj.put(EventResoponseConstants.COLLECTION_GOORU_ID, (Object)collectionId );

    return;
  }
  
  private JsonObject createPayloadFromResponse() {
    JsonObject retJson = new JsonObject();
    String contentId = this.body.getJsonObject(MessageConstants.MSG_EVENT_BODY).getString(MessageConstants.MSG_EVENT_CONTENT_ID);
    
    retJson.put(EventResoponseConstants.DATA, this.response);
    retJson.put(EventResoponseConstants.CONTENT_ID, contentId);
    retJson.put(EventResoponseConstants.MODE, getModeFromResponse());
    retJson.put(EventResoponseConstants.ITEM_TYPE, getItemTypeFromResponse());
    retJson.put(EventResoponseConstants.TYPE,  getTypeFromResponse());
    retJson.put(EventResoponseConstants.ITEM_SEQUENCE, getItemSequenceFromResponse());
    retJson.put(EventResoponseConstants.ITEM_ID, contentId);    
    retJson.put(EventResoponseConstants.SOURCE_GOORU_ID, getSourceIDFromResponse());
    retJson.put(EventResoponseConstants.PARENT_CONTENT_ID, getParentIDFromResponse());
    
    updateJsonForCULCInfo(retJson);

    return retJson;
  }
  
  private String getDecodedUserIDFromSession(String sessionToken) {
    try {
      String decoded = new String(Base64.getDecoder().decode(sessionToken));
      return decoded.split(":")[1];
    } catch (IllegalArgumentException e ) {
      LOGGER.error(e.getMessage());
      return null;
    }
  }
}

