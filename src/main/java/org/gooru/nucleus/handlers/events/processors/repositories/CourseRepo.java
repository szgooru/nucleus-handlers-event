package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

public interface CourseRepo {

    JsonObject createUpdateCourseEvent();

    JsonObject copyCourseEvent();

    JsonObject deleteCourseEvent();

    JsonObject updateCourseCollaboratorEvent();

    JsonObject reorderCourseEvent();

    JsonObject reorderCourseContentEvent();
    
    JsonObject getCourse(String courseId);
}
