package org.gooru.nucleus.handlers.events.app.components;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.events.bootstrap.shutdown.Finalizer;
import org.gooru.nucleus.handlers.events.bootstrap.startup.Initializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;
import java.util.Map;


public class KafkaRegistry implements Initializer, Finalizer {

  private static final String DEFAULT_KAFKA_SETTINGS = "defaultKafkaSettings";
  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaRegistry.class);
  private Producer<String, String> kafkaProducer;
  boolean initialized = false;
  
  @Override
  public void initializeComponent(Vertx vertx, JsonObject config) {
    // Skip if we are already initialized
    LOGGER.debug("Initialization called upon.");
    if (!initialized) {
      LOGGER.debug("May have to do initialization");
      // We need to do initialization, however, we are running it via verticle instance which is going to run in 
      // multiple threads hence we need to be safe for this operation
      synchronized (Holder.INSTANCE) {
        LOGGER.debug("Will initialize after double checking");
        if (!initialized) {
          LOGGER.debug("Initializing KafkaRegistry now");
          JsonObject kafkaConfig = config.getJsonObject(DEFAULT_KAFKA_SETTINGS);
          this.kafkaProducer = initializeKafkaPublisher(kafkaConfig);
          initialized = true;
          LOGGER.debug("Initializing KafkaRegistry DONE");          
        }
      }
    }
  }
  

  private Producer<String, String> initializeKafkaPublisher(JsonObject kafkaConfig) {
    LOGGER.debug("InitializeKafkaPublisher now...");

    final Properties properties = new Properties();
    
    for (Map.Entry<String, Object> entry : kafkaConfig) {
      switch (entry.getKey()) {
        case ProducerConfig.BOOTSTRAP_SERVERS_CONFIG :  
          properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, (String) entry.getValue());          
          break;
        case ProducerConfig.RETRIES_CONFIG:
          properties.put(ProducerConfig.RETRIES_CONFIG,(Integer) entry.getValue());
          break;
//        case ProducerConfig.ACKS_CONFIG:
//          properties.put(ProducerConfig.ACKS_CONFIG,(String) entry.getValue());
//          break;
        case ProducerConfig.BATCH_SIZE_CONFIG:
          properties.put(ProducerConfig.BATCH_SIZE_CONFIG,(Integer) entry.getValue());
          break;
        case ProducerConfig.LINGER_MS_CONFIG:
          properties.put(ProducerConfig.LINGER_MS_CONFIG,(Integer) entry.getValue());
          break;
        case ProducerConfig.BUFFER_MEMORY_CONFIG:
          properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,(Integer) entry.getValue());
          break;
        case ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG:
          properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,(String) entry.getValue());
          break;
        case ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG:
          properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,(String) entry.getValue());
          break;       
      }
    }
    LOGGER.debug("InitializeKafkaPublisher properties created...");
    Producer<String, String> producer = new KafkaProducer<String, String>(properties);

    LOGGER.debug("InitializeKafkaPublisher producer created successfully!");
    
    return producer;
  }

  public Producer<String, String> getKafkaProducer() {
    if (initialized) {
      return this.kafkaProducer;
    }
    return null;
  }

  @Override
  public void finalizeComponent() {
    if (this.kafkaProducer != null) {
      this.kafkaProducer.close();
      this.kafkaProducer = null;
    }
  }
  
  public static KafkaRegistry getInstance() {
    return Holder.INSTANCE;
  }

  private KafkaRegistry() {
    // TODO Auto-generated constructor stub
  }
  
  private static class Holder {
    private static KafkaRegistry INSTANCE = new KafkaRegistry();
  }

}
