package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by subbu on 12-Jan-2016.
 */
@Table("collection")
@IdName("id")
public class AJEntityCollection extends Model {

  public static final String ID = "id";
  public static final String TITLE = "title";
  public static final String LESSON_ID = "lesson_id";
  public static final String ORIGINAL_COLLECTION_ID = "original_collection_id";
  
  public static final String SELECT_COLLECTION =
    "SELECT id, title, lesson_id, original_collection_id FROM collection WHERE id = ?::uuid AND format = 'collection'::content_container_type"
    + " AND is_deleted = false";

  public static final List<String> COLLECTION_FIELDS = Arrays.asList(ID, TITLE, LESSON_ID, ORIGINAL_COLLECTION_ID);

  public static final String SELECT_ASSESSMENT =
    "SELECT id, title, lesson_id, original_collection_id FROM collection WHERE id = ?::uuid AND format = 'assessment'::content_container_type"
    + " AND is_deleted = false";

  public static final List<String> ASSESSMENT_FIELDS = Arrays.asList(ID, TITLE, LESSON_ID, ORIGINAL_COLLECTION_ID);
}
