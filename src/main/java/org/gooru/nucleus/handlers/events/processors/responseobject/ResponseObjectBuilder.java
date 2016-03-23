package org.gooru.nucleus.handlers.events.processors.responseobject;

import java.util.Date;

import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;


/**
 * @author Subbu-Gooru
 *
 */
public final class ResponseObjectBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(ResponseObjectBuilder.class);

  private JsonObject body = null;
  private JsonObject response = null;
  private int eventType = MessageConstants.EST_ERROR;

  public ResponseObjectBuilder() {
  }

  // Setters for headers, body and response
  public ResponseObjectBuilder setBody(JsonObject input) {
    this.body = input.copy();
    return this;
  }

  public ResponseObjectBuilder setResponse(JsonObject input) {
    this.response = input.copy();
    return this;
  }

  public ResponseObjectBuilder setEventType(int type) {
    this.eventType = type;
    return this;
  }
  
  public JsonObject build() {
    JsonObject result;
    if ((this.response == null) || (this.body == null)) {
      LOGGER.error("Can't create response with invalid response. Will return internal error");
      result = buildFailureResponseObject();
    } else {
      switch (this.eventType) {
        case MessageConstants.EST_ERROR :
          result = buildFailureResponseObject();
          break;
        case MessageConstants.EST_ITEM_CREATE :
        case MessageConstants.EST_ITEM_EDIT :
        case MessageConstants.EST_ITEM_COPY :
          result = buildItemCreateUpdateCopyResponseObject();
          break;
        case MessageConstants.EST_ITEM_MOVE :
          result = buildItemMoveResponseObject();
          break;
        case MessageConstants.EST_ITEM_DELETE :
          result = buildItemDeleteResponseObject();
          break;
        case MessageConstants.EST_ITEM_REORDER:
          result = buildItemReorderResponseObject();
          break;
        case MessageConstants.EST_ITEM_CONTENT_REORDER:
          result = buildItemContentReorderResponseObject();
          break;
        case MessageConstants.EST_ITEM_COLLABORATOR_UPDATE:
          result = buildItemCollaboratorUpdate();
          break;
        case MessageConstants.EST_ITEM_CONTENT_ADD:
          result = buildItemContentAdd();
          break;
        default :
          LOGGER.error("Invalid event type seen. Do not know how to handle. Will return failure object.");
          result = buildFailureResponseObject();
          break;
      }
    }

    return result;
  }

  private JsonObject buildItemCreateUpdateCopyResponseObject() {
    return new ItemCreateUpdateCopyResponseObjectBuilder(body, response).build();
  }
  
  private JsonObject buildItemMoveResponseObject() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject buildItemDeleteResponseObject() {
    return new ItemDeleteResponseObjectBuilder(body, response).build();
  }

  private JsonObject buildItemReorderResponseObject() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject buildItemContentReorderResponseObject() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject buildItemCollaboratorUpdate() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject buildItemContentAdd() {
    // TODO Auto-generated method stub
    return null;
  }

  private  JsonObject buildFailureResponseObject() {
    JsonObject failureJson = new JsonObject();
    failureJson.put(MessageConstants.MSG_EVENT_TIMESTAMP, new Date().toString());
    failureJson.put(MessageConstants.MSG_EVENT_DUMP, this.body);
    return failureJson;
  }
}

