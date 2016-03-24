package org.gooru.nucleus.handlers.events.processors.responseobject;

import org.gooru.nucleus.handlers.events.constants.EventResoponseConstants;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;

import io.vertx.core.json.JsonObject;

public class ItemDeleteResponseObjectBuilder extends ResponseObject {

  public ItemDeleteResponseObjectBuilder(JsonObject body, JsonObject response) {
    super(body, response);
  }

  public JsonObject build() {
    JsonObject eventStructure = createGenericStructure();
    eventStructure.put(EventResoponseConstants.METRICS, createMetricsStructure());
    eventStructure.put(EventResoponseConstants.SESSION, createSessionStructure());
    eventStructure.put(EventResoponseConstants.USER, createUserStructure());
    eventStructure.put(EventResoponseConstants.VERSION, createVersionStructure());
    eventStructure.put(EventResoponseConstants.CONTEXT, createContextStructure());
    eventStructure.put(EventResoponseConstants.PAYLOAD_OBJECT, createPayLoadObjectStructure());
    return eventStructure;
  }
  
  private JsonObject createPayLoadObjectStructure() {
    JsonObject payloadStructure = new JsonObject();
    String contentId = this.body.getJsonObject(MessageConstants.MSG_EVENT_BODY).getString(MessageConstants.MSG_EVENT_CONTENT_ID);
    
    payloadStructure.put(EventResoponseConstants.DATA, this.response);
    payloadStructure.put(EventResoponseConstants.CONTENT_ID, contentId);
    payloadStructure.put(EventResoponseConstants.MODE, getModeFromResponse());
    payloadStructure.put(EventResoponseConstants.ITEM_TYPE, getItemTypeFromResponse());
    payloadStructure.put(EventResoponseConstants.TYPE,  getTypeFromResponse());
    payloadStructure.put(EventResoponseConstants.ITEM_SEQUENCE, getItemSequenceFromResponse());
    payloadStructure.put(EventResoponseConstants.ITEM_ID, contentId);    
    payloadStructure.put(EventResoponseConstants.SOURCE_GOORU_ID, getSourceIDFromResponse());
    payloadStructure.put(EventResoponseConstants.PARENT_CONTENT_ID, getParentIDFromResponse());
    
    return payloadStructure;
  }
  

}
