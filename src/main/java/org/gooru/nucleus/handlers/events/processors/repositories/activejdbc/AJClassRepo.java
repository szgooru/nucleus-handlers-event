package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import java.util.Iterator;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.ClassRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityClass;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class AJClassRepo implements ClassRepo {

    private final ProcessorContext context;
    private static final Logger LOGGER = LoggerFactory.getLogger(AJClassRepo.class);

    public AJClassRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public JsonObject craeteUpdateClassEvent() {
        String classId = context.eventBody().getString(EventRequestConstants.ID);
        return getClassById(classId);
    }

    @Override
    public JsonObject deleteClassEvent() {
        String classId = context.eventBody().getString(EventRequestConstants.ID);
        return getClassById(classId);
    }

    @Override
    public JsonObject updateClassCollaboratorEvent() {
        Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
        String contentId = context.eventBody().getString(EventRequestConstants.ID);
        JsonObject result = context.eventBody();
        LazyList<AJEntityClass> classes = AJEntityClass.findBySQL(AJEntityClass.SELECT_COLLABORATOR, contentId);
        if (!classes.isEmpty()) {
            result.put(EventRequestConstants.COLLABORATORS,
                new JsonArray(classes.get(0).getString(AJEntityClass.COLLABORATOR)));
        }
        Base.close();
        return result;
    }

    @Override
    public JsonObject joinClassEvent() {
        return new JsonObject();
    }

    @Override
    public JsonObject inviteStudentToClassEvent() {
        return context.eventBody();
    }

    @Override
    public JsonObject assignClassToCourseEvent() {
        return new JsonObject();
    }

    @Override
    public JsonObject classContentVisibleEvent() {
        /*
         * Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
         * String classId =
         * context.eventBody().getString(EventRequestConstants.ID);
         * LazyList<AJEntityClass> classes =
         * AJEntityClass.where(AJEntityClass.SELECT_CONTENT_VISIBILITY,
         * classId); if (classes.isEmpty()) { LOGGER.warn(
         * "Not able to find class '{}'", classId); return null; } Base.close();
         * return new
         * JsonObject(classes.get(0).getString(AJEntityClass.CONTENT_VISIBILITY)
         * );
         */
        return new JsonObject();
    }

    public JsonObject getClassById(String classId) {
        Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
        LazyList<AJEntityClass> classes = AJEntityClass.where(AJEntityClass.SELECT_QUERY, classId);
        if (classes.isEmpty()) {
            LOGGER.warn("Not able to find class '{}'", classId);
            return null;
        }
        Base.close();
        return new JsonObject(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityClass.ALL_FIELDS)
            .toJson(classes.get(0)));
    }

    @Override
    public String getClassIdsForCourse(String courseId) {
        Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
        StringBuilder classIds = new StringBuilder();
        LOGGER.debug("getting class ids for course:{}", courseId);
        LazyList<AJEntityClass> classes = AJEntityClass.findBySQL(AJEntityClass.SELECT_CLASSID_FOR_COURSE, courseId);
        if (!classes.isEmpty()) {
            LOGGER.debug("found {} classes matching course id", classes.size());
            Iterator<AJEntityClass> it = classes.iterator();
            for (;;) {
                classIds.append(it.next().getString(AJEntityClass.ID));
                if (!it.hasNext()) {
                    break;
                }
                classIds.append(",");
            }
        }
        Base.close();
        LOGGER.debug("returnin class ids: {}", classIds.toString());
        return classIds.toString();
    }

    @Override
    public JsonObject classRemoveStudentEvent() {
        return context.eventBody();
    }

}
