package org.gooru.nucleus.handlers.events.processors;

import io.vertx.core.eventbus.Message;

public final class ProcessorBuilder {

  public static Processor build(Message<Object> message) {
    return new MessageProcessor(message);
  }

  private ProcessorBuilder(Message<Object> message) {
    throw new AssertionError();
  }
}
