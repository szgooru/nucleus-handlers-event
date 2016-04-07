package org.gooru.nucleus.handlers.events.processors.responseobject;

import java.util.Base64;
import java.util.UUID;

import org.gooru.nucleus.handlers.events.constants.EntityConstants;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityCollection;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityContent;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityLesson;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityUnit;
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
    this.eventName = body.getString(EventRequestConstants.EVENT_NAME);
  }

  protected JsonObject createGenericStructure() {
    JsonObject genericStructure = new JsonObject();
    long timeinMS = System.currentTimeMillis();
    genericStructure.put(EventResponseConstants.START_TIME, timeinMS);  // cannot be null
    genericStructure.put(EventResponseConstants.END_TIME, timeinMS);    // cannot be null
    genericStructure.put(EventResponseConstants.EVENT_ID, UUID.randomUUID().toString());
    genericStructure.put(EventResponseConstants.EVENT_NAME, eventName);
    return genericStructure;
  }

  protected JsonObject createMetricsStructure() {
    return new JsonObject();
  }

  protected JsonObject createSessionStructure() {
    JsonObject sessionStructure = new JsonObject();
    String sessionToken = this.body.getString(EventRequestConstants.SESSION_TOKEN);
    sessionStructure.put(EventResponseConstants.API_KEY, (Object) null);         // can be null
    sessionStructure.put(EventResponseConstants.SESSION_TOKEN, sessionToken);   // cannot be null
    sessionStructure.put(EventResponseConstants.ORGANIZATION_UID, (Object) null);// can be null
    return sessionStructure;
  }

  protected JsonObject createUserStructure() {
    JsonObject userStructure = new JsonObject();
    String sessionToken, userId;
    sessionToken = this.body.getString(EventRequestConstants.SESSION_TOKEN);
    String decodedVal = getDecodedUserIDFromSession(sessionToken);
    if (decodedVal != null) {
      userId = decodedVal;
    } else {
      userId = sessionToken;
    }

    userStructure.put(EventResponseConstants.USER_IP, (Object) null); // can be null
    userStructure.put(EventResponseConstants.USER_AGENT, (Object) null); // can be null
    userStructure.put(EventResponseConstants.GOORU_UID, userId);   // cannot be null
    return userStructure;
  }

  protected JsonObject createVersionStructure() {
    JsonObject versionStructure = new JsonObject();
    versionStructure.put(EventResponseConstants.LOG_API, EventResponseConstants.API_VERSION);
    return versionStructure;
  }

  private String getDecodedUserIDFromSession(String sessionToken) {
    try {
      String decoded = new String(Base64.getDecoder().decode(sessionToken));
      return decoded.split(":")[1];
    } catch (IllegalArgumentException e) {
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
        retVal = EventResponseConstants.MODE_CREATE;
        break;

      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_COURSE_UPDATE:
      case MessageConstants.MSG_OP_EVT_CLASS_UPDATE:
        retVal = EventResponseConstants.MODE_UPDATE;
        break;

      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
      case MessageConstants.MSG_OP_EVT_LESSON_COPY:
      case MessageConstants.MSG_OP_EVT_UNIT_COPY:
      case MessageConstants.MSG_OP_EVT_COURSE_COPY:
        retVal = EventResponseConstants.MODE_COPY;
        break;

      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
      case MessageConstants.MSG_OP_EVT_COURSE_DELETE:
      case MessageConstants.MSG_OP_EVT_CLASS_DELETE:
        retVal = EventResponseConstants.MODE_DELETE;
        break;

      case MessageConstants.MSG_OP_EVT_COLLECTION_MOVE:
      case MessageConstants.MSG_OP_EVT_LESSON_MOVE:
      case MessageConstants.MSG_OP_EVT_UNIT_MOVE:
        retVal = EventResponseConstants.MODE_MOVE;

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
          retType = EventResponseConstants.ITEM_TYPE_COLLECTION_RESOURCE;
        }
        break;

      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
        retVal = this.response.getString(EntityConstants.COLLECTION_ID);
        if (retVal != null) {
          retType = EventResponseConstants.ITEM_TYPE_COLLECTION_QUESTION;
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
          retType = EventResponseConstants.ITEM_TYPE_LESSON_COLLECTION;
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
          retType = EventResponseConstants.ITEM_TYPE_LESSON_ASSESSMENT;
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
          retType = EventResponseConstants.ITEM_TYPE_UNIT_LESSON;
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
          retType = EventResponseConstants.ITEM_TYPE_COURSE_UNIT;
        }
        break;

      default:
        break;
    }
    return retType;
  }

  protected String getContentFormatFromResponse() {
    String retVal = null;
    switch (eventName) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
        retVal = EventResponseConstants.FORMAT_RESOUCE;
        break;

      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
        retVal = EventResponseConstants.FORMAT_QUESTION;
        break;

      case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
      case MessageConstants.MSG_OP_EVT_COLLECTION_MOVE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_COLLABORATOR_UPDATE:
      case MessageConstants.MSG_OP_EVT_COLLECTION_CONTENT_ADD:
      case MessageConstants.MSG_OP_EVT_COLLECTION_CONTENT_REORDER:
        retVal = EventResponseConstants.FORMAT_COLLECTION;
        break;

      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_CONTENT_REORDER:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_COLLABORATOR_UPDATE:
      case MessageConstants.MSG_OP_EVT_ASSESSMENT_QUESTION_ADD:
        retVal = EventResponseConstants.FORMAT_ASSESSMENT;
        break;

      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
      case MessageConstants.MSG_OP_EVT_LESSON_COPY:
      case MessageConstants.MSG_OP_EVT_LESSON_MOVE:
      case MessageConstants.MSG_OP_EVT_LESSON_CONTENT_REORDER:
        retVal = EventResponseConstants.FORMAT_LESSON;
        break;

      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
      case MessageConstants.MSG_OP_EVT_UNIT_COPY:
      case MessageConstants.MSG_OP_EVT_UNIT_MOVE:
      case MessageConstants.MSG_OP_EVT_UNIT_CONTENT_REORDER:
        retVal = EventResponseConstants.FORMAT_UNIT;
        break;

      case MessageConstants.MSG_OP_EVT_COURSE_CREATE:
      case MessageConstants.MSG_OP_EVT_COURSE_UPDATE:
      case MessageConstants.MSG_OP_EVT_COURSE_DELETE:
      case MessageConstants.MSG_OP_EVT_COURSE_COPY:
      case MessageConstants.MSG_OP_EVT_COURSE_CONTENT_REORDER:
      case MessageConstants.MSG_OP_EVT_COURSE_COLLABORATOR_UPDATE:
      case MessageConstants.MSG_OP_EVT_COURSE_REORDER:
        retVal = EventResponseConstants.FORMAT_COURSE;
        break;

      case MessageConstants.MSG_OP_EVT_CLASS_CREATE:
      case MessageConstants.MSG_OP_EVT_CLASS_UPDATE:
      case MessageConstants.MSG_OP_EVT_CLASS_DELETE:
      case MessageConstants.MSG_OP_EVT_CLASS_COLLABORATOR_UPDATE:
      case MessageConstants.MSG_OP_EVT_CLASS_CONTENT_VISIBLE:
      case MessageConstants.MSG_OP_EVT_CLASS_COURSE_ASSIGNED:
      case MessageConstants.MSG_OP_EVT_CLASS_STUDENT_INVITE:
      case MessageConstants.MSG_OP_EVT_CLASS_STUDENT_JOIN:
        retVal = EventResponseConstants.FORMAT_CLASS;

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

  protected Object getParentContentId(JsonObject content) {
    String parentContentId = null;
    switch (eventName) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
        parentContentId = content.getString(EntityConstants.COLLECTION_ID);
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
        parentContentId = content.getString(EntityConstants.LESSON_ID);
        break;

      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
      case MessageConstants.MSG_OP_EVT_LESSON_COPY:
      case MessageConstants.MSG_OP_EVT_LESSON_MOVE:
        parentContentId = content.getString(EntityConstants.UNIT_ID);
        break;

      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
      case MessageConstants.MSG_OP_EVT_UNIT_COPY:
      case MessageConstants.MSG_OP_EVT_UNIT_MOVE:
        parentContentId = content.getString(EntityConstants.COURSE_ID);
        break;

      default:
        break;
    }

    return parentContentId;
  }

  protected void updateCULCInfo(JsonObject fromContent, JsonObject toObject) {
    String courseId = null, unitId = null, lessonId = null;

    switch (eventName) {
      case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
      case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
      case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
        //collectionId = this.response.getString(EntityConstants.COLLECTION_ID);
        lessonId = fromContent.getString(EntityConstants.LESSON_ID);
        unitId = fromContent.getString(EntityConstants.UNIT_ID);
        courseId = fromContent.getString(EntityConstants.COURSE_ID);
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
        lessonId = fromContent.getString(EntityConstants.LESSON_ID);
        unitId = fromContent.getString(EntityConstants.UNIT_ID);
        courseId = fromContent.getString(EntityConstants.COURSE_ID);
        break;

      case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
      case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
      case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
      case MessageConstants.MSG_OP_EVT_LESSON_MOVE:
      case MessageConstants.MSG_OP_EVT_LESSON_COPY:
        unitId = fromContent.getString(EntityConstants.UNIT_ID);
        courseId = fromContent.getString(EntityConstants.COURSE_ID);
        break;

      case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
      case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
      case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
      case MessageConstants.MSG_OP_EVT_UNIT_MOVE:
      case MessageConstants.MSG_OP_EVT_UNIT_COPY:
        courseId = fromContent.getString(EntityConstants.COURSE_ID);
        break;

      default:
        break;
    }

    toObject.put(EventResponseConstants.COURSE_GOORU_ID, courseId);
    toObject.put(EventResponseConstants.UNIT_GOORU_ID, unitId);
    toObject.put(EventResponseConstants.LESSON_GOORU_ID, lessonId);
    //toObject.put(EventResponseConstants.COLLECTION_GOORU_ID, collectionId);
  }
  
  protected String getContentGooruId(JsonObject content) {
    String contentGooruId = null;
    
    switch (eventName) {
    case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
    case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      contentGooruId = content.getString(AJEntityContent.ID);
      break;
      
    
    case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
    case MessageConstants.MSG_OP_EVT_COLLECTION_MOVE:
    case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
      contentGooruId = content.getString(AJEntityCollection.ID);
      break;
      
    case MessageConstants.MSG_OP_EVT_LESSON_COPY:
    case MessageConstants.MSG_OP_EVT_LESSON_MOVE:
      contentGooruId = content.getString(AJEntityLesson.LESSON_ID);
      break;
      
    case MessageConstants.MSG_OP_EVT_UNIT_COPY:
    case MessageConstants.MSG_OP_EVT_UNIT_MOVE:
      contentGooruId = content.getString(AJEntityUnit.UNIT_ID);
      break;
      
    case MessageConstants.MSG_OP_EVT_COURSE_COPY:
      contentGooruId = content.getString(AJEntityCourse.ID);
      break;
      
      default:
        break;
    }
    return contentGooruId;
  } 
  
  protected String getOriginalContentId(JsonObject content) {
    String originalContentGooruId = null;
    
    switch (eventName) {
    case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
    case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
      originalContentGooruId = content.getString(AJEntityContent.ORIGINAL_CONTENT_ID);
      break;
      
    
    case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
    case MessageConstants.MSG_OP_EVT_COLLECTION_MOVE:
    case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
      originalContentGooruId = content.getString(AJEntityCollection.ORIGINAL_COLLECTION_ID);
      break;
      
    case MessageConstants.MSG_OP_EVT_LESSON_COPY:
    case MessageConstants.MSG_OP_EVT_LESSON_MOVE:
      originalContentGooruId = content.getString(AJEntityLesson.ORIGINAL_LESSON_ID);
      break;
      
    case MessageConstants.MSG_OP_EVT_UNIT_COPY:
    case MessageConstants.MSG_OP_EVT_UNIT_MOVE:
      originalContentGooruId = content.getString(AJEntityUnit.ORIGINAL_UNIT_ID);
      break;
      
    case MessageConstants.MSG_OP_EVT_COURSE_COPY:
      originalContentGooruId = content.getString(AJEntityCourse.ORIGINAL_COURSE_ID);
      break;
      
      default:
        break;
    }
    return originalContentGooruId;
  }
}
