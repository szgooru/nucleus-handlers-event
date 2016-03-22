package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("course")
public class AJEntityCourse extends Model {

  public static final String ID = "id";
  public static final String TITLE = "title";
  public static final String DESCRIPTION = "description";
  public static final String ORIGINAL_COURSE_ID = "original_course_id";

  public static final String SELECT_COURSE =
    "SELECT id, title, description, original_course_id FROM course WHERE id = ?::uuid AND is_deleted = false";

  public static final List<String> ALL_FIELDS = Arrays.asList(ID, TITLE, DESCRIPTION, ORIGINAL_COURSE_ID);
}
