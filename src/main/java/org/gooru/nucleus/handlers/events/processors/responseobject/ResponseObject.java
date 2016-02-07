package org.gooru.nucleus.handlers.events.processors.responseobject;

import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;


/**
 * @author Subbu-Gooru
 *
 */
public class ResponseObject {
  private static final Logger LOGGER = LoggerFactory.getLogger(ResponseObject.class);
  
  public static class Builder {
    private JsonObject body = null;
    private MultiMap headers;
    private JsonObject response = null;
    private int eventType = MessageConstants.EST_ERROR;

    private final String LOG_API = "logApi";
    private final String API_VERSION = "0.1";
    
    public Builder() {
    }

    // Setters for headers, body and response
    public Builder setBody(JsonObject input) {
      this.body = input.copy();
      return this;      
    }
    
    public Builder setHeaders(MultiMap input) {
      this.headers = input;
      return this;      
    }

    public Builder setResponse(JsonObject input) {
      this.response = input.copy();
      return this;      
    }
    
    public Builder setEventType(int type) {
      this.eventType = type;
      return this;
    }
       
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
            result = buildItemCreateResponseObject();
            break;
          case MessageConstants.EST_ITEM_EDIT :
            result = buildItemEditResponseObject();
            break;
          case MessageConstants.EST_ITEM_COPY :
            result = new JsonObject();
            break;
          case MessageConstants.EST_ITEM_MOVE :
            result = new JsonObject();
            break;
          case MessageConstants.EST_ITEM_DELETE :
            result = new JsonObject();
            break;
          default :
            LOGGER.error("Invalid event type seen. Do not know how to handle. Will return failure object.");
            result = buildFailureResponseObject();
            break;           
        }
      }
      
      return result;
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
    private JsonObject buildItemCreateResponseObject() {
      LOGGER.debug("buildItemCreateResponseObject: inputData : " + this.response );
                  
      String contentId = this.body.getJsonObject(MessageConstants.MSG_EVENT_BODY).getString("id");
      
      // add mandatory top-level items: startTime, endTime, eventId, eventName, metrics, session, user, version
      JsonObject retVal = createGenericDataToEventStructure();
      
      // add specific items: context, payLoadObject      
      // TBD : get these values from message object / request object
      JsonObject contextObj = new JsonObject();
      contextObj.put("contentGooruId", contentId); // cannot be null
      contextObj.put("parentGooruId", getParentIDFromResponse());   
      contextObj.put("sourceGooruId", getSourceIDFromResponse());
      contextObj.put("clientSource", "web");  
      retVal.put("context", contextObj);
      LOGGER.debug("buildItemCreateResponseObject: retVal : " + retVal.toString() );
      
      JsonObject payloadObj = new JsonObject();
      payloadObj.put("data", this.response);
      retVal.put("payLoadObject", payloadObj);      
      LOGGER.debug("buildItemCreateResponseObject: returning json:" + retVal.toString());
          
      return retVal;
    }    
    
    /*
     * buildItemEditResponseObject() builds the response object in the structure below.
     * 
     * EVENT Structure for Item.Edit is as below.
     * {"startTime" : 1451994610328,
     *  "eventId"   : "df970f1c-2988-4a57-b297-ac315d46ab3f",
     *  "metrics"   : "{ "totalTimeSpentInMs" : 402 },
     *  "session"   : "{ "sessionToken"    : "fa768c5d-2924-4dc8-9c82-3355e9512789", 
     *                   "organizationUId" : "4261739e-ccae-11e1-adfb-5404a609bd14", 
     *                   "apiKey"          : "ASERTYUIOMNHBGFDXSDWERT123RTGHYT"
     *                 }",
     *  "context"   : "{ "clientSource"    : "web", 
     *                   "url"             : "/gooruapi/rest/v3/item/edit",
     *                   "contentGooruId"  : "uuid of content item",
     *                   "parentGooruId"   : "uuid of parent container of the item"
     *                 }",
     *  "eventName" : "item.edit",
     *  "endTime"   : 1451994610730,
     *  "user"      : "{ "userIp"          : "54.219.20.89", 
     *                   "gooruUId"        : "9f0d4b91-4c7b-4fd2-8bbf-defadc86f122", 
     *                   "userAgent"       : "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36"
     *                 }",
     *  "payLoadObject" : "{ "data"        : "{ "title" : "google", 
     *                                          "description"      : "description",
     *                                          ....
     *                                        }",
     *                     }",
     *  "version"    : "{"logApi"  : "0.1"}"
     * }
     */    
    private JsonObject buildItemEditResponseObject() {
      LOGGER.debug("buildItemEditResponseObject: inputData : " + this.response );
            
      String contentId = this.body.getJsonObject(MessageConstants.MSG_EVENT_BODY).getString("id");
      
      // add mandatory top-level items: startTime, endTime, eventId, eventName, metrics, session, user
      JsonObject retVal = createGenericDataToEventStructure();
      
      // add specific items: context, payLoadObject
      // TBD : get these values from message object / request object
      JsonObject contextObj = new JsonObject();
      contextObj.put("contentGooruId", contentId); // cannot be null
      contextObj.put("parentGooruId", getParentIDFromResponse()); 
      contextObj.put("sourceGooruId", getSourceIDFromResponse());
      contextObj.put("clientSource", "web");  
      retVal.put("context", contextObj);
      LOGGER.debug("buildItemEditResponseObject: retVal : " + retVal.toString() );
            
      JsonObject payloadObj = new JsonObject();
      payloadObj.put("data", this.response);
      retVal.put("payLoadObject", payloadObj);
      
      LOGGER.debug("buildItemEditResponseObject: returning json:" + retVal.toString());
          
      return retVal;
    }
    
    private JsonObject createGenericDataToEventStructure() {
      
      JsonObject retVal = new JsonObject();

      // add mandatory top-level items: startTime, endTime, eventId, eventName, metrics, session, user, version
      long timeinMS = System.currentTimeMillis();
      retVal.put("startTime", timeinMS);  // cannot be null
      retVal.put("endTime", timeinMS);    // cannot be null
      retVal.put("eventId", UUID.randomUUID().toString() );    
      retVal.put("eventName", this.body.getString(MessageConstants.MSG_EVENT_NAME));
      LOGGER.debug("addEventGenericData: retVal : " + retVal.toString() );
      
      // TBD : get these values from message object / request object
      retVal.put("metrics", new JsonObject());  // can be null
      LOGGER.debug("addEventGenericData: retVal : " + retVal.toString() );
      
      // TBD : get these values from message object / request object
      JsonObject sessionObj = new JsonObject();
      sessionObj.put("apiKey", (Object)null);         // can be null
      sessionObj.put("sessionToken", this.headers.get(MessageConstants.MSG_HEADER_TOKEN));   // cannot be null
      sessionObj.put("organizationUId", (Object)null);// can be null
      retVal.put("session", sessionObj);
      LOGGER.debug("addEventGenericData: retVal : " + retVal.toString() );  
      
      // TBD : get these values from message object / request object
      JsonObject userObj = new JsonObject();
      userObj.put("userIp", (Object)null);    
      userObj.put("userAgent", "Chrome");    
      userObj.put("gooruUId", this.headers.get(MessageConstants.MSG_USER_ID));   // cannot be null 
      retVal.put("user", userObj);
      LOGGER.debug("addEventGenericData: retVal : " + retVal.toString() );
      
      retVal.put("version", new JsonObject().put(LOG_API, API_VERSION));
      LOGGER.debug("buildItemEditResponseObject: returning json:" + retVal.toString());
      
      return retVal;
    }
    
    private Object getParentIDFromResponse() {
      String retVal = null;
      final String msgOp = this.body.getString(MessageConstants.MSG_EVENT_NAME);
      switch (msgOp) {
        case MessageConstants.MSG_OP_EVT_RES_GET:  //  this is test related....invalid code...
        case MessageConstants.MSG_OP_EVT_RES_CREATE:
        case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
        case MessageConstants.MSG_OP_EVT_RES_UPDATE:
        case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
        case MessageConstants.MSG_OP_EVT_RES_COPY:
        case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
        case MessageConstants.MSG_OP_EVT_RES_DELETE:
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
        case MessageConstants.MSG_OP_EVT_LESSON_COPY:
        case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
          retVal = this.response.getString("unit_id");
          break;
            
        case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
        case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
        case MessageConstants.MSG_OP_EVT_UNIT_COPY:
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
        case MessageConstants.MSG_OP_EVT_RES_GET:  //  this is test related....invalid code...
        case MessageConstants.MSG_OP_EVT_RES_CREATE:
        case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
        case MessageConstants.MSG_OP_EVT_RES_UPDATE:
        case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
        case MessageConstants.MSG_OP_EVT_RES_COPY:
        case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
        case MessageConstants.MSG_OP_EVT_RES_DELETE:
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
        case MessageConstants.MSG_OP_EVT_LESSON_COPY:
        case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
          retVal = this.response.getString("original_lesson_id");
          break;
            
        case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
        case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
        case MessageConstants.MSG_OP_EVT_UNIT_COPY:
        case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
          retVal = this.response.getString("original_unit_id");
          break;
            
        case MessageConstants.MSG_OP_EVT_COURSE_CREATE:
        case MessageConstants.MSG_OP_EVT_COURSE_UPDATE:
        case MessageConstants.MSG_OP_EVT_COURSE_COPY:
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
    
  } // end of Builder class

  
}

