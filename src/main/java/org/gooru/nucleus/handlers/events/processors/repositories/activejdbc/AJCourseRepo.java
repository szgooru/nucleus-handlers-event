package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.CourseRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class AJCourseRepo implements CourseRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(AJCourseRepo.class);
    private final ProcessorContext context;

    public AJCourseRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public JsonObject createUpdateCourseEvent() {
        String courseId = context.eventBody().getString(EventRequestConstants.ID);
        return getCourse(courseId);
    }

    @Override
    public JsonObject copyCourseEvent() {
        JsonObject response = new JsonObject();

        String targetCourseId = context.eventBody().getString(EventRequestConstants.ID);
        JsonObject targetCourse = getCourse(targetCourseId);
        response.put(EventResponseConstants.TARGET, targetCourse);

        String sourceCourseId = targetCourse.getString(AJEntityCourse.ORIGINAL_COURSE_ID);
        if (sourceCourseId != null && !sourceCourseId.isEmpty()) {
            JsonObject sourceCourse = getCourse(sourceCourseId);
            response.put(EventResponseConstants.SOURCE, sourceCourse);
        }

        return response;
    }

    @Override
    public JsonObject deleteCourseEvent() {
        String courseId = context.eventBody().getString(EventRequestConstants.ID);
        return getCourse(courseId);
    }

    @Override
    public JsonObject updateCourseCollaboratorEvent() {
        Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
        String contentId = context.eventBody().getString(EventRequestConstants.ID);
        JsonObject result = context.eventBody();
        LazyList<AJEntityCourse> courses =
            AJEntityCourse.findBySQL(AJEntityCourse.SELECT_COLLABORATOR, contentId);
        if (!courses.isEmpty()) {
            result.put(EventRequestConstants.COLLABORATORS, new JsonArray(courses.get(0).getString(AJEntityCourse.COLLABORATOR)));
        }
        Base.close();
        return result;
    }

    @Override
    public JsonObject reorderCourseEvent() {
        return new JsonObject();
    }

    @Override
    public JsonObject reorderCourseContentEvent() {
        return new JsonObject();
    }

    @Override
    public JsonObject getCourse(String courseId) {
        Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
        LazyList<AJEntityCourse> courses = AJEntityCourse.findBySQL(AJEntityCourse.SELECT_COURSE, courseId);
        JsonObject result = null;
        if (!courses.isEmpty()) {
            LOGGER.info("found course for id {} : " + courseId);
            result = new JsonObject(new JsonFormatterBuilder()
                .buildSimpleJsonFormatter(false, AJEntityCourse.ALL_FIELDS).toJson(courses.get(0)));
        }
        Base.close();
        return result;
    }
}
