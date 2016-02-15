/**
 * Utility package for generating appropriate response objects
 */
package org.gooru.nucleus.handlers.events.processors.responseobject;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;


/**
 * @author Subbu-Gooru
 *
 */
public final class ResponseFactory {
  public static JsonObject generateItemCreateResponse(JsonObject body, MultiMap headers, JsonObject response) {
      return new ResponseObject().setBody(body).setHeaders(headers).setResponse(response).setEventType(MessageConstants.EST_ITEM_CREATE).build();
    }

    public static JsonObject generateItemDeleteResponse(JsonObject body, MultiMap headers, JsonObject response) {
      return new ResponseObject().setBody(body).setHeaders(headers).setResponse(response).setEventType(MessageConstants.EST_ITEM_DELETE).build();
    }

    public static JsonObject generateItemEditResponse(JsonObject body, MultiMap headers, JsonObject response) {
      return new ResponseObject().setBody(body).setHeaders(headers).setResponse(response).setEventType(MessageConstants.EST_ITEM_EDIT).build();
    }

    public static JsonObject generateItemCopyResponse(JsonObject body, MultiMap headers, JsonObject response) {
      return new ResponseObject().setBody(body).setHeaders(headers).setResponse(response).setEventType(MessageConstants.EST_ITEM_COPY).build();
    }

    public static JsonObject generateItemMoveResponse(JsonObject body, MultiMap headers, JsonObject response) {
      return new ResponseObject().setBody(body).setHeaders(headers).setResponse(response).setEventType(MessageConstants.EST_ITEM_MOVE).build();
    }

    public static JsonObject generateErrorResponse(JsonObject body, MultiMap headers) {
      return new ResponseObject().setBody(body).setHeaders(headers).setEventType(MessageConstants.EST_ERROR).build();
    }

  private ResponseFactory() {
    throw new AssertionError();
  }
}
