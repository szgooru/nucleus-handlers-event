package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

public interface LessonRepo {

  public JsonObject getLesson();
}
