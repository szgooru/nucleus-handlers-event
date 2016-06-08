package org.gooru.nucleus.handlers.events.processors.responseobject;

import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;

import io.vertx.core.json.JsonObject;

public class ItemCopyResponseObjectBuilder extends ResponseObject {

    public ItemCopyResponseObjectBuilder(JsonObject body, JsonObject response) {
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
        payloadStructure.put(EventResponseConstants.SOURCE, getSourceStructure());
        payloadStructure.put(EventResponseConstants.TARGET, getTargetStructure());
        payloadStructure.put(EventResponseConstants.CONTENT_FORMAT, getContentFormatFromResponse());
        payloadStructure.put(EventResponseConstants.SUB_EVENT_NAME, getSubEventName());
        return payloadStructure;
    }

    private JsonObject getSourceStructure() {
        JsonObject sourceStructure = new JsonObject();
        JsonObject sourceContent = response.getJsonObject(EventResponseConstants.SOURCE);
        if (sourceContent == null || sourceContent.isEmpty()) {
            return null;
        }
        
        sourceStructure.put(EventResponseConstants.CONTENT_GOORU_ID, getContentGooruId(sourceContent));
        sourceStructure.put(EventResponseConstants.PARENT_GOORU_ID, getParentGooruId(sourceContent));
        sourceStructure.put(EventResponseConstants.PARENT_CONTENT_ID, getParentContentId(sourceContent));
        sourceStructure.put(EventResponseConstants.ORIGINAL_CONTENT_ID, getOriginalContentId(sourceContent));
        updateCULCInfo(sourceContent, sourceStructure);
        return sourceStructure;
    }

    private JsonObject getTargetStructure() {
        JsonObject targetStructure = new JsonObject();
        JsonObject targetContent = response.getJsonObject(EventResponseConstants.TARGET);
        if (targetContent == null || targetContent.isEmpty()) {
            return null;
        }
        
        targetStructure.put(EventResponseConstants.CONTENT_GOORU_ID, getContentGooruId(targetContent));
        targetStructure.put(EventResponseConstants.PARENT_GOORU_ID, getParentGooruId(targetContent));
        targetStructure.put(EventResponseConstants.PARENT_CONTENT_ID, getParentContentId(targetContent));
        targetStructure.put(EventResponseConstants.ORIGINAL_CONTENT_ID, getOriginalContentId(targetContent));
        updateCULCInfo(targetContent, targetStructure);
        return targetStructure;
    }
}
