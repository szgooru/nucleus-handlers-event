package org.gooru.nucleus.handlers.events.processors;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.gooru.nucleus.handlers.events.processors.exceptions.InvalidRequestException;
import org.gooru.nucleus.handlers.events.processors.MessageDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MessageProcessor implements Processor {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);
  private Message<Object> message;
  String userId;
  JsonObject prefs;
  JsonObject request;
  
  public MessageProcessor(Message<Object> message) {
    this.message = message;
  }
  
  @Override
  public boolean process() {
    boolean result;
    try {
      if (message == null) {
        LOGGER.error("Invalid message received, either null or body of message is not JsonObject ");
        throw new InvalidRequestException();
      }
      
      JsonObject msgObject = (JsonObject) message.body();
      final String msgOp = msgObject.getString(MessageConstants.MSG_EVENT_NAME);
      
      LOGGER.debug("MsgOp: " + msgOp);
      
      switch (msgOp) {
      case MessageConstants.MSG_OP_EVT_RES_CREATE:
        result = processEventResourceCreate();
        break;
      default:
        LOGGER.error("Invalid operation type passed in, not able to handle");
        throw new InvalidRequestException();
      }
      return result;
    } catch (InvalidRequestException e) {
      // TODO: handle exception
    }
    return true;
  }


  private boolean processEventResourceCreate() {
    JsonObject msgObject = (JsonObject) message.body();
    String eventName = msgObject.getString(MessageConstants.MSG_EVENT_NAME);
    JsonObject eventBody = msgObject.getJsonObject(MessageConstants.MSG_EVENT_BODY);

    return MessageDispatcher.getInstance().sendMessage2Kafka(eventName, eventBody);    
  }

}
