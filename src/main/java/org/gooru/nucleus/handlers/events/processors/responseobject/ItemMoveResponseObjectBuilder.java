package org.gooru.nucleus.handlers.events.processors.responseobject;

import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;

import io.vertx.core.json.JsonObject;

public class ItemMoveResponseObjectBuilder extends ResponseObject {

  public ItemMoveResponseObjectBuilder(JsonObject body, JsonObject response) {
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
    payloadStructure.put(EventResponseConstants.SOURCE, getSourceStructure());
    payloadStructure.put(EventResponseConstants.TARGET, getTargetStructure());
    payloadStructure.put(EventResponseConstants.CONTENT_FORMAT, getContentFormatFromResponse());
    return payloadStructure;
  }

  private JsonObject getTargetStructure() {
    JsonObject sourceStructure = new JsonObject();
    JsonObject sourceContent = response.getJsonObject(EventResponseConstants.SOURCE);
    sourceStructure.put(EventResponseConstants.CONTENT_GOORU_ID, getContentGooruId(sourceContent));
    sourceStructure.put(EventResponseConstants.PARENT_GOORU_ID, (Object)null);
    sourceStructure.put(EventResponseConstants.PARENT_CONTENT_ID, getParentContentId(sourceContent));
    sourceStructure.put(EventResponseConstants.ORIGINAL_CONTENT_ID, getOriginalContentId(sourceContent));
    sourceStructure.put(EventResponseConstants.CLASS_GOORU_ID, (Object)null);
    updateCULCInfo(sourceContent, sourceStructure);
    return sourceStructure;
  }

  private JsonObject getSourceStructure() {
    JsonObject targetStructure = new JsonObject();
    JsonObject targetContent = response.getJsonObject(EventResponseConstants.TARGET);
    targetStructure.put(EventResponseConstants.CONTENT_GOORU_ID, getContentGooruId(targetContent));
    targetStructure.put(EventResponseConstants.PARENT_GOORU_ID, (Object)null);
    targetStructure.put(EventResponseConstants.PARENT_CONTENT_ID, getParentContentId(targetContent));
    targetStructure.put(EventResponseConstants.ORIGINAL_CONTENT_ID, getOriginalContentId(targetContent));
    targetStructure.put(EventResponseConstants.CLASS_GOORU_ID, (Object)null);
    updateCULCInfo(targetContent, targetStructure);
    return targetStructure;
  }

}
