package org.gooru.nucleus.handlers.events.processors.responseobject;

import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;
import org.gooru.nucleus.handlers.events.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityCollection;

import io.vertx.core.json.JsonObject;

public class ItemRemoveResponseObjectBuilder extends ResponseObject {

    protected ItemRemoveResponseObjectBuilder(JsonObject body, JsonObject response) {
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
        JsonObject eventBody = this.body.getJsonObject(EventRequestConstants.EVENT_BODY);
        String contentId = eventBody.getString(EventRequestConstants.ID);
        contextStructure.put(EventResponseConstants.CONTENT_GOORU_ID, contentId);
        contextStructure.put(EventResponseConstants.PARENT_CONTENT_ID, getParentContentId(response));
        contextStructure.put(EventResponseConstants.ORIGINAL_CONTENT_ID, getOriginalContentId(response));
        contextStructure.put(EventResponseConstants.COURSE_GOORU_ID, eventBody.getString(EventRequestConstants.COURSE_ID));
        contextStructure.put(EventResponseConstants.UNIT_GOORU_ID, eventBody.getString(EventRequestConstants.UNIT_ID));
        contextStructure.put(EventResponseConstants.LESSON_GOORU_ID, eventBody.getString(EventRequestConstants.LESSON_ID));

        String courseId = eventBody.getString(EventRequestConstants.COURSE_ID);
        String classIds = null;
        if (courseId != null) {
            classIds = RepoBuilder.buildClassRepo(null).getClassIdsForCourse(courseId);
        }
        contextStructure.put(EventResponseConstants.CLASS_GOORU_ID, classIds);
        contextStructure.put(EventResponseConstants.CLIENT_SOURCE, (Object) null);
        return contextStructure;
    }
    
    private JsonObject createPayLoadObjectStructure() {
        JsonObject payloadStructure = new JsonObject();
        String contentFormat = response.getString(AJEntityCollection.FORMAT);
        payloadStructure.put(EventResponseConstants.CONTENT_FORMAT, contentFormat);
        payloadStructure.put(EventResponseConstants.SUB_EVENT_NAME, getSubEventName());
        return payloadStructure;
    }

    
}
