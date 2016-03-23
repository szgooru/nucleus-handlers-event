package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

public interface ClassRepo {

  public JsonObject craeteUpdateClassEvent();
  public JsonObject deleteClassEvent();
  public JsonObject updateClassCollaboratorEvent();
  public JsonObject joinClassEvent();
  public JsonObject inviteStudentToClassEvent();
  public JsonObject assignClassToCourseEvent();
  public JsonObject classContentVisibleEvent();
}
