package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.CourseRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

public class AJCourseRepo implements CourseRepo {

  private static final Logger LOGGER = LoggerFactory.getLogger(AJCourseRepo.class);
  private final ProcessorContext context;
  
  public AJCourseRepo(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public JsonObject getCourse() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LazyList<AJEntityCourse> courses = AJEntityCourse.findBySQL(AJEntityCourse.SELECT_COURSE, context.id());
    JsonObject result = null;
    if (!courses.isEmpty()) {
      LOGGER.info("found course for id {} : " + context.id());
      result = new JsonObject(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityCourse.ALL_FIELDS).toJson(courses.get(0)));
    } 
    Base.close();
    return result;
  }

}
