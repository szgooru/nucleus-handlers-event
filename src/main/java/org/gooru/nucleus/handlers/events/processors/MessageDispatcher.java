package org.gooru.nucleus.handlers.events.processors;

import org.gooru.nucleus.handlers.events.app.components.KafkaRegistry;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.BufferExhaustedException;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.errors.InterruptException;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MessageDispatcher {
  private static final MessageDispatcher INSTANCE = new MessageDispatcher();
  private static final Logger LOGGER = LoggerFactory.getLogger(MessageDispatcher.class);
  
  private MessageDispatcher() { 
  }
  
  public static MessageDispatcher getInstance() {
    return INSTANCE;
  }
  
  public void sendMessage2Kafka(String eventName, JsonObject eventBody) {
    //
    // Kafka message publish
    //
    if ( KafkaRegistry.getInstance().isDevEnvironment() ) return; // running without KafkaServer...
    
    Producer<String,String> producer = KafkaRegistry.getInstance().getKafkaProducer();
    ProducerRecord<String, String> kafkaMsg = new ProducerRecord<String, String>(KafkaRegistry.getInstance().getKafkaTopic(), eventName, eventBody.toString());

    LOGGER.debug("Message to Kafka server:" + kafkaMsg);

    try {
      producer.send(kafkaMsg, new Callback() {
        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if (exception == null) {
              LOGGER.info("Message Delivered Successfully: Offset : " + metadata.offset() + " : Topic : " + metadata.topic() + " : Partition : " + metadata.partition() + " : Message : " + kafkaMsg);
            } else {
              LOGGER.error("Message Could not be delivered : " + kafkaMsg + ". Cause: " + exception.getMessage());
            }
        }
      });
      
      LOGGER.debug("Message Sent Successfully: " + kafkaMsg);
      
    } catch (InterruptException ie) {
      //   - If the thread is interrupted while blocked
      LOGGER.error("SendMesage2Kafka: to Kafka server:", ie);
    } catch (SerializationException se) {
      //   - If the key or value are not valid objects given the configured serializers
      LOGGER.error("SendMesage2Kafka: to Kafka server:", se);
    } catch (BufferExhaustedException be) {
      //   - If block.on.buffer.full=false and the buffer is full.
      LOGGER.error("SendMesage2Kafka: to Kafka server:", be);
    }
  }
  
}
