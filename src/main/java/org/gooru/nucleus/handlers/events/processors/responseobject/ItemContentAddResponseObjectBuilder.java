package org.gooru.nucleus.handlers.events.processors.responseobject;

import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;
import org.gooru.nucleus.handlers.events.processors.repositories.RepoBuilder;

import io.vertx.core.json.JsonObject;

public class ItemContentAddResponseObjectBuilder extends ResponseObject {

    protected ItemContentAddResponseObjectBuilder(JsonObject body, JsonObject response) {
        super(body, response);
    }

    public JsonObject build() {
        JsonObject eventStructure = createGenericStructure();
        eventStructure.put(EventResponseConstants.METRICS, createMetricsStructure());
        eventStructure.put(EventResponseConstants.SESSION, createSessionStructure());
        eventStructure.put(EventResponseConstants.USER, createUserStructure());
        eventStructure.put(EventResponseConstants.VERSION, createVersionStructure());
        eventStructure.put(EventResponseConstants.CONTEXT, createContextStructure());
        eventStructure.put(EventResponseConstants.PAYLOAD_OBJECT, createPayLoadObjectStructure());
        return eventStructure;
    }

    private JsonObject createContextStructure() {
        JsonObject contextStructure = new JsonObject();
        String contentId =
            this.body.getJsonObject(EventRequestConstants.EVENT_BODY).getString(EventRequestConstants.CONTENT_ID);
        contextStructure.put(EventResponseConstants.CONTENT_GOORU_ID, contentId);
        contextStructure.put(EventResponseConstants.CLIENT_SOURCE, (Object) null);
        return contextStructure;
    }

    private JsonObject createPayLoadObjectStructure() {
        JsonObject payloadStructure = new JsonObject();
        payloadStructure.put(EventResponseConstants.TARGET, getTargetStructure());
        String contentId =
            this.body.getJsonObject(EventRequestConstants.EVENT_BODY).getString(EventRequestConstants.CONTENT_ID);
        String contentFormat = RepoBuilder.buildContentRepo(null).getContentFormatById(contentId);
        payloadStructure.put(EventResponseConstants.CONTENT_FORMAT, contentFormat);
        payloadStructure.put(EventResponseConstants.SUB_EVENT_NAME, getSubEventName());
        return payloadStructure;
    }

    private JsonObject getTargetStructure() {
        JsonObject targetStructure = new JsonObject();
        JsonObject targetContent = new JsonObject();
        targetStructure.put(EventResponseConstants.PARENT_GOORU_ID, getParentGooruId(response));
        targetStructure.put(EventResponseConstants.PARENT_CONTENT_ID, getParentContentId(response));
        targetStructure.put(EventResponseConstants.ORIGINAL_CONTENT_ID, getOriginalContentId(response));

        String courseId = getCourseId(response);
        String classIds = null;
        if (courseId != null) {
            classIds = RepoBuilder.buildClassRepo(null).getClassIdsForCourse(courseId);
        }
        targetStructure.put(EventResponseConstants.CLASS_GOORU_ID, classIds);
        updateCULCInfo(targetContent, targetStructure);
        return targetStructure;
    }

}
