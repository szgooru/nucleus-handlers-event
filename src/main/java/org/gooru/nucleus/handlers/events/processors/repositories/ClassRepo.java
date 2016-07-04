package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

public interface ClassRepo {

    JsonObject craeteUpdateClassEvent();

    JsonObject deleteClassEvent();

    JsonObject updateClassCollaboratorEvent();

    JsonObject joinClassEvent();

    JsonObject inviteStudentToClassEvent();

    JsonObject assignClassToCourseEvent();

    JsonObject classContentVisibleEvent();

    JsonObject getClassById(String classId);

    String getClassIdsForCourse(String courseId);

    JsonObject classRemoveStudentEvent();
}
