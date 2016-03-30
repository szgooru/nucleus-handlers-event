package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.LessonRepo;
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
  public JsonObject createUpdateCopyLessonEvent() {
    return getLesson();
  }

  @Override
  public JsonObject deleteLessonEvent() {
    return getLesson();
  }

  @Override
  public JsonObject moveLessonEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject reorderLessonContentEvent() {
    return new JsonObject();
  }

  private JsonObject getLesson() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LazyList<AJEntityLesson> lessons = AJEntityLesson.findBySQL(AJEntityLesson.SELECT_LESSON, context.id());
    JsonObject result = null;
    if (!lessons.isEmpty()) {
      LOGGER.info("found lesson for id {} : " + context.id());
      result = new JsonObject(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityLesson.ALL_FIELDS).toJson(lessons.get(0)));
    } 
    Base.close();
    return result;
  }
}
