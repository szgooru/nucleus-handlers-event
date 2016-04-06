package org.gooru.nucleus.handlers.events.processors.responseobject;

import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;

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
    contextStructure.put(EventResponseConstants.CLIENT_SOURCE, (Object) null);
    return contextStructure;
  }

  private JsonObject createPayLoadObjectStructure() {
    JsonObject payloadStructure = new JsonObject();
    String contentId = this.body.getJsonObject(EventRequestConstants.EVENT_BODY).getString(EventRequestConstants.ID);

    payloadStructure.put(EventResponseConstants.DATA, this.response);
    payloadStructure.put(EventResponseConstants.CONTENT_ID, contentId);
    payloadStructure.put(EventResponseConstants.MODE, getModeFromResponse());
    payloadStructure.put(EventResponseConstants.ITEM_TYPE, getItemTypeFromResponse());
    payloadStructure.put(EventResponseConstants.TYPE, getContentFormatFromResponse());
    payloadStructure.put(EventResponseConstants.ITEM_SEQUENCE, getItemSequenceFromResponse());
    payloadStructure.put(EventResponseConstants.ITEM_ID, contentId);
    payloadStructure.put(EventResponseConstants.SOURCE_GOORU_ID, getSourceIDFromResponse());
    payloadStructure.put(EventResponseConstants.PARENT_CONTENT_ID, getParentIDFromResponse());

    return payloadStructure;
  }


}
