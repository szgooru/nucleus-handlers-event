/**
 * Utility package for generating appropriate response objects
 */
package org.gooru.nucleus.handlers.events.processors.responseobject;

import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;


/**
 * @author Subbu-Gooru
 *
 */
public final class ResponseFactory {
  public static JsonObject generateItemCreateResponse(JsonObject body, JsonObject response) {
      return new ResponseObject().setBody(body).setResponse(response).setEventType(MessageConstants.EST_ITEM_CREATE).build();
    }

    public static JsonObject generateItemDeleteResponse(JsonObject body, JsonObject response) {
      return new ResponseObject().setBody(body).setResponse(response).setEventType(MessageConstants.EST_ITEM_DELETE).build();
    }

    public static JsonObject generateItemEditResponse(JsonObject body, JsonObject response) {
      return new ResponseObject().setBody(body).setResponse(response).setEventType(MessageConstants.EST_ITEM_EDIT).build();
    }

    public static JsonObject generateItemCopyResponse(JsonObject body, JsonObject response) {
      return new ResponseObject().setBody(body).setResponse(response).setEventType(MessageConstants.EST_ITEM_COPY).build();
    }

    public static JsonObject generateItemMoveResponse(JsonObject body, JsonObject response) {
      return new ResponseObject().setBody(body).setResponse(response).setEventType(MessageConstants.EST_ITEM_MOVE).build();
    }

    public static JsonObject generateErrorResponse(JsonObject body) {
      return new ResponseObject().setBody(body).setEventType(MessageConstants.EST_ERROR).build();
    }

  private ResponseFactory() {
    throw new AssertionError();
  }
}
