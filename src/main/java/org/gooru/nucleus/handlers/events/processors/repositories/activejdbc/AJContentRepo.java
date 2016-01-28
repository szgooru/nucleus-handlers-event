package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;


import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.processors.repositories.ContentRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.Content;
import org.javalite.activejdbc.Base;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

import java.sql.SQLException;
import java.util.Set;


/**
 * Created by subbu on 06-Jan-2016.
 */
public class AJContentRepo implements ContentRepo {

  private static final Logger LOGGER = LoggerFactory.getLogger(AJContentRepo.class);

  /**
  * getResource: generates event with the following data items:
  *            id, title, description, url, created_at, updated_at, creator_id, original_creator_id, original_content_id, narration, 
  *            content_format, content_subformat, metadata, taxonomy, depth_of_knowledge, thumbnail, 
  *            course_id, unit_id, lesson_id, collection_id, sequence_id, 
  *            is_copyright_owner, copyright_owner, visible_on_profile, info, display_guide, accessibility
  */
  @Override
  public JsonObject getResource(String contentID) {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("AJContentRepo:getResource: " + contentID);
    
    Content result = Content.findById(getPGObject("id", UUID_TYPE, contentID));
    LOGGER.debug("AJContentRepo:getResource:findById: " + result);
    
    JsonObject returnValue = null;
    //List<String> attributes = Content.attributes();
    String[] attributes =  {"id", "title", "description", "url", 
                            "created_at", "updated_at", "creator_id", "original_creator_id", "original_content_id", 
                            "narration", "content_format", "content_subformat", 
                            "metadata", "taxonomy", "depth_of_knowledge", "thumbnail",
                            "course_id", "unit_id", "lesson_id", "collection_id", "sequence_id",
                            "is_copyright_owner", "copyright_owner", "visible_on_profile",
                            "info", "display_guide", "accessibility" };
    LOGGER.debug("AJContentRepo:getResource:findById attributes: " + String.join(", ", attributes) );

    if (result != null) {      
      //returnValue =  new JsonObject(result.toJson(false,  attributes.toArray(new String[0]) ));
      returnValue =  new JsonObject(result.toJson(false,  attributes ));
    }
    LOGGER.debug("AJContentRepo:getResource:findById returned: " + returnValue);
    
    Base.close();
    return returnValue;
  }

  /**
  * getDeletedResource: generates event with the following data items:
  *            id, title, description, url, created_at, updated_at, creator_id, original_creator_id, original_content_id,
  *            content_format, content_subformat,  
  *            course_id, unit_id, lesson_id, collection_id, sequence_id, 
  *            is_copyright_owner, copyright_owner
  */
  @Override
  public JsonObject getDeletedResource(String contentID) {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("AJContentRepo:getDeletedResource: " + contentID);
    
    Content result = Content.findById(getPGObject("id", UUID_TYPE, contentID));
    LOGGER.debug("AJContentRepo:getDeletedResource:findById: " + result);
    
    JsonObject returnValue = null;
    String[] attributes =  {"id", "title", "description", "url", 
                            "created_at", "updated_at", "creator_id", "original_creator_id", "original_content_id",
                            "content_format", "content_subformat", 
                            "course_id", "unit_id", "lesson_id", "collection_id", "sequence_id",
                            "is_copyright_owner", "copyright_owner" };
    LOGGER.debug("AJContentRepo:getDeletedResource:findById attributes: " + String.join(", ", attributes)  );

    if (result != null) {      
      returnValue =  new JsonObject(result.toJson(false,  attributes ));
    }
    LOGGER.debug("AJContentRepo:getDeletedResource:findById returned: " + returnValue);
    
    Base.close();
    return returnValue;
  }

  /**
  * getQuestion: generates event with ALL data from DB table
  */
  @Override
  public JsonObject getQuestion(String contentID) {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("AJContentRepo:getQuestion: " + contentID);
    
    Content result = Content.findById(getPGObject("id", UUID_TYPE, contentID));
    LOGGER.debug("AJContentRepo:getResource:findById: " + result);
    
    JsonObject returnValue = null;
    Set<String> attributes = Content.attributeNames();   
    LOGGER.debug("AJContentRepo:getQuestion:findById attributes: " + String.join(", ", attributes.toArray(new String[0])) );

    if (result != null) {      
      returnValue =  new JsonObject(result.toJson(false,  attributes.toArray(new String[0]) ));
    }
    LOGGER.debug("AJContentRepo:getQuestion:findById returned: " + returnValue);
    
    Base.close();
    return returnValue;
  }
    
  /**
  * getDeletedQuestion: generates event with the following data items:
  *            id, title, description, url, created_at, updated_at, creator_id, original_creator_id, original_content_id, short_title,
  *            content_format, content_subformat,  
  *            course_id, unit_id, lesson_id, collection_id, sequence_id 
  */
  @Override
  public JsonObject getDeletedQuestion(String contentID) {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("AJContentRepo:getDeletedQuestion: " + contentID);
    
    Content result = Content.findById(getPGObject("id", UUID_TYPE, contentID));
    LOGGER.debug("AJContentRepo:getDeletedResource:findById: " + result);
    
    JsonObject returnValue = null;
    String[] attributes =  {"id", "title", "description", "url", "short_title",
                            "created_at", "updated_at", "creator_id", "original_creator_id", "original_content_id",
                            "content_format", "content_subformat", 
                            "course_id", "unit_id", "lesson_id", "collection_id", "sequence_id" };
    LOGGER.debug("AJContentRepo:getDeletedQuestion:findById attributes: " + String.join(", ", attributes) );

    if (result != null) {      
      returnValue =  new JsonObject(result.toJson(false,  attributes ));
    }
    LOGGER.debug("AJContentRepo:getDeletedQuestion:findById returned: " + returnValue);
    
    Base.close();
    return returnValue;
  }

  private final String UUID_TYPE = "uuid"; 
  
  private PGobject getPGObject(String field, String type, String value) {
    PGobject pgObject = new PGobject();
    pgObject.setType(type);
    try {
      pgObject.setValue(value);
      return pgObject;
    } catch (SQLException e) {
      LOGGER.error("Not able to set value for field: {}, type: {}, value: {}", field, type, value);
      return null;
    }
  }
  
}
