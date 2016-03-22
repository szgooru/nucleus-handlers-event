package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.CollectionRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.ContentRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.CourseRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.LessonRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.UnitRepo;


/**
 * Created by subbu on 06-Jan-2016.
 */
public final class AJRepoBuilder {

  public static ContentRepo buildContentRepo(ProcessorContext context) {
    return new AJContentRepo(context);
  }

  public static CollectionRepo buildCollectionRepo(ProcessorContext context) {
    return new AJCollectionRepo(context);
  }

  public static CourseRepo buildCourseRepo(ProcessorContext context) {
    return new AJCourseRepo(context);
  }

  public static UnitRepo buildUnitRepo(ProcessorContext context) {
    return new AJUnitRepo(context);
  }

  public static LessonRepo buildLessonRepo(ProcessorContext context) {
    return new AJLessonRepo(context);
  }
  
  public static AJUserRepo buildUserRepo(ProcessorContext context) {
    return new AJUserRepo(context);
  }
  
  private AJRepoBuilder() {
    throw new AssertionError();
  }
}
