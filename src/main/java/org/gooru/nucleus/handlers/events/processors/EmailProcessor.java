package org.gooru.nucleus.handlers.events.processors;

import java.util.ArrayList;
import java.util.List;

import org.gooru.nucleus.handlers.events.constants.EmailConstants;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;
import org.gooru.nucleus.handlers.events.constants.HttpConstants;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.gooru.nucleus.handlers.events.processors.exceptions.InvalidRequestException;
import org.gooru.nucleus.handlers.events.processors.repositories.RepoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class EmailProcessor implements Processor {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(EmailProcessor.class);
  
  private Vertx vertx;
  private JsonObject config;
  private JsonObject result;
  
  private static final String KEY_ENDPOINT = "api.endpoint";
  private static final String KEY_HOST = "api.host";
  private static final String KEY_EMAIL_SETTINGS = "emailSettings";

  public EmailProcessor(Vertx vertx, JsonObject config, JsonObject result) {
    this.vertx = vertx;
    this.config = config;
    this.result = result;
  }

  @Override
  public JsonObject process() {
    List<String> emailIds = null;
    String eventName;
    try {
       if (!validateAndInitialize()) {
         LOGGER.error("Invalid payload received from the result, can't process to send email");
         throw new InvalidRequestException();
       }
       
       eventName = result.getString(EventResponseConstants.EVENT_NAME);
       switch (eventName) {
       
       case MessageConstants.MSG_OP_EVT_COLLECTION_COLLABORATOR_UPDATE: 
       case MessageConstants.MSG_OP_EVT_COURSE_COLLABORATOR_UPDATE:
       case MessageConstants.MSG_OP_EVT_CLASS_COLLABORATOR_UPDATE:
         emailIds = processEmailCollaboratorUpate();
         break;
         
       case MessageConstants.MSG_OP_EVT_CLASS_STUDENT_INVITE:
         emailIds = processEmailStudentInvite();
         break;
         
       default:
         LOGGER.info("event {} does not require to send email", eventName);
         break;
       }
    } catch (Throwable t) {
      LOGGER.error("Something wrong while processing email", t);
      return new JsonObject().put(EmailConstants.EMAIL_SENT, false).put(EmailConstants.STATUS, EmailConstants.STATUS_FAIL);
    }
    
    if (emailIds == null || emailIds.isEmpty()) {
      return new JsonObject().put(EmailConstants.EMAIL_SENT, false).put(EmailConstants.STATUS, EmailConstants.STATUS_SUCCESS);
    }
    
    emailIds.stream().filter(email -> (email != null && !email.isEmpty())).forEach(email -> {
      final JsonObject requestPayload;
      HttpClientRequest emailRequest = getHttpClient().post(getAPIEndPoint(), responseHandler -> {
        if (responseHandler.statusCode() == HttpConstants.HttpStatus.SUCCESS.getCode()) {
          LOGGER.info("email sent to '{}' for event: {}", email, eventName);
        } else {
          LOGGER.warn("email not sent to '{}' for event {}, HttpStatusCode: {}, requestPayload: {}", email, eventName, responseHandler.statusCode(),
            responseHandler.toString());
        }
      });
      
      //TODO: check for null payload
      requestPayload = generateRequestPayload(eventName, email);
      emailRequest.putHeader(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON);
      emailRequest.putHeader(HttpConstants.HEADER_CONTENT_LENGTH, "" + requestPayload.toString().getBytes().length);
      emailRequest.putHeader(HttpConstants.HEADER_AUTH, getAuthorizationHeader(result));
      emailRequest.write(requestPayload.toString());
      emailRequest.end();
    });
    
      
    return new JsonObject().put(EmailConstants.EMAIL_SENT, true).put(EmailConstants.STATUS, EmailConstants.STATUS_SUCCESS);
  }
  
  //TODO: Not sure whether we can make it singleton or return new instance every time
  // as there will multiple email to send in single request.
  private HttpClient getHttpClient() {
    return vertx.createHttpClient(new HttpClientOptions().setDefaultHost(getAPIHost()));
  }
  
  private List<String> processEmailStudentInvite() {
    // TODO Auto-generated method stub
    return null;
  }

  private List<String> processEmailCollaboratorUpate() {
    JsonObject payloadObject = result.getJsonObject(EventResponseConstants.PAYLOAD_OBJECT);
    JsonObject data = payloadObject.getJsonObject(EventResponseConstants.DATA);
    
    JsonArray collaboratorsAdded = data.getJsonArray(EventRequestConstants.COLLABORATORS_ADDED);
    LOGGER.debug("collaborators.add: {}", collaboratorsAdded.toString());
    List<String> userIds = new ArrayList<>();
    collaboratorsAdded.stream().forEach(collaborator -> userIds.add(collaborator.toString()));
    
    List<String> emailIds = RepoBuilder.buildUserRepo(null).getMultipleEmailIds(userIds);
    LOGGER.debug("no of email ids received: {}", emailIds.size());
    return emailIds;
  }

  private boolean validateAndInitialize() {
    JsonObject payloadObject = result.getJsonObject(EventResponseConstants.PAYLOAD_OBJECT);
    if (payloadObject.isEmpty()) {
      LOGGER.warn("No payload found in event response");
      return false;
    }
    
    JsonObject data = payloadObject.getJsonObject(EventResponseConstants.DATA);
    if (data.isEmpty()) {
      LOGGER.warn("no data found in payload object");
      return false;
    }
    
    return true;
  }

  private String getAPIHost() {
    JsonObject emailSettings = config.getJsonObject(KEY_EMAIL_SETTINGS);
    return emailSettings.getString(KEY_HOST);
  }

  private String getAPIEndPoint() {
    JsonObject emailSettings = config.getJsonObject(KEY_EMAIL_SETTINGS);
    return emailSettings.getString(KEY_ENDPOINT);
  }
  
  private JsonObject generateRequestPayload(String eventName, String email) {
    String templateName = null;
    switch (eventName) {
    
    case MessageConstants.MSG_OP_EVT_COLLECTION_COLLABORATOR_UPDATE: 
    case MessageConstants.MSG_OP_EVT_COURSE_COLLABORATOR_UPDATE:
    case MessageConstants.MSG_OP_EVT_CLASS_COLLABORATOR_UPDATE:
      templateName = EmailConstants.COLLECTION_COLLABORATOR_INVITE;
      break;
      
    case MessageConstants.MSG_OP_EVT_CLASS_STUDENT_INVITE:
      templateName = EmailConstants.USER_INVITE_CLASS;
      break;
      
    default:
      LOGGER.info("no template found for event {}", eventName);
      break;
    }
    
    if (templateName == null) {
      return null;
    }
    
    JsonObject requestPayload = new JsonObject();
    requestPayload.put(EmailConstants.MAIL_TEMPLATE_NAME, templateName);
    requestPayload.put(EmailConstants.TO_ADDRESSES, new JsonArray().add(email));
    return requestPayload;
  }
  
  private String getAuthorizationHeader(JsonObject result) {
    //TODO: check for null session
    JsonObject session = result.getJsonObject(EventResponseConstants.SESSION);
    return "Token " + session.getString(EventResponseConstants.SESSION_TOKEN);
  }
}
