package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.CourseRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AJCourseRepo implements CourseRepo {

  private static final Logger LOGGER = LoggerFactory.getLogger(AJCourseRepo.class);
  private final ProcessorContext context;

  public AJCourseRepo(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public JsonObject createUpdateCourseEvent() {
    return getCourse();
  }

  @Override
  public JsonObject copyCourseEvent() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JsonObject deleteCourseEvent() {
    return getCourse();
  }

  @Override
  public JsonObject updateCourseCollaboratorEvent() {
    //Nothing to process here so just returning event body as is
    return context.eventBody();
  }

  @Override
  public JsonObject reorderCourseEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject reorderCourseContentEvent() {
    return new JsonObject();
  }

  private JsonObject getCourse() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    String contentId = context.eventBody().getString(EventRequestConstants.ID);
    LazyList<AJEntityCourse> courses = AJEntityCourse.findBySQL(AJEntityCourse.SELECT_COURSE, contentId);
    JsonObject result = null;
    if (!courses.isEmpty()) {
      LOGGER.info("found course for id {} : " + contentId);
      result = new JsonObject(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityCourse.ALL_FIELDS).toJson(courses.get(0)));
    }
    Base.close();
    return result;
  }
}
