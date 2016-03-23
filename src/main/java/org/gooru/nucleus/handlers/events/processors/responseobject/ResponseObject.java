package org.gooru.nucleus.handlers.events.processors.responseobject;

import java.util.Base64;
import java.util.UUID;

import org.gooru.nucleus.handlers.events.constants.EntityConstants;
import org.gooru.nucleus.handlers.events.constants.EventResoponseConstants;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

public class ResponseObject {

  private static final Logger LOGGER = LoggerFactory.getLogger(ResponseObject.class);
  
  protected JsonObject body;
  protected JsonObject response;
  private final String eventName;
  
  protected ResponseObject(JsonObject body, JsonObject response) {
    this.body = body;
    this.response = response;
    this.eventName = body.getString(MessageConstants.MSG_EVENT_NAME);
  }
  
  protected JsonObject createGenericStructure() {
    JsonObject genericStructure = new JsonObject();
    long timeinMS = System.currentTimeMillis();
    genericStructure.put(EventResoponseConstants.START_TIME, timeinMS);  // cannot be null
    genericStructure.put(EventResoponseConstants.END_TIME, timeinMS);    // cannot be null
    genericStructure.put(EventResoponseConstants.EVENT_ID, UUID.randomUUID().toString());
    genericStructure.put(EventResoponseConstants.EVENT_NAME, eventName);
    return genericStructure;
  }
  
  protected JsonObject createMetricsStructure() {
    JsonObject metricsStructure = new JsonObject();
    return metricsStructure;
  }
  
  protected JsonObject createSessionStructure() {
    JsonObject sessionStructure = new JsonObject();
    String sessionToken = this.body.getString(MessageConstants.MSG_HEADER_TOKEN);
    sessionStructure.put(EventResoponseConstants.API_KEY, (Object)null);         // can be null
    sessionStructure.put(EventResoponseConstants.SESSION_TOKEN, sessionToken);   // cannot be null
    sessionStructure.put(EventResoponseConstants.ORGANIZATION_UID, (Object)null);// can be null
    return sessionStructure;
  }

  protected JsonObject createUserStructure() {
    JsonObject userStructure = new JsonObject();
    String sessionToken, userId;
    sessionToken = this.body.getString(MessageConstants.MSG_HEADER_TOKEN);
    String decodedVal = getDecodedUserIDFromSession(sessionToken);
    if (decodedVal != null) {
      userId = decodedVal;
    } else {
      userId = sessionToken;
    }
    
    userStructure.put(EventResoponseConstants.USER_IP, (Object)null); // can be null
    userStructure.put(EventResoponseConstants.USER_AGENT, (Object)null); // can be null
    userStructure.put(EventResoponseConstants.GOORU_UID, userId);   // cannot be null
    return userStructure;
  }
  
  protected JsonObject createVersionStructure() {
    JsonObject versionStructure = new JsonObject();
    versionStructure.put(EventResoponseConstants.LOG_API, EventResoponseConstants.API_VERSION);
    return versionStructure;
  }
  
  protected JsonObject createContextStructure() {
    JsonObject contextStructure = new JsonObject();
    String contentId = this.body.getJsonObject(MessageConstants.MSG_EVENT_BODY).getString(MessageConstants.MSG_EVENT_CONTENT_ID);
    contextStructure.put(EventResoponseConstants.CONTENT_GOORU_ID, contentId); // cannot be null
    contextStructure.put(EventResoponseConstants.PARENT_GOORU_ID, getParentIDFromResponse());
    contextStructure.put(EventResoponseConstants.SOURCE_GOORU_ID, getSourceIDFromResponse());
    contextStructure.put(EventResoponseConstants.CLIENT_SOURCE, (Object)null);
    return contextStructure;
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
  
  protected String getModeFromResponse() {
    String retVal = null;
    switch (eventName) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_COURSE_CREATE:
      case MessageConstants.MSG_OP_EVT_CLASS_CREATE:
        retVal = EventResoponseConstants.MODE_CREATE;
        break;
        
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_COURSE_UPDATE:
      case MessageConstants.MSG_OP_EVT_CLASS_UPDATE:
        retVal = EventResoponseConstants.MODE_UPDATE;
        break;
        
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
      case MessageConstants.MSG_OP_EVT_LESSON_COPY:
      case MessageConstants.MSG_OP_EVT_UNIT_COPY:
      case MessageConstants.MSG_OP_EVT_COURSE_COPY:
        retVal = EventResoponseConstants.MODE_COPY;
        break;
      
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
      case MessageConstants.MSG_OP_EVT_COURSE_DELETE:
      case MessageConstants.MSG_OP_EVT_CLASS_DELETE:
        retVal = EventResoponseConstants.MODE_DELETE;
        break;
        
      case MessageConstants.MSG_OP_EVT_COLLECTION_MOVE:
      case MessageConstants.MSG_OP_EVT_LESSON_MOVE:
      case MessageConstants.MSG_OP_EVT_UNIT_MOVE:
        retVal = EventResoponseConstants.MODE_MOVE;

      default:
        break;
    }
    
    return retVal;
  }
  
