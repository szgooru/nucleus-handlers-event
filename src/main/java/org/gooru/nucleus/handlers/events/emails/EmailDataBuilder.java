package org.gooru.nucleus.handlers.events.emails;

import java.util.ArrayList;
import java.util.List;

import org.gooru.nucleus.handlers.events.constants.EmailConstants;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.processors.repositories.RepoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public final class EmailDataBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailDataBuilder.class);

    private String emailTemplate = null;
    private JsonObject input = null;

    public EmailDataBuilder() {
    }

    public EmailDataBuilder setEmailTemplate(String emailTemplate) {
        this.emailTemplate = emailTemplate;
        return this;
    }

    public EmailDataBuilder setInputData(JsonObject input) {
        this.input = input.copy();
        return this;
    }

    public JsonArray build() {
        if ((this.emailTemplate == null) || (this.input == null) || (this.emailTemplate.isEmpty())
            || (this.input.isEmpty())) {
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

        default:
            LOGGER.warn("no matching email template found to build email data");
        }

        return emailData;
    }

    private JsonArray buildCollectionCollaboratorUpdateEmailData() {
        JsonArray emailData = new JsonArray();

        String collectionId = input.getString(EventRequestConstants.ID);
        JsonArray collaboratorsAdded = input.getJsonArray(EventRequestConstants.COLLABORATORS_ADDED);

        LOGGER.debug("collaborators.add: {}", collaboratorsAdded.toString());
        List<String> userIds = new ArrayList<>();
        collaboratorsAdded.stream().forEach(collaborator -> userIds.add(collaborator.toString()));

        List<String> emailIds = RepoBuilder.buildUserRepo(null).getMultipleEmailIds(userIds);
        LOGGER.debug("no of email ids received: {}", emailIds.size());

        JsonObject collection = RepoBuilder.buildCollectionRepo(null).getCollection(collectionId);
        return emailData;

        // username
        // collection-title
        // serverpath
        // collection-id
        // recipient
    }

    private JsonArray buildClassCollaboratorUpdateEmailData() {
        // TODO Auto-generated method stub
        return null;
    }

    private JsonArray buildCourseCollaboratorUpdateEmailData() {
        // TODO Auto-generated method stub
        return null;
    }

    private JsonArray buildResourceDeleteEmailData() {
        // TODO Auto-generated method stub
        return null;
    }

    private JsonArray buildUserInviteToClassEmailData() {
        // TODO Auto-generated method stub
        return null;
    }

    private JsonArray buildUserInviteToOpenClassEmailData() {
        // TODO Auto-generated method stub
        return null;
    }

}
