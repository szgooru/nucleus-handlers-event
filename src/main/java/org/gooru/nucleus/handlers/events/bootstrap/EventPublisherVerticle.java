package org.gooru.nucleus.handlers.events.bootstrap;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.events.bootstrap.shutdown.Finalizer;
import org.gooru.nucleus.handlers.events.bootstrap.shutdown.Finalizers;
import org.gooru.nucleus.handlers.events.bootstrap.startup.Initializer;
import org.gooru.nucleus.handlers.events.bootstrap.startup.Initializers;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.gooru.nucleus.handlers.events.constants.MessagebusEndpoints;
import org.gooru.nucleus.handlers.events.processors.ProcessorBuilder;
import org.gooru.nucleus.handlers.events.processors.MessageDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ashish on 25/12/15.
 */
public class EventVerticle extends AbstractVerticle {

  static final Logger LOGGER = LoggerFactory.getLogger(EventVerticle.class);

  @Override
  public void start(Future<Void> voidFuture) throws Exception {

    vertx.executeBlocking(blockingFuture -> {
      startApplication();
      blockingFuture.complete();
    }, future -> {
      if (future.succeeded()) {
        LOGGER.info("Successfully initialized application machinery");
        voidFuture.complete();
      } else {
        voidFuture.fail("Not able to initialize the Resource machinery properly");
      }
    });

    EventBus eb = vertx.eventBus();

    eb.consumer(MessagebusEndpoints.MBEP_EVENT, message -> {

      LOGGER.debug("Received message: " + message.body());

      vertx.executeBlocking(future -> {
        // do blocking database interactions here...and collect the return value which will be proccessed in the 
        // onSuccessful completion handler....which would be to just dispatch the message.
        JsonObject result = new ProcessorBuilder(message).build().process();
        future.complete(result);
        
      }, res -> {
        
        if (res.succeeded()) {
          JsonObject result = (JsonObject)res.result();
          LOGGER.debug("Successful!! Now dispatch message: " + result.toString());
          
          String eventName = result.getString(MessageConstants.MSG_EVENT_NAME);
          JsonObject eventBody = result.getJsonObject(MessageConstants.MSG_EVENT_BODY);
          
          MessageDispatcher.getInstance().sendMessage2Kafka(eventName, eventBody);    
                    
        } else {
          LOGGER.error("Error processing the database interactions!!");          
        }

      });


    }).completionHandler(result -> {
      if (result.succeeded()) {
        LOGGER.info("Resource end point ready to listen");
      } else {
        LOGGER.error("Error registering the resource handler. Halting the Resource machinery");
        Runtime.getRuntime().halt(1);
      }
    });
  }

  @Override
  public void stop() throws Exception {
    shutDownApplication();
    super.stop();
  }

  private void startApplication() {
    Initializers initializers = new Initializers();
    try {
      for (Initializer initializer : initializers) {
        initializer.initializeComponent(vertx, config());
      }
    } catch(IllegalStateException ie) {
      LOGGER.error("Error initializing application", ie);
      Runtime.getRuntime().halt(1);
    }
  }

  private void shutDownApplication() {
    Finalizers finalizers = new Finalizers();
    for (Finalizer finalizer : finalizers ) {
      finalizer.finalizeComponent();
    }

  }
}
