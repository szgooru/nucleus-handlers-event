package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.ClassRepo;

import io.vertx.core.json.JsonObject;

public class AJClassRepo implements ClassRepo {

  private final ProcessorContext context;
  
  public AJClassRepo(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public JsonObject craeteUpdateClassEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject deleteClassEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject updateClassCollaboratorEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject joinClassEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject inviteStudentToClassEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject assignClassToCourseEvent() {
    return new JsonObject();
  }

}
