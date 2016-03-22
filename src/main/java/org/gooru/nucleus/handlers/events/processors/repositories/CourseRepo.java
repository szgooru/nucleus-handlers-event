package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

public interface CourseRepo {

  public JsonObject createUpdateCopyCourseEvent();
  public JsonObject deleteCourseEvent();
  public JsonObject updateCourseCollaboratorEvent();
  public JsonObject reorderCourseEvent();
  public JsonObject reorderCourseContentEvent();
}
