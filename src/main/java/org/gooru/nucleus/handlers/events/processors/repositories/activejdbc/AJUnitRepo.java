package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.events.processors.repositories.UnitRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityUnit;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

public class AJUnitRepo implements UnitRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(AJUnitRepo.class);
    private final ProcessorContext context;

    public AJUnitRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public JsonObject createUpdateUnitEvent() {
        String contentId = context.eventBody().getString(EventRequestConstants.ID);
        return getUnit(contentId);
    }

    @Override
    public JsonObject copyUnitEvent() {
        JsonObject response = new JsonObject();
        String targetContentId = context.eventBody().getString(EventRequestConstants.ID);
        JsonObject targetContent = getUnit(targetContentId);
        response.put(EventResponseConstants.TARGET, targetContent);

        String sourceContentId = targetContent.getString(AJEntityUnit.ORIGINAL_UNIT_ID);
        if (sourceContentId != null && !sourceContentId.isEmpty()) {
            JsonObject sourceContent = getUnit(sourceContentId);
            response.put(EventResponseConstants.SOURCE, sourceContent);
        }
        return response;
    }

    @Override
    public JsonObject deleteUnitEvent() {
        String contentId = context.eventBody().getString(EventRequestConstants.ID);
        return getUnit(contentId);
    }

    @Override
    public JsonObject moveUnitEvent() {
        JsonObject response = new JsonObject();
        JsonObject target = context.eventBody().getJsonObject(EventRequestConstants.TARGET);
        if (target == null || target.isEmpty()) {
            LOGGER.error("no target exists in move unit event");
            return response;
        }
        
        String targetCourseId = target.getString(EventRequestConstants.COURSE_ID);
        JsonObject targetCourse = RepoBuilder.buildCourseRepo(null).getCourse(targetCourseId);
        response.put(EventResponseConstants.TARGET, targetCourse);
        
        JsonObject source = context.eventBody().getJsonObject(EventRequestConstants.SOURCE);
        if (source == null || source.isEmpty()) {
            LOGGER.error("no source exists in move unit event");
            return response;
        }
        
        String sourceCourseId = source.getString(EventRequestConstants.COURSE_ID);
        JsonObject sourceCourse = RepoBuilder.buildCourseRepo(null).getCourse(sourceCourseId);
        response.put(EventResponseConstants.SOURCE, sourceCourse);
        return response;
    }

    @Override
    public JsonObject reorderUnitContentEvent() {
        return new JsonObject();
    }

    @Override
    public JsonObject getUnit(String contentId) {
        Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());

        LazyList<AJEntityUnit> units = AJEntityUnit.findBySQL(AJEntityUnit.SELECT_UNIT, contentId);
        JsonObject result = null;
        if (!units.isEmpty()) {
            LOGGER.info("found unit for id {} : " + contentId);
            result = new JsonObject(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityUnit.ALL_FIELDS)
                .toJson(units.get(0)));
        }
        Base.close();
        return result;
    }
}
