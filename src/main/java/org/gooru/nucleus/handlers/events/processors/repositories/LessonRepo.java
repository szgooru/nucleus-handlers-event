package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

public interface LessonRepo {

  public JsonObject createUpdateCopyLessonEvent();
  public JsonObject deleteLessonEvent();
  public JsonObject moveLessonEvent();
  public JsonObject reorderLessonContentEvent();
}
