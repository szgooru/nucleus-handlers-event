package org.gooru.nucleus.handlers.events.processors;


import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class MessageDispatcher {
  private static final MessageDispatcher INSTANCE = new MessageDispatcher();
  private static final Logger LOGGER = LoggerFactory.getLogger(MessageDispatcher.class);
  
  static final String kafkaServer = "localhost:9092";  // <TBD> : This needs to go to external file
  
  private MessageDispatcher() { 
    
  }
  
  public static MessageDispatcher getInstance() {
    return INSTANCE;
  }
  
  public boolean sendMessage2Kafka(String eventName, JsonObject eventBody) {
    //
    // Kafka message publish
    //
    Properties properties = new Properties();
    properties.put("metadata.broker.list", this.kafkaServer);
    properties.put("serializer.class","kafka.serializer.StringEncoder");
    
    ProducerConfig producerConfig = new ProducerConfig(properties);
    Producer<String,String> producer = new Producer<String, String>(producerConfig);
    KeyedMessage<String, String> kafkaMsg = new KeyedMessage<String, String>(eventName, eventBody.toString());

    LOGGER.debug("Message to Kafka server:" + kafkaMsg);

    producer.send(kafkaMsg);            
    producer.close();
    
    return true;
  }
}
