package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.LessonRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityLesson;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

public class AJLessonRepo implements LessonRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(AJLessonRepo.class);
    private final ProcessorContext context;

    public AJLessonRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public JsonObject createUpdateLessonEvent() {
        String contentId = context.eventBody().getString(EventRequestConstants.ID);
        return getLesson(contentId);
    }

    @Override
    public JsonObject copyLessonEvent() {
        JsonObject response = new JsonObject();
        String targetContentId = context.eventBody().getString(EventRequestConstants.ID);
        JsonObject targetContent = getLesson(targetContentId);
        response.put(EventResponseConstants.TARGET, targetContent);

        String sourceContentId = targetContent.getString(AJEntityLesson.ORIGINAL_LESSON_ID);
        if (sourceContentId != null && !sourceContentId.isEmpty()) {
            JsonObject sourceContent = getLesson(sourceContentId);
            response.put(EventResponseConstants.SOURCE, sourceContent);
        }
        return response;
    }

    @Override
    public JsonObject deleteLessonEvent() {
        String contentId = context.eventBody().getString(EventRequestConstants.ID);
        return getLesson(contentId);
    }

    @Override
    public JsonObject moveLessonEvent() {
        JsonObject response = new JsonObject();
        JsonObject target = context.eventBody().getJsonObject(EventRequestConstants.TARGET);
        if (target == null || target.isEmpty()) {
            LOGGER.error("no target exists in move lesson event");
            return response;
        }
        
        String targetUnitId = target.getString(EventRequestConstants.UNIT_ID);
        JsonObject targetUnit = RepoBuilder.buildUnitRepo(null).getUnit(targetUnitId);
        response.put(EventResponseConstants.TARGET, targetUnit);

        JsonObject source = context.eventBody().getJsonObject(EventRequestConstants.SOURCE);
        if (source == null || source.isEmpty()) {
            LOGGER.error("no source exists in move lesson event");
            return response;
        }
        
        String sourceUnitId = source.getString(EventRequestConstants.UNIT_ID);
        JsonObject sourceUnit = RepoBuilder.buildUnitRepo(null).getUnit(sourceUnitId);
        response.put(EventResponseConstants.SOURCE, sourceUnit);
        return response;
    }

    @Override
    public JsonObject reorderLessonContentEvent() {
        return new JsonObject();
    }

    @Override
    public JsonObject getLesson(String contentId) {
        Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());

        LazyList<AJEntityLesson> lessons = AJEntityLesson.findBySQL(AJEntityLesson.SELECT_LESSON, contentId);
        JsonObject result = null;
        if (!lessons.isEmpty()) {
            LOGGER.info("found lesson for id {} : " + contentId);
            result = new JsonObject(new JsonFormatterBuilder()
                .buildSimpleJsonFormatter(false, AJEntityLesson.ALL_FIELDS).toJson(lessons.get(0)));
        }
        Base.close();
        return result;
    }

}
