package org.gooru.nucleus.handlers.events.processors.responseobject;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;

public class ItemDeleteResponseObjectBuilder extends ResponseObject {

  public ItemDeleteResponseObjectBuilder(JsonObject body, JsonObject response) {
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
    String contentId = this.body.getJsonObject(EventRequestConstants.EVENT_BODY).getString(EventRequestConstants.ID);
    contextStructure.put(EventResponseConstants.CONTENT_GOORU_ID, contentId); // cannot be null
    contextStructure.put(EventResponseConstants.PARENT_CONTENT_ID, getParentContentId(response));
    contextStructure.put(EventResponseConstants.ORIGINAL_CONTENT_ID, getOriginalContentId(response));
    contextStructure.put(EventResponseConstants.CLASS_GOORU_ID, (Object)null);
    updateCULCInfo(response, contextStructure);
    
    contextStructure.put(EventResponseConstants.CLIENT_SOURCE, (Object) null);
    return contextStructure;
  }

  private JsonObject createPayLoadObjectStructure() {
    JsonObject payloadStructure = new JsonObject();
    
    String eventName = body.getString(EventRequestConstants.EVENT_NAME);
    if (eventName.equalsIgnoreCase(MessageConstants.MSG_OP_EVT_RESOURCE_DELETE)) {
      JsonArray refParentGooruIds = this.body.getJsonObject(EventRequestConstants.EVENT_BODY).getJsonArray(EventRequestConstants.COLLECTION_ID);
      payloadStructure.put(EventResponseConstants.REFERENCE_PARENT_GOORU_IDS, refParentGooruIds);
    }
    payloadStructure.put(EventResponseConstants.CONTENT_FORMAT, getContentFormatFromResponse());
    
    return payloadStructure;
  }


}