  protected Object getItemTypeFromResponse() {
    String retVal = null;
    String retType = null;
    switch (eventName) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
        retVal = this.response.getString(EntityConstants.COLLECTION_ID);
        if (retVal != null) {
          retType = EventResoponseConstants.ITEM_TYPE_COLLECTION_RESOURCE;
        }
        break;
        
      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
        retVal = this.response.getString(EntityConstants.COLLECTION_ID);
        if (retVal != null) {
            retType = EventResoponseConstants.ITEM_TYPE_COLLECTION_QUESTION;
        }
        break;
            
      case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
      case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_MOVE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_CONTENT_REORDER:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COLLABORATOR_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_CONTENT_ADD:
        retVal = this.response.getString(EntityConstants.LESSON_ID);
        if (retVal != null) {
          retType = EventResoponseConstants.ITEM_TYPE_LESSON_COLLECTION;
        }
        break;
        
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CONTENT_REORDER:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COLLABORATOR_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_QUESTION_ADD:
        retVal = this.response.getString(EntityConstants.LESSON_ID);
        if (retVal != null) {
          retType = EventResoponseConstants.ITEM_TYPE_LESSON_ASSESSMENT;
        }
        break;

      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
      case MessageConstants.MSG_OP_EVT_LESSON_COPY:
      case MessageConstants.MSG_OP_EVT_LESSON_MOVE:
      case MessageConstants.MSG_OP_EVT_LESSON_CONTENT_REORDER:
        retVal = this.response.getString(EntityConstants.UNIT_ID);
        if (retVal != null) {
          retType = EventResoponseConstants.ITEM_TYPE_UNIT_LESSON;
        }
        break;

      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
      case MessageConstants.MSG_OP_EVT_UNIT_COPY:
      case MessageConstants.MSG_OP_EVT_UNIT_MOVE:
      case MessageConstants.MSG_OP_EVT_UNIT_CONTENT_REORDER:
        retVal = this.response.getString(EntityConstants.COURSE_ID);
        if (retVal != null) {
          retType = EventResoponseConstants.ITEM_TYPE_COURSE_UNIT;
        }
        break;

      default:
        break;
    }
    return retType;
  }
  
  protected String getTypeFromResponse() {
    String retVal = null;
    switch (eventName) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
        retVal = EventResoponseConstants.TYPE_RESOUCE;
        break;
        
      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
        retVal = EventResoponseConstants.TYPE_QUESTION;
        break;
            
      case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
      case MessageConstants.MSG_OP_EVT_COLLECTION_MOVE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COLLABORATOR_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_CONTENT_ADD:
      case MessageConstants.MSG_OP_EVT_COLLECTION_CONTENT_REORDER:
        retVal = EventResoponseConstants.TYPE_COLLECTION;
        break;
        
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CONTENT_REORDER:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COLLABORATOR_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_QUESTION_ADD:
        retVal = EventResoponseConstants.TYPE_ASSESSMENT;
        break;
    
      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
      case MessageConstants.MSG_OP_EVT_LESSON_COPY:
      case MessageConstants.MSG_OP_EVT_LESSON_MOVE:
      case MessageConstants.MSG_OP_EVT_LESSON_CONTENT_REORDER:
        retVal = EventResoponseConstants.TYPE_LESSON;
        break;
    
      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
      case MessageConstants.MSG_OP_EVT_UNIT_COPY:
      case MessageConstants.MSG_OP_EVT_UNIT_MOVE:
      case MessageConstants.MSG_OP_EVT_UNIT_CONTENT_REORDER:
        retVal = EventResoponseConstants.TYPE_UNIT;
        break;
    
      case MessageConstants.MSG_OP_EVT_COURSE_CREATE:
      case MessageConstants.MSG_OP_EVT_COURSE_UPDATE:
      case MessageConstants.MSG_OP_EVT_COURSE_DELETE:
      case MessageConstants.MSG_OP_EVT_COURSE_COPY:
      case MessageConstants.MSG_OP_EVT_COURSE_CONTENT_REORDER:
      case MessageConstants.MSG_OP_EVT_COURSE_COLLABORATOR_UPDATE:
        retVal = EventResoponseConstants.TYPE_COURSE;
        break;
    
      default:
        break;
    }

    return retVal;
  }
  
  protected Integer getItemSequenceFromResponse() {
    Integer retVal = null;
    try {
      retVal = this.response.getInteger(EntityConstants.SEQUENCE_ID);  // all tables consistently use this as "sequence_id" so we should be good.
    } catch (ClassCastException cce) {
      LOGGER.warn("invalid sequence_id found in respone");
    }
    return retVal;
  }
  
  protected String getSourceIDFromResponse() {
    String retVal = null;
    switch (eventName) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
        retVal = this.response.getString(EntityConstants.ORIGINAL_CONTENT_ID);
        break;

      case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
      case MessageConstants.MSG_OP_EVT_COLLECTION_MOVE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
        retVal = this.response.getString(EntityConstants.ORIGINAL_COLLECTION_ID);
        break;

      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
      case MessageConstants.MSG_OP_EVT_LESSON_COPY:
      case MessageConstants.MSG_OP_EVT_LESSON_MOVE:
        retVal = this.response.getString(EntityConstants.ORIGINAL_LESSON_ID);
        break;

      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
      case MessageConstants.MSG_OP_EVT_UNIT_COPY:
      case MessageConstants.MSG_OP_EVT_UNIT_MOVE:
        retVal = this.response.getString(EntityConstants.ORIGINAL_UNIT_ID);
        break;

      case MessageConstants.MSG_OP_EVT_COURSE_CREATE:
      case MessageConstants.MSG_OP_EVT_COURSE_UPDATE:
      case MessageConstants.MSG_OP_EVT_COURSE_DELETE:
      case MessageConstants.MSG_OP_EVT_COURSE_COPY:
        retVal = this.response.getString(EntityConstants.ORIGINAL_COURSE_ID);
        break;

      default:
        break;
    }
    return retVal;
  }
  
  protected Object getParentIDFromResponse() {
    String retVal = null;
    switch (eventName) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
        retVal = this.response.getString(EntityConstants.COLLECTION_ID);
        break;

      case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
      case MessageConstants.MSG_OP_EVT_COLLECTION_MOVE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
        retVal = this.response.getString(EntityConstants.LESSON_ID);
        break;

      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
      case MessageConstants.MSG_OP_EVT_LESSON_COPY:
      case MessageConstants.MSG_OP_EVT_LESSON_MOVE:
        retVal = this.response.getString(EntityConstants.UNIT_ID);
        break;

      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
      case MessageConstants.MSG_OP_EVT_UNIT_COPY:
      case MessageConstants.MSG_OP_EVT_UNIT_MOVE:
        retVal = this.response.getString(EntityConstants.COURSE_ID);
        break;

      default:
        break;
    }
    
    return retVal;
  }
  
  protected void updateJsonForCULCInfo(JsonObject inoutObj) {
    String courseId = null, unitId = null, lessonId = null, collectionId = null;

    switch (eventName) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
        collectionId = this.response.getString(EntityConstants.COLLECTION_ID);
        lessonId = this.response.getString(EntityConstants.LESSON_ID);
        unitId = this.response.getString(EntityConstants.UNIT_ID);
        courseId = this.response.getString(EntityConstants.COURSE_ID);
        break;
  
      case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
      case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_MOVE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
        lessonId = this.response.getString(EntityConstants.LESSON_ID);
        unitId = this.response.getString(EntityConstants.UNIT_ID);
        courseId = this.response.getString(EntityConstants.COURSE_ID);
        break;
  
      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
      case MessageConstants.MSG_OP_EVT_LESSON_MOVE:
      case MessageConstants.MSG_OP_EVT_LESSON_COPY:
        unitId = this.response.getString(EntityConstants.UNIT_ID);
        courseId = this.response.getString(EntityConstants.COURSE_ID);
        break;
  
      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
      case MessageConstants.MSG_OP_EVT_UNIT_MOVE:
      case MessageConstants.MSG_OP_EVT_UNIT_COPY:
        courseId = this.response.getString(EntityConstants.COURSE_ID);
        break;
  
      default:
        break;
    }

    inoutObj.put(EventResoponseConstants.COURSE_GOORU_ID, courseId);
    inoutObj.put(EventResoponseConstants.UNIT_GOORU_ID, unitId);
    inoutObj.put(EventResoponseConstants.LESSON_GOORU_ID, lessonId);
    inoutObj.put(EventResoponseConstants.COLLECTION_GOORU_ID, collectionId);
  }
}
