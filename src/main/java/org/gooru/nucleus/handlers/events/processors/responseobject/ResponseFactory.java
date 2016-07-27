/**
 * Utility package for generating appropriate response objects
 */
package org.gooru.nucleus.handlers.events.processors.responseobject;

import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;

/**
 * @author Subbu-Gooru
 */
public final class ResponseFactory {
    public static JsonObject generateItemCreateResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_ITEM_CREATE).build();
    }

    public static JsonObject generateItemDeleteResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_ITEM_DELETE).build();
    }

    public static JsonObject generateItemEditResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_ITEM_EDIT).build();
    }

    public static JsonObject generateItemCopyResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_ITEM_COPY).build();
    }

    public static JsonObject generateItemMoveResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_ITEM_MOVE).build();
    }

    public static JsonObject generateItemReorderResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_ITEM_REORDER).build();
    }

    public static JsonObject generateItemContentReorderResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_ITEM_CONTENT_REORDER).build();
    }

    public static JsonObject generateItemCollaboratorUpdateResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_ITEM_COLLABORATOR_UPDATE).build();
    }

    public static JsonObject generateItemContentAddResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_ITEM_CONTENT_ADD).build();
    }

    public static JsonObject generateJoinClassResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_JOIN_CLASS).build();
    }

    public static JsonObject generateClassContentVisibleResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_CLASS_CONTENT_VISIBILITY).build();
    }

    public static JsonObject generateAssignClassToCourseResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_ASSIGN_CLASS_COURSE).build();
    }

    public static JsonObject generateInviteStudentResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_INVITE_STUDENT).build();
    }

    public static JsonObject generateFollowUnfollowProfileResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_PROFILE_FOLLOW_UNFOLLOW).build();
    }

    public static JsonObject generateErrorResponse(JsonObject body) {
        return new ResponseObjectBuilder().setBody(body).setEventType(MessageConstants.EST_ERROR).build();
    }

    private ResponseFactory() {
        throw new AssertionError();
    }

    public static JsonObject generateClassStudentRemoveResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_REMOVE_STUDENT).build();
    }

    public static JsonObject generateItemRemoveResponse(JsonObject body, JsonObject response) {
        return new ResponseObjectBuilder().setBody(body).setResponse(response)
            .setEventType(MessageConstants.EST_ITEM_REMOVE).build();
    }
}
