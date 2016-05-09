package org.gooru.nucleus.handlers.events.processors.responseobject;

import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;
import org.gooru.nucleus.handlers.events.processors.repositories.RepoBuilder;

import io.vertx.core.json.JsonObject;

public class ItemContentAddResponseObjectBuilder extends ResponseObject {

    private String contentFormat;
    private String contentId;
    
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
        contentId =
            this.body.getJsonObject(EventRequestConstants.EVENT_BODY).getString(EventRequestConstants.CONTENT_ID);
        contextStructure.put(EventResponseConstants.CONTENT_GOORU_ID, contentId);
        contextStructure.put(EventResponseConstants.CLIENT_SOURCE, (Object) null);
        return contextStructure;
    }

    private JsonObject createPayLoadObjectStructure() {
        JsonObject payloadStructure = new JsonObject();
        contentFormat = RepoBuilder.buildContentRepo(null).getContentFormatById(contentId);
        payloadStructure.put(EventResponseConstants.TARGET, getTargetStructure());
        payloadStructure.put(EventResponseConstants.CONTENT_FORMAT, contentFormat);
        payloadStructure.put(EventResponseConstants.SUB_EVENT_NAME, getSubEventName());
        return payloadStructure;
    }

    private JsonObject getTargetStructure() {
        JsonObject targetStructure = new JsonObject();
        String parentGooruId = this.body.getJsonObject(EventRequestConstants.EVENT_BODY).getString(EventRequestConstants.ID);
        targetStructure.put(EventResponseConstants.PARENT_GOORU_ID, parentGooruId);
        JsonObject content = null;
        if (contentFormat.equalsIgnoreCase(EventResponseConstants.FORMAT_RESOUCE)) {
            content = RepoBuilder.buildContentRepo(null).getResource(contentId);
        } else {
            content = RepoBuilder.buildContentRepo(null).getQuestion(contentId);
        }
        
        targetStructure.put(EventResponseConstants.PARENT_CONTENT_ID, getParentContentId(content));
        targetStructure.put(EventResponseConstants.ORIGINAL_CONTENT_ID, getOriginalContentId(content));

        String courseId = getCourseId(response);
        String classIds = null;
        if (courseId != null) {
            classIds = RepoBuilder.buildClassRepo(null).getClassIdsForCourse(courseId);
        }
        targetStructure.put(EventResponseConstants.CLASS_GOORU_ID, classIds);
        updateCULCInfo(response, targetStructure);
        return targetStructure;
    }

}
