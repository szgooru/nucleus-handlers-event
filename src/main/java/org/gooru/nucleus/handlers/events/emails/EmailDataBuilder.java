package org.gooru.nucleus.handlers.events.emails;

import java.util.ArrayList;
import java.util.List;

import org.gooru.nucleus.handlers.events.constants.EmailConstants;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;
import org.gooru.nucleus.handlers.events.processors.exceptions.InvalidRequestException;
import org.gooru.nucleus.handlers.events.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityClass;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityCollection;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityContent;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public final class EmailDataBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailDataBuilder.class);

    private String emailTemplate = null;
    private JsonObject result = null;

    public EmailDataBuilder() {
    }

    public EmailDataBuilder setEmailTemplate(String emailTemplate) {
        this.emailTemplate = emailTemplate;
        return this;
    }

    public EmailDataBuilder setResultData(JsonObject result) {
        this.result = result.copy();
        return this;
    }

    public JsonArray build() {
        if ((this.emailTemplate == null) || (this.result == null) || (this.emailTemplate.isEmpty())
            || (this.result.isEmpty())) {
            LOGGER.error("invalid data provided to build email data. returning");
            return new JsonArray();
        }

        JsonArray emailData = null;
        switch (emailTemplate) {

        case EmailConstants.TEMPLATE_COLLECTION_COLLABORATOR_INVITE:
            emailData = buildCollectionCollaboratorUpdateEmailData();
            break;

        case EmailConstants.TEMPLATE_CLASS_COLLABORATOR_INVITE:
            emailData = buildClassCollaboratorUpdateEmailData();
            break;

        case EmailConstants.TEMPLATE_COURSE_COLLABORATOR_INVITE:
            emailData = buildCourseCollaboratorUpdateEmailData();
            break;

        case EmailConstants.TEMPLATE_RESOURCE_DELETE:
            emailData = buildResourceDeleteEmailData();
            break;

        case EmailConstants.TEMPLATE_USER_INVITE_CLASS:
            emailData = buildUserInviteToClassEmailData();
            break;

        case EmailConstants.TEMPLATE_USER_INVITE_OPEN_CLASS:
            emailData = buildUserInviteToOpenClassEmailData();
            break;
            
        case EmailConstants.TEMPLATE_PROFILE_FOLLOW:
            emailData = buildProfileFollowEmailData();
            break;

        default:
            LOGGER.warn("no matching email template found to build email data");
        }

        return emailData;
    }

    private JsonArray buildCollectionCollaboratorUpdateEmailData() {
        JsonArray emailDataArray = new JsonArray();
        JsonObject data = getData();
        String collectionId = data.getString(EventRequestConstants.ID);
        JsonArray collaboratorsAdded = data.getJsonArray(EventRequestConstants.COLLABORATORS_ADDED);

        List<String> userIds = new ArrayList<>();
        collaboratorsAdded.stream().forEach(collaborator -> userIds.add(collaborator.toString()));

        List<String> emailIds = RepoBuilder.buildUserRepo(null).getMultipleEmailIds(userIds);
        LOGGER.debug("no of email ids received: {}", emailIds.size());

        JsonObject collection = RepoBuilder.buildCollectionRepo(null).getCollection(collectionId);
        String username = RepoBuilder.buildUserRepo(null).getUsername(getUserId());
        
        emailIds.stream().forEach(email -> {
            JsonObject emailData = new JsonObject();
            emailData.put(EmailConstants.MAIL_TEMPLATE_NAME, emailTemplate);
            emailData.put(EmailConstants.TO_ADDRESSES, email);
            
            JsonObject emailContextData = new JsonObject();
            emailContextData.put(EmailConstants.USERNAME, username);
            emailContextData.put(EmailConstants.COLLECTION_ID, collection.getString(AJEntityCourse.ID));
            emailData.put(EmailConstants.MAIL_TEMPLATE_CONTEXT, emailContextData);
            
            emailDataArray.add(emailData);
        });
        
        return emailDataArray;
    }

    private JsonArray buildClassCollaboratorUpdateEmailData() {
        JsonArray emailDataArray = new JsonArray();
        JsonObject data = getData();
        
        //String classId = data.getString(EventRequestConstants.ID);
        JsonArray collaboratorsAdded = data.getJsonArray(EventRequestConstants.COLLABORATORS_ADDED);
        List<String> userIds = new ArrayList<>();
        collaboratorsAdded.stream().forEach(collaborator -> userIds.add(collaborator.toString()));
       
        List<String> emailIds = RepoBuilder.buildUserRepo(null).getMultipleEmailIds(userIds);
        LOGGER.debug("no of email ids received: {}", emailIds.size());
        
        JsonObject classObj = RepoBuilder.buildClassRepo(null).getClassById();
        String username = RepoBuilder.buildUserRepo(null).getUsername(getUserId());
        String[] firstLastName = RepoBuilder.buildUserRepo(null).getFirstAndLastName(getUserId());
        
        emailIds.stream().forEach(email -> {
            JsonObject emailData = new JsonObject();
            emailData.put(EmailConstants.MAIL_TEMPLATE_NAME, emailTemplate);
            emailData.put(EmailConstants.TO_ADDRESSES, email);
            
            JsonObject emailContextData = new JsonObject();
            emailContextData.put(EmailConstants.USERNAME, username);
            emailContextData.put(EmailConstants.CLASS_ID, classObj.getString(AJEntityClass.ID));
            emailContextData.put(EmailConstants.FIRSTNAME, firstLastName[0]);
            emailContextData.put(EmailConstants.LASTNAME, firstLastName[1]);
            emailContextData.put(EmailConstants.CLASS_TITLE, classObj.getString(AJEntityClass.TITLE));
            emailData.put(EmailConstants.MAIL_TEMPLATE_CONTEXT, emailContextData);
            emailDataArray.add(emailData);
        });
        return emailDataArray;
    }

    private JsonArray buildCourseCollaboratorUpdateEmailData() {
        JsonArray emailDataArray = new JsonArray();
        JsonObject data = getData();
        String courseId = data.getString(EventRequestConstants.ID);
        JsonArray collaboratorsAdded = data.getJsonArray(EventRequestConstants.COLLABORATORS_ADDED);

        List<String> userIds = new ArrayList<>();
        collaboratorsAdded.stream().forEach(collaborator -> userIds.add(collaborator.toString()));

        List<String> emailIds = RepoBuilder.buildUserRepo(null).getMultipleEmailIds(userIds);
        LOGGER.debug("no of email ids received: {}", emailIds.size());

        JsonObject course = RepoBuilder.buildCourseRepo(null).getCourse(courseId);
        String username = RepoBuilder.buildUserRepo(null).getUsername(getUserId());
        
        emailIds.stream().forEach(email -> {
            JsonObject emailData = new JsonObject();
            emailData.put(EmailConstants.MAIL_TEMPLATE_NAME, emailTemplate);
            emailData.put(EmailConstants.TO_ADDRESSES, email);
            
            JsonObject emailContextData = new JsonObject();
            emailContextData.put(EmailConstants.USERNAME, username);
            emailContextData.put(EmailConstants.COURSE_ID, course.getString(AJEntityCollection.ID));
            emailData.put(EmailConstants.MAIL_TEMPLATE_CONTEXT, emailContextData);
            
            emailDataArray.add(emailData);
        });
        
        return emailDataArray;
    }

    private JsonArray buildResourceDeleteEmailData() {
        JsonObject payloadObject = result.getJsonObject(EventResponseConstants.PAYLOAD_OBJECT);
        JsonArray refCollectionIds = payloadObject.getJsonArray(EventResponseConstants.REFERENCE_PARENT_GOORU_IDS);
        List<String> ownerCreatorIds = RepoBuilder.buildCollectionRepo(null).getOwnerAndCreatorIds(refCollectionIds);
        List<String> emailIds = RepoBuilder.buildUserRepo(null).getMultipleEmailIds(ownerCreatorIds);
        
        JsonObject data = getData();
        String resourceId = data.getString(EventRequestConstants.ID);
        
        JsonObject resource = RepoBuilder.buildContentRepo(null).getResource(resourceId);
        String username = RepoBuilder.buildUserRepo(null).getUsername(getUserId());
        JsonArray emailDataArray = new JsonArray();
        
        emailIds.stream().forEach(email -> {
            JsonObject emailData = new JsonObject();
            emailData.put(EmailConstants.MAIL_TEMPLATE_NAME, emailTemplate);
            emailData.put(EmailConstants.TO_ADDRESSES, email);
            
            JsonObject emailContextData = new JsonObject();
            emailContextData.put(EmailConstants.USERNAME, username);
            emailContextData.put(EmailConstants.RESOURCE_TITLE, resource.getString(AJEntityContent.TITLE));
            //TODO: add all colletion ids in context data
            emailData.put(EmailConstants.MAIL_TEMPLATE_CONTEXT, emailContextData);
            emailDataArray.add(emailData);
            
        });
        
        return emailDataArray;
    }

    private JsonArray buildUserInviteToClassEmailData() {
        JsonObject payloadObject = result.getJsonObject(EventResponseConstants.PAYLOAD_OBJECT);
        JsonObject data = getData();
        JsonArray invitees = data.getJsonArray(EventRequestConstants.INVITEES);
        List<String> inviteesList = new ArrayList<>();
        invitees.stream().forEach(invitee -> inviteesList.add(invitee.toString()));
        // teachername
        // title
        // classCode
        // memberMailId
        //ProcessorContext context = createContext();
        //JsonObject classEntity = RepoBuilder.buildClassRepo(context).getClassById();
        return null;
    }

    private JsonArray buildUserInviteToOpenClassEmailData() {
        // TODO Auto-generated method stub
        return null;
    }
    
    private JsonArray buildProfileFollowEmailData() {
        JsonObject data = getData();
        String userId = data.getString(EventRequestConstants.USER_ID);
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        List<String> emailIds = RepoBuilder.buildUserRepo(null).getMultipleEmailIds(userIds);
        return null;
    }
    
    private JsonObject getData() {
        JsonObject payloadObject = result.getJsonObject(EventResponseConstants.PAYLOAD_OBJECT);
        JsonObject data = payloadObject.getJsonObject(EventResponseConstants.DATA);
        if (data.isEmpty()) {
            LOGGER.warn("no data found in payload object");
            throw new InvalidRequestException();
        }

        return data;
    }
    
    private String getUserId() {
        JsonObject user = result.getJsonObject(EventResponseConstants.USER);
        return user.getString(EventResponseConstants.GOORU_UID);
    }

}
