package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by subbu on 06-Jan-2016.
 */
@Table("content")
@IdName("id")
public class AJEntityContent extends Model {

  public static final String ID = "id";
  public static final String TITLE = "title";
  public static final String DESCRIPTION = "description";
  public static final String COLLECTION_ID = "collection_id";
  public static final String ORIGINAL_CONTENT_ID = "original_content_id";
  public static final String CONTENT_FORMAT = "content_format";
  
  public static final String CONTENT_FORMAT_RESOURCE = "resource";

  public static final String SELECT_RESOURCE =
    "SELECT id, title, description, collection_id, original_content_id, content_format FROM content WHERE id = ?::uuid AND content_format ="
    + " 'resource'::content_format_type AND is_deleted = false";

  public static final List<String> RESOURCE_FIELDS = Arrays.asList(ID, TITLE, DESCRIPTION, COLLECTION_ID, ORIGINAL_CONTENT_ID, CONTENT_FORMAT);

  public static final String SELECT_QUESTION =
    "SELECT id, title, description, collection_id, original_content_id, content_format FROM content WHERE id = ?::uuid AND content_format ="
    + " 'question'::content_format_type AND is_deleted = false";

  public static final List<String> QUESTION_FIELDS = Arrays.asList(ID, TITLE, DESCRIPTION, COLLECTION_ID, ORIGINAL_CONTENT_ID, CONTENT_FORMAT);

}
