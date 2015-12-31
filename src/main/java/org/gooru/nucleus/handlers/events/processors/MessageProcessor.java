package org.gooru.nucleus.handlers.events.processors;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.gooru.nucleus.handlers.events.processors.exceptions.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import java.util.Properties;

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
      final String msgOp = msgObject.getString(MessageConstants.MSG_EVENT_OP);
//      request = message.body().getString(MessageConstants.MSG_EVENT_BODY);
      
      LOGGER.debug("MsgOp: " + msgOp);
      
      switch (msgOp) {
      case MessageConstants.MSG_OP_EVT_RES_GET:
        result = processEventResourceGet();
        break;
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
    // TODO Auto-generated method stub
    
    return true;
  }

  private boolean processEventResourceGet() {
    // TODO Auto-generated method stub
    JsonObject msgObject = (JsonObject) message.body();
    LOGGER.debug("processEventResourceGet .... " + this.message.body());
    JsonObject result = msgObject.getJsonObject(MessageConstants.MSG_EVENT_BODY);
    
    LOGGER.debug("Trying to connect to Kafka server ..." + result.toString());
    //
    // testing for Kafka message publish
    //
    Properties properties = new Properties();
    properties.put("metadata.broker.list","192.168.1.7:9092");
    properties.put("serializer.class","kafka.serializer.StringEncoder");
    
    LOGGER.debug("Trying to connect to Kafka server ...Properties Set" );
    ProducerConfig producerConfig = new ProducerConfig(properties);

    LOGGER.debug("Trying to connect to Kafka server ...Producer CONFIG done." );
    Producer<String,String> producer = new Producer<String, String>(producerConfig);        
    
    LOGGER.debug("Trying to connect to Kafka server ...");
    KeyedMessage<String, String> kafkaMsg = new KeyedMessage<String, String>(msgObject.getString(MessageConstants.MSG_EVENT_OP), result.toString() );
    LOGGER.debug("Trying to send message to Kafka server ..." + kafkaMsg);

    producer.send(kafkaMsg);            
    producer.close();
    LOGGER.debug("Closing producer");
    
    return true;
  }
}
