package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import io.vertx.core.json.JsonObject;
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

public class AJClassRepo implements ClassRepo {

    private final ProcessorContext context;
    private static final Logger LOGGER = LoggerFactory.getLogger(AJClassRepo.class);

    public AJClassRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public JsonObject craeteUpdateClassEvent() {
        return getClassById();
    }

    @Override
    public JsonObject deleteClassEvent() {
        return getClassById();
    }

    @Override
    public JsonObject updateClassCollaboratorEvent() {
        // Nothing to process here so just returning event body as is
        return context.eventBody();
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
        // TODO: get visibility of class and return
        return new JsonObject();
    }

    public JsonObject getClassById() {
        Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
        String classId = context.eventBody().getString(EventRequestConstants.ID);
        LazyList<AJEntityClass> classes = AJEntityClass.where(AJEntityClass.SELECT_QUERY, classId);
        if (classes.isEmpty()) {
            LOGGER.warn("Not able to find class '{}'", classId);
            return null;
        }
        Base.close();
        return new JsonObject(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityClass.ALL_FIELDS)
            .toJson(classes.get(0)));
    }

}
