package org.gooru.nucleus.handlers.events.bootstrap;

import org.gooru.nucleus.handlers.events.bootstrap.shutdown.Finalizer;
import org.gooru.nucleus.handlers.events.bootstrap.shutdown.Finalizers;
import org.gooru.nucleus.handlers.events.bootstrap.startup.Initializer;
import org.gooru.nucleus.handlers.events.bootstrap.startup.Initializers;
import org.gooru.nucleus.handlers.events.constants.EmailTemplateConstants;
import org.gooru.nucleus.handlers.events.constants.EventResoponseConstants;
import org.gooru.nucleus.handlers.events.constants.HttpConstants;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.gooru.nucleus.handlers.events.constants.MessagebusEndpoints;
import org.gooru.nucleus.handlers.events.processors.MessageDispatcher;
import org.gooru.nucleus.handlers.events.processors.ProcessorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by ashish on 25/12/15.
 */
public class EventPublisherVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventPublisherVerticle.class);
  private static final String KEY_ENDPOINT = "api.endpoint";
  private static final String KEY_HOST = "api.host";
  private static final String KEY_EMAIL_SETTINGS = "emailSettings";

  @Override
  public void start(Future<Void> voidFuture) throws Exception {

    vertx.executeBlocking(blockingFuture -> {
      startApplication();
      blockingFuture.complete();
    }, future -> {
      if (future.succeeded()) {
        LOGGER.info("Successfully initialized EventPublish Handler machinery");
        voidFuture.complete();
      } else {
        voidFuture.fail("Not able to initialize the EventPublish Handler machinery properly");
      }
    });

    EventBus eb = vertx.eventBus();

    eb.consumer(MessagebusEndpoints.MBEP_EVENT, message -> {

      LOGGER.debug("Received message: " + message.body());

      vertx.executeBlocking(future -> {
        // do blocking database interactions here...and collect the return value which will be proccessed in the
        // onSuccessful completion handler....which would be to just dispatch the message.
        JsonObject result = ProcessorBuilder.build(message).process();
        future.complete(result);

      }, res -> {

        if (res.succeeded()) {
          //
          // We can be here with or without a valid result object -- as in case of object not found in DB returns null
          // but future.complete() happened successfully.......
          // So, do check null objects upon return.
          //
          JsonObject result = (JsonObject)res.result();
          if (result != null) {

            LOGGER.debug("***********************************************");
            LOGGER.debug("Now dispatch message: \n \n " + result.toString() + " \n \n");
            LOGGER.debug("***********************************************");

            String eventName = result.getString(EventResoponseConstants.EVENT_NAME);
            //JsonObject eventBody = result.getJsonObject(MessageConstants.MSG_EVENT_BODY);

            MessageDispatcher.getInstance().sendMessage2Kafka(eventName, result);
            LOGGER.info("Message dispatched successfully for event: {}", eventName);

            JsonObject emailData = generateEmailData(result);
            if (emailData != null && !emailData.isEmpty()) {
              HttpClient httpClient = vertx.createHttpClient(new HttpClientOptions().setDefaultHost(getAPIHost()));
              HttpClientRequest emailRequest = httpClient.post(getAPIEndPoint(), response -> {
                if (response.statusCode() == HttpConstants.HttpStatus.SUCCESS.getCode()) {
                  LOGGER.info("email has been successfully sent for event: {}", eventName);
                } else {
                  LOGGER.warn("email not sent for event {}, HttpStatusCode: {}, requestPayload: {}", eventName, response.statusCode(), emailData.toString());
                }
              });
              emailRequest.putHeader(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON);
              emailRequest.putHeader(HttpConstants.HEADER_CONTENT_LENGTH, ""+emailData.toString().getBytes().length);
              emailRequest.putHeader(HttpConstants.HEADER_AUTH, getAuthorizationHeader(result));
              emailRequest.write(emailData.toString());
              emailRequest.end();
            } else {
              LOGGER.warn("may be incorrect email data or no need to send email for event: {}", eventName);
            }
          } else {
            LOGGER.warn("No data received from database interaction for this. So, no message being relayed to Kafka.");
          }
        } else {
          LOGGER.error("Error processing the database interactions!!");
        }
      });

    }).completionHandler(result -> {
      if (result.succeeded()) {
        LOGGER.info("EventPublish handler end point ready to listen");
      } else {
        LOGGER.error("Error registering the EventPublish handler. Halting the EventPublish Handler machinery");
        Runtime.getRuntime().halt(1);
      }
    });
  }

  private String getAPIHost() {
    JsonObject emailSettings = config().getJsonObject(KEY_EMAIL_SETTINGS);
    return emailSettings.getString(KEY_HOST);
  }

  private String getAPIEndPoint() {
    JsonObject emailSettings = config().getJsonObject(KEY_EMAIL_SETTINGS);
    return emailSettings.getString(KEY_ENDPOINT);
  }

  @Override
  public void stop() throws Exception {
    shutDownApplication();
    super.stop();
  }

  private void startApplication() {
    Initializers initializers = new Initializers();
    try {
      for (Initializer initializer : initializers) {
        initializer.initializeComponent(vertx, config());
      }
    } catch(IllegalStateException ie) {
      LOGGER.error("Error initializing application", ie);
      Runtime.getRuntime().halt(1);
    }
  }

  private void shutDownApplication() {
    Finalizers finalizers = new Finalizers();
    for (Finalizer finalizer : finalizers ) {
      finalizer.finalizeComponent();
    }
  }
  
  /*
   * {
    "mail_template_name": "welcome_mail",
    "to_addresses": [
        "sheeban@gooru.org"
    ],
    "attachments": [
        {
            "url": "http://www.cbu.edu.zm/downloads/pdf-sample.pdf",
            "filename": "attachment.pdf"
        }
    ]
  }
   */
  private JsonObject generateEmailData(JsonObject result) {
    JsonObject toReturn = new JsonObject();
    String emailTemplateName = null;
    String eventName = result.getString(EventResoponseConstants.EVENT_NAME);
    switch (eventName) {
    case MessageConstants.MSG_OP_EVT_USER_CREATE:
      emailTemplateName = EmailTemplateConstants.WELCOME_MAIL;
      break;
      
    /*case MessageConstants.MSG_OP_EVT_USER_RESET_PASSWORD:
      emailTemplateName = EmailTemplateConstants.PASSWORD_CHANGE_REQUEST;
      break;
      
    case MessageConstants.MSG_OP_EVT_USER_UPDATE_EMAIL_CONFIRM:
      break;
      
    case MessageConstants.MSG_OP_EVT_COURSE_COLLABORATOR_UPDATE:
    case MessageConstants.MSG_OP_EVT_COLLECTION_COLLABORATOR_UPDATE:
    case MessageConstants.MSG_OP_EVT_ASSESSMENT_COLLABORATOR_UPDATE:
    case MessageConstants.MSG_OP_EVT_CLASS_COLLABORATOR_UPDATE:
      emailTemplateName = EmailTemplateConstants.INVITE_COLLABORATOR;
      break;
      
      //TODO: check whether the class sharing is open
      //based on that set email template
    case MessageConstants.MSG_OP_EVT_CLASS_STUDENT_INVITE:
      emailTemplateName = EmailTemplateConstants.USER_INVITE_CLASS;
      break;
    */
      default:
        LOGGER.warn("not event matched to generate email data");
        emailTemplateName = null;
    }
    
    if (emailTemplateName == null) {
      LOGGER.warn("No email template name found for the event");
      return null;
    }
    toReturn.put(EmailTemplateConstants.MAIL_TEMPLATE_NAME, emailTemplateName);
    
    JsonObject payloadObject = result.getJsonObject(EventResoponseConstants.PAYLOAD_OBJECT);
    if (payloadObject.isEmpty()) {
      LOGGER.warn("No payload found in event response");
      return null;
    }
    
    JsonObject data = payloadObject.getJsonObject(EventResoponseConstants.DATA);
    if (data.isEmpty()) {
      LOGGER.warn("no data found in payload object");
      return null;
    }
    
    String email = data.getString(EventResoponseConstants.EMAIL);
    if (email == null || email.isEmpty()) {
      LOGGER.warn("invalid email address found in data payload");
      return null;
    }
    
    toReturn.put(EmailTemplateConstants.TO_ADDRESSES, new JsonArray().add(email));
    return toReturn;
  }
  
  private String getAuthorizationHeader(JsonObject result) {
    //TODO: check for null session
    JsonObject session = result.getJsonObject(EventResoponseConstants.SESSION);
    return "Token " + session.getString(EventResoponseConstants.SESSION_TOKEN);
  }
}
