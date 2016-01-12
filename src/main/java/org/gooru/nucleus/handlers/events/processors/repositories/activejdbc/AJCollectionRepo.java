package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import java.util.List;
import java.util.Map;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.processors.repositories.CollectionRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.Collection;
import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;


/**
 * Created by Subbu on 12-Jan-2016.
 */
public class AJCollectionRepo implements CollectionRepo {

  private static final Logger LOGGER = LoggerFactory.getLogger(AJCollectionRepo.class);

  /**
  * @see org.gooru.nucleus.handlers.events.processors.repositories.CollectionRepo#getCollection(java.lang.String)
  * getCollection: generates event with the following data items:
  *            id, title, created_at, updated_at, creator_id, original_creator_id, original_collection_id, 
  *            publish_date, format, learning_objective, collaborator, orientation, grading, setting, 
  *            metadata, taxonomy, thumbnail, visible_on_profile, course_id, unit_id, lesson_id 
  *            
  *            course_id, unit_id, lesson_id   ------ will come from the association table
  */
  @Override
  public JsonObject getCollection(String contentID) {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("AJCollectionRepo : getCollection : " + contentID);
    
    Collection result = Collection.findById(contentID);
    LOGGER.debug("AJCollectionRepo : getCollection : " + result);
    
    JsonObject returnValue = null;
    String[] attributes =  {"id", "title", "created_at", "updated_at", "creator_id", "original_creator_id", "original_collection_id", 
                            "publish_date", "format", "learning_objective", "collaborator", "orientation", "grading", "setting", 
                            "metadata", "taxonomy", "thumbnail", "visible_on_profile" };
    LOGGER.debug("AJCollectionRepo : getCollection : findById attributes: " + String.join(", ", attributes) );

    if (result != null) {      
      returnValue =  new JsonObject(result.toJson(false,  attributes ));
      addContainmentInfo(returnValue);
    }
    LOGGER.debug("AJCollectionRepo : getCollection : findById returned: " + returnValue);

    Base.close();
    return returnValue;
  }

  /* (non-Javadoc)
   * @see org.gooru.nucleus.handlers.events.processors.repositories.CollectionRepo#getDeletedCollection(java.lang.String)
   */
  @Override
  public JsonObject getDeletedCollection(String contentID) {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("AJCollectionRepo : getDeletedCollection : " + contentID);
    // TODO: ...
    Base.close();
    return null;
  }

  /**
  * @see org.gooru.nucleus.handlers.events.processors.repositories.CollectionRepo#getCollection(java.lang.String)
  * getAssessment: generates event with the following data items:
  *            id, title, created_at, updated_at, creator_id, original_creator_id, original_collection_id, 
  *            publish_date, format, learning_objective, collaborator, orientation, grading, setting, 
  *            metadata, taxonomy, thumbnail, visible_on_profile, course_id, unit_id, lesson_id 
  *            
  *            course_id, unit_id, lesson_id   ------ will come from the association table
  */
  @Override
  public JsonObject getAssessment(String contentID) {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("AJCollectionRepo : getAssessment : " + contentID);
    
    Collection result = Collection.findById(contentID);
    LOGGER.debug("AJCollectionRepo : getAssessment : " + result);
    
    JsonObject returnValue = null;
    String[] attributes =  {"id", "title", "created_at", "updated_at", "creator_id", "original_creator_id", "original_collection_id", 
                            "publish_date", "format", "learning_objective", "collaborator", "orientation", "grading", "setting", 
                            "metadata", "taxonomy", "thumbnail", "visible_on_profile" };
    LOGGER.debug("AJCollectionRepo : getAssessment : findById attributes: " + String.join(", ", attributes) );

    if (result != null) {      
      returnValue =  new JsonObject(result.toJson(false,  attributes ));
      addContainmentInfo(returnValue);
    }
    LOGGER.debug("AJCollectionRepo : getAssessment : findById returned: " + returnValue);

    Base.close();
    return returnValue;
  }

  /* (non-Javadoc)
   * @see org.gooru.nucleus.handlers.events.processors.repositories.CollectionRepo#getDeletedAssessment(java.lang.String)
   */
  @Override
  public JsonObject getDeletedAssessment(String contentID) {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("AJCollectionRepo : getDeletedAssessment : " + contentID);
    // TODO: ...
    Base.close();
    return null;
  }
  
  /*
   * course : {[
   *            unit : {[
   *                    lesson : { }
   *                    lesson : { } ]}
   *            unit : {[
   *                    lesson : { } ]}] }
   *                    
   * course : {[
   *            unit : {[
   *                    lesson : { }
   *                    lesson : { } ]}
   *            unit : {[
   *                    lesson : { } ]}] }
   *                            
   */
  private void addContainmentInfo(JsonObject jsonToModify) {
    String collectionId = jsonToModify.getString("id");
    if ((collectionId != null) && !collectionId.isEmpty() ) {
      List<Map> collectionAssocs = Base.findAll("select * from course_unit_lesson_collection where collection_id = ? order by course_id, unit_id, lesson_id", collectionId);
      if (collectionAssocs.size() > 0) {
        JsonArray courseIds = new JsonArray();
        JsonArray unitIds = new JsonArray();
        JsonArray lessonIds = new JsonArray();
        
        for (Map entry : collectionAssocs) {
          LOGGER.debug("AJCollectionRepo : addContainmentInfo : " + entry.toString());
          
          //
          // <TBD> -- need to create this jsonArray properly...
          //          right now we are sending a flat list of courseids, unitids and lessonids
          //          instead of course{ unit { lesson {} } } hierarchy data
          //
          courseIds.add(entry.get("course_id"));
          unitIds.add(entry.get("unit_id"));
          lessonIds.add(entry.get("lesson_id"));
                      
        }
        
        jsonToModify.put("courses", courseIds);
        jsonToModify.put("units", unitIds);
        jsonToModify.put("lessons", lessonIds);
        
      }
    }
  }
  
}
