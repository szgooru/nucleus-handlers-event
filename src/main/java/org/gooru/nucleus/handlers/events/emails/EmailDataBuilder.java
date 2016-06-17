package org.gooru.nucleus.handlers.events.emails;

import java.util.ArrayList;
import java.util.Arrays;
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
    private JsonObject event = null;

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
    
    public EmailDataBuilder setEventData(JsonObject event) {
        this.event = event.copy();
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
        JsonObject eventData = getEventBody();
        String collectionId = eventData.getString(EventRequestConstants.ID);
        JsonArray collaboratorsAdded = eventData.getJsonArray(EventRequestConstants.COLLABORATORS_ADDED);

        List<String> userIds = new ArrayList<>(collaboratorsAdded.size());
        collaboratorsAdded.stream().forEach(collaborator -> userIds.add(collaborator.toString()));

        List<String> emailIds = RepoBuilder.buildUserRepo(null).getMultipleEmailIds(userIds);
        LOGGER.debug("Preparing data for email ids:{}", Arrays.toString(emailIds.toArray()));

        JsonObject collection = RepoBuilder.buildCollectionRepo(null).getCollection(collectionId);
        String username = RepoBuilder.buildUserRepo(null).getUsername(getUserId());

        emailIds.stream().forEach(email -> {
            JsonObject emailData = new JsonObject();
            emailData.put(EmailConstants.MAIL_TEMPLATE_NAME, emailTemplate);
            emailData.put(EmailConstants.TO_ADDRESSES, new JsonArray().add(email));

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
        JsonObject eventData = getEventBody();
        String classId = eventData.getString(EventRequestConstants.ID);
        JsonArray collaboratorsAdded = eventData.getJsonArray(EventRequestConstants.COLLABORATORS_ADDED);
        List<String> userIds = new ArrayList<>(collaboratorsAdded.size());
        collaboratorsAdded.stream().forEach(collaborator -> userIds.add(collaborator.toString()));

        List<String> emailIds = RepoBuilder.buildUserRepo(null).getMultipleEmailIds(userIds);
        LOGGER.debug("Preparing data for email ids:{}", Arrays.toString(emailIds.toArray()));

        JsonObject classObj = RepoBuilder.buildClassRepo(null).getClassById(classId);
        String username = RepoBuilder.buildUserRepo(null).getUsername(getUserId());
        String[] firstLastName = RepoBuilder.buildUserRepo(null).getFirstAndLastName(getUserId());

        emailIds.stream().forEach(email -> {
            JsonObject emailData = new JsonObject();
            emailData.put(EmailConstants.MAIL_TEMPLATE_NAME, emailTemplate);
            emailData.put(EmailConstants.TO_ADDRESSES, new JsonArray().add(email));

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
        JsonObject eventData = getEventBody();
        String courseId = eventData.getString(EventRequestConstants.ID);
        JsonArray collaboratorsAdded = eventData.getJsonArray(EventRequestConstants.COLLABORATORS_ADDED);

        List<String> userIds = new ArrayList<>(collaboratorsAdded.size());
        collaboratorsAdded.stream().forEach(collaborator -> userIds.add(collaborator.toString()));

        List<String> emailIds = RepoBuilder.buildUserRepo(null).getMultipleEmailIds(userIds);
        LOGGER.debug("Preparing data for email ids:{}", Arrays.toString(emailIds.toArray()));

        JsonObject course = RepoBuilder.buildCourseRepo(null).getCourse(courseId);
        String username = RepoBuilder.buildUserRepo(null).getUsername(getUserId());

        emailIds.stream().forEach(email -> {
            JsonObject emailData = new JsonObject();
            emailData.put(EmailConstants.MAIL_TEMPLATE_NAME, emailTemplate);
            emailData.put(EmailConstants.TO_ADDRESSES, new JsonArray().add(email));

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
        if (refCollectionIds != null && !refCollectionIds.isEmpty()) {
            List<String> ownerCreatorIds = RepoBuilder.buildCollectionRepo(null).getOwnerAndCreatorIds(refCollectionIds);
            List<String> emailIds = RepoBuilder.buildUserRepo(null).getMultipleEmailIds(ownerCreatorIds);
            LOGGER.debug("Preparing data for email ids:{}", Arrays.toString(emailIds.toArray()));
    
            JsonObject data = getData();
            String resourceId = data.getString(EventRequestConstants.ID);
    
            JsonObject resource = RepoBuilder.buildContentRepo(null).getResource(resourceId);
            String username = RepoBuilder.buildUserRepo(null).getUsername(getUserId());
            JsonArray emailDataArray = new JsonArray();
    
            emailIds.stream().forEach(email -> {
                JsonObject emailData = new JsonObject();
                emailData.put(EmailConstants.MAIL_TEMPLATE_NAME, emailTemplate);
                emailData.put(EmailConstants.TO_ADDRESSES, new JsonArray().add(email));
    
                JsonObject emailContextData = new JsonObject();
                emailContextData.put(EmailConstants.USERNAME, username);
                emailContextData.put(EmailConstants.RESOURCE_TITLE, resource.getString(AJEntityContent.TITLE));
                emailData.put(EmailConstants.MAIL_TEMPLATE_CONTEXT, emailContextData);
                emailDataArray.add(emailData);
            });
            return emailDataArray;
        }

        return null;
    }

    private JsonArray buildUserInviteToClassEmailData() {
        JsonObject data = getData();
        JsonArray invitees = data.getJsonArray(EventRequestConstants.INVITEES);
        List<String> inviteesList = new ArrayList<>(invitees.size());
        invitees.stream().forEach(invitee -> inviteesList.add(invitee.toString()));

        //List<String> emailIds = RepoBuilder.buildUserRepo(null).getMultipleEmailIds(inviteesList);
        LOGGER.debug("Preparing data for email ids:{}", Arrays.toString(inviteesList.toArray()));
        String classId = data.getString(EventRequestConstants.ID);
        JsonObject classObj = RepoBuilder.buildClassRepo(null).getClassById(classId);
        String teacherUsername =
            RepoBuilder.buildUserRepo(null).getUsername(classObj.getString(AJEntityClass.CREATOR_ID));
        JsonArray emailDataArray = new JsonArray();

        inviteesList.stream().forEach(email -> {
            JsonObject emailData = new JsonObject();
            emailData.put(EmailConstants.MAIL_TEMPLATE_NAME, emailTemplate);
            emailData.put(EmailConstants.TO_ADDRESSES, new JsonArray().add(email));

            JsonObject emailContextData = new JsonObject();
            emailContextData.put(EmailConstants.TEACHER_USERNAME, teacherUsername);
            emailContextData.put(EmailConstants.CLASS_TITLE, classObj.getString(AJEntityClass.TITLE));
            emailContextData.put(EmailConstants.CLASS_CODE, classObj.getString(AJEntityClass.CODE));
            emailContextData.put(EmailConstants.CLASS_ID, classObj.getString(AJEntityClass.ID));
            emailContextData.put(EmailConstants.EMAIL_ID, email);
            emailData.put(EmailConstants.MAIL_TEMPLATE_CONTEXT, emailContextData);
            emailDataArray.add(emailData);

        });

        return emailDataArray;
    }

    private JsonArray buildUserInviteToOpenClassEmailData() {
        JsonObject data = getData();
        JsonArray invitees = data.getJsonArray(EventRequestConstants.INVITEES);
        List<String> inviteesList = new ArrayList<>(invitees.size());
        invitees.stream().forEach(invitee -> inviteesList.add(invitee.toString()));

        List<String> emailIds = RepoBuilder.buildUserRepo(null).getMultipleEmailIds(inviteesList);
        LOGGER.debug("Preparing data for email ids:{}", Arrays.toString(emailIds.toArray()));
        String classId = data.getString(EventRequestConstants.ID);
        JsonObject classObj = RepoBuilder.buildClassRepo(null).getClassById(classId);
        String teacherUsername =
            RepoBuilder.buildUserRepo(null).getUsername(classObj.getString(AJEntityClass.CREATOR_ID));
        JsonArray emailDataArray = new JsonArray();

        emailIds.stream().forEach(email -> {
            JsonObject emailData = new JsonObject();
            emailData.put(EmailConstants.MAIL_TEMPLATE_NAME, emailTemplate);
            emailData.put(EmailConstants.TO_ADDRESSES, new JsonArray().add(email));

            JsonObject emailContextData = new JsonObject();
            emailContextData.put(EmailConstants.TEACHER_USERNAME, teacherUsername);
            emailContextData.put(EmailConstants.CLASS_TITLE, classObj.getString(AJEntityClass.TITLE));
            emailContextData.put(EmailConstants.CLASS_CODE, classObj.getString(AJEntityClass.CODE));
            emailContextData.put(EmailConstants.CLASS_ID, classObj.getString(AJEntityClass.ID));
            emailContextData.put(EmailConstants.EMAIL_ID, email);
            emailData.put(EmailConstants.MAIL_TEMPLATE_CONTEXT, emailContextData);
            emailDataArray.add(emailData);

        });

        return emailDataArray;
    }

    private JsonArray buildProfileFollowEmailData() {
        JsonObject data = getData();
        String username = RepoBuilder.buildUserRepo(null).getUsername(getUserId());
        List<String> userIds = new ArrayList<>(1);
        userIds.add(data.getString(EventRequestConstants.FOLLOW_ON_USER_ID));
        List<String> emailIds = RepoBuilder.buildUserRepo(null).getMultipleEmailIds(userIds);
        LOGGER.debug("Preparing data for email ids:{}", Arrays.toString(emailIds.toArray()));
        JsonArray emailDataArray = new JsonArray();
        emailIds.stream().forEach(email -> {
            JsonObject emailData = new JsonObject();
            emailData.put(EmailConstants.MAIL_TEMPLATE_NAME, emailTemplate);
            emailData.put(EmailConstants.TO_ADDRESSES, new JsonArray().add(email));

            JsonObject emailContextData = new JsonObject();
            emailContextData.put(EmailConstants.USERNAME, username);
            emailData.put(EmailConstants.MAIL_TEMPLATE_CONTEXT, emailContextData);
            emailDataArray.add(emailData);
        });
        return emailDataArray;
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
    
    private JsonObject getEventBody() {
        return this.event.getJsonObject(EventRequestConstants.EVENT_BODY);
    }

    private String getUserId() {
        JsonObject user = result.getJsonObject(EventResponseConstants.USER);
        return user.getString(EventResponseConstants.GOORU_UID);
    }

}
