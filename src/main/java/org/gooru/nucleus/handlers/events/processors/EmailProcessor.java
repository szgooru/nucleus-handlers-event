package org.gooru.nucleus.handlers.events.processors;

import org.gooru.nucleus.handlers.events.constants.EmailConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;
import org.gooru.nucleus.handlers.events.constants.HttpConstants;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.gooru.nucleus.handlers.events.emails.EmailDataBuilder;
import org.gooru.nucleus.handlers.events.processors.exceptions.InvalidRequestException;
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

    private final Vertx vertx;
    private final JsonObject config;
    private final JsonObject result;
    private final JsonObject message;
    private String eventName = null;

    private static final String KEY_ENDPOINT = "api.endpoint";
    private static final String KEY_HOST = "api.host";
    private static final String KEY_EMAIL_SETTINGS = "emailSettings";

    public EmailProcessor(Vertx vertx, JsonObject config, JsonObject result, JsonObject message) {
        this.vertx = vertx;
        this.config = config;
        this.result = result;
        this.message = message;
    }

    @Override
    public JsonObject process() {
        JsonArray emailData = null;
        try {
            if (!validatePayload()) {
                LOGGER.error("Invalid payload received from the result, can't process to send email");
                throw new InvalidRequestException();
            }

            JsonObject payloadObject = result.getJsonObject(EventResponseConstants.PAYLOAD_OBJECT);
            eventName = payloadObject.getString(EventResponseConstants.SUB_EVENT_NAME);
            switch (eventName) {
            case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
                emailData = processEmailForResourceDelete();
                break;

            case MessageConstants.MSG_OP_EVT_COLLECTION_COLLABORATOR_UPDATE:
                emailData = processEmailForCollectionCollaboratorUpdate();
                break;
            
            case MessageConstants.MSG_OP_EVT_COURSE_COLLABORATOR_UPDATE:
                emailData = processEmailForCourseCollaboratorUpdate();
                break;
                
            case MessageConstants.MSG_OP_EVT_CLASS_COLLABORATOR_UPDATE:
                emailData = processEmailForClassCollaboratorUpate();
                break;

            case MessageConstants.MSG_OP_EVT_CLASS_STUDENT_INVITE:
                emailData = processEmailToInviteStudent();
                break;

            case MessageConstants.MSG_OP_EVT_PROFILE_FOLLOW:
                emailData = processEmailToFollowProfile();
                break;

            default:
                LOGGER.info("event {} does not require to send email", eventName);
                return new JsonObject().put(EmailConstants.EMAIL_SENT, false).put(EmailConstants.STATUS,
                    EmailConstants.STATUS_SUCCESS);
            }
        } catch (Throwable t) {
            LOGGER.error("Something wrong while processing email", t);
            return new JsonObject().put(EmailConstants.EMAIL_SENT, false).put(EmailConstants.STATUS,
                EmailConstants.STATUS_FAIL);
        }

        if (emailData == null || emailData.isEmpty()) {
            return new JsonObject().put(EmailConstants.EMAIL_SENT, false).put(EmailConstants.STATUS,
                EmailConstants.STATUS_SUCCESS);
        }

        emailData.stream().forEach(data -> {
            HttpClientRequest emailRequest = getHttpClient().post(getAPIEndPoint(), responseHandler -> {
                if (responseHandler.statusCode() == HttpConstants.HttpStatus.SUCCESS.getCode()) {
                    LOGGER.info("email sent to '{}' for event: {}", data, eventName);
                } else {
                    LOGGER.warn("email not sent for event {}, HttpStatusCode: {}, requestPayload: {}", eventName,
                        responseHandler.statusCode(), data.toString());
                }
            });

            // TODO: check for null payload
            emailRequest.putHeader(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON);
            emailRequest.putHeader(HttpConstants.HEADER_CONTENT_LENGTH,
                String.valueOf(data.toString().getBytes().length));
            emailRequest.putHeader(HttpConstants.HEADER_AUTH, getAuthorizationHeader(result));
            emailRequest.write(data.toString());
            emailRequest.end();
        });

        LOGGER.debug("done with sending email.. returning");
        return new JsonObject().put(EmailConstants.EMAIL_SENT, true).put(EmailConstants.STATUS,
            EmailConstants.STATUS_SUCCESS);
    }
    
    private JsonArray processEmailForResourceDelete() {
        return new EmailDataBuilder().setEmailTemplate(EmailConstants.TEMPLATE_RESOURCE_DELETE).setResultData(result)
            .build();
    }

    private JsonArray processEmailForCollectionCollaboratorUpdate() {
        return new EmailDataBuilder().setEmailTemplate(EmailConstants.TEMPLATE_COLLECTION_COLLABORATOR_INVITE)
            .setResultData(result).setEventData(message).build();
    }
    
    private JsonArray processEmailForCourseCollaboratorUpdate() {
        return new EmailDataBuilder().setEmailTemplate(EmailConstants.TEMPLATE_COURSE_COLLABORATOR_INVITE)
            .setResultData(result).setEventData(message).build();
    }
    
    private JsonArray processEmailForClassCollaboratorUpate() {
        return new EmailDataBuilder().setEmailTemplate(EmailConstants.TEMPLATE_CLASS_COLLABORATOR_INVITE)
            .setResultData(result).setEventData(message).build();
    }
    
    private JsonArray processEmailToInviteStudent() {
        // TODO: check class sharing and call email builder for open or
        // restricted class invite
        return new EmailDataBuilder().setEmailTemplate(EmailConstants.TEMPLATE_USER_INVITE_CLASS).setResultData(result)
            .build();
    }
    
    private JsonArray processEmailToFollowProfile() {
        return new EmailDataBuilder().setEmailTemplate(EmailConstants.TEMPLATE_PROFILE_FOLLOW).setResultData(result)
            .build();
    }

    private boolean validatePayload() {
        JsonObject payloadObject = result.getJsonObject(EventResponseConstants.PAYLOAD_OBJECT);
        if (payloadObject.isEmpty()) {
            LOGGER.warn("No payload found in event response");
            return false;
        }

        return true;
    }

    // TODO: Not sure whether we can make it singleton or return new instance
    // every time
    // as there will multiple email to send in single request.
    private HttpClient getHttpClient() {
        return vertx.createHttpClient(new HttpClientOptions().setDefaultHost(getAPIHost()));
    }

    private String getAPIHost() {
        JsonObject emailSettings = config.getJsonObject(KEY_EMAIL_SETTINGS);
        return emailSettings.getString(KEY_HOST);
    }

    private String getAPIEndPoint() {
        JsonObject emailSettings = config.getJsonObject(KEY_EMAIL_SETTINGS);
        return emailSettings.getString(KEY_ENDPOINT);
    }

    private String getAuthorizationHeader(JsonObject result) {
        // TODO: check for null session
        JsonObject session = result.getJsonObject(EventResponseConstants.SESSION);
        return "Token " + session.getString(EventResponseConstants.SESSION_TOKEN);
    }
}
