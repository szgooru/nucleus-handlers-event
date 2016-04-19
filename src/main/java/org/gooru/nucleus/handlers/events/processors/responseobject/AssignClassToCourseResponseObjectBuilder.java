package org.gooru.nucleus.handlers.events.processors.responseobject;

import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;

import io.vertx.core.json.JsonObject;

public class AssignClassToCourseResponseObjectBuilder extends ResponseObject {

    public AssignClassToCourseResponseObjectBuilder(JsonObject body, JsonObject response) {
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
            this.body.getJsonObject(EventRequestConstants.EVENT_BODY).getString(EventRequestConstants.ID);
        contextStructure.put(EventResponseConstants.CONTENT_GOORU_ID, contentId);
        contextStructure.put(EventResponseConstants.CLIENT_SOURCE, (Object) null);
        return contextStructure;
    }

    private JsonObject createPayLoadObjectStructure() {
        JsonObject payloadStructure = new JsonObject();
        payloadStructure.put(EventResponseConstants.DATA, this.response);
        payloadStructure.put(EventResponseConstants.CONTENT_FORMAT, getContentFormatFromResponse());
        payloadStructure.put(EventResponseConstants.SUB_EVENT_NAME, getSubEventName());
        return payloadStructure;
    }

}
