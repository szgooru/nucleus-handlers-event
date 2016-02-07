package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.processors.repositories.UserRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.User;
import org.javalite.activejdbc.Base;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import io.vertx.core.json.JsonObject;


/**
 * Created by subbu on 07-Jan-2016.
 */
public class AJUserRepo implements UserRepo {

  private static final Logger LOGGER = LoggerFactory.getLogger(AJUserRepo.class);
  
  /**
   * getUser: generates event with the following data items:
   *            id, firstname, lastname, parent_user_id, user_category, 
   *            created_at, updated_at, last_login, birth_date, grade, 
   *            thumbnail_path, gender, email_id, school_id, school_name, 
   *            school_district_id, school_district_name, country_id, country_name, state_id, state_name 
   *            
   *      Consumer needs to check for null / existence of values
   */
  @Override
  public JsonObject getUser(String userID) {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("AJUserRepo : getUser: " + userID);
    
    User result = User.findById(getPGObject("id", UUID_TYPE, userID));
    LOGGER.debug("AJUserRepo : getUser : findById : " + result);
    
    JsonObject returnValue = null;
    String[] attributes =  {"id", "firstname", "lastname", "email_id", "parent_user_id", "user_category",
                            "created_at", "updated_at", "last_login", "birth_date", "grade",
                            "thumbnail_path", "gender", "school_id", "school_district_id", "country_id", "state_id" };
    
    LOGGER.debug("AJUserRepo : getUser : findById attributes: " + String.join(", ", attributes) );

    if (result != null) {      
      returnValue =  new JsonObject(result.toJson(false,  attributes ));
      //
      // get following detail and set them in response object
      // "school_name",  "school_district_name", "country_name", "state_name" 
      //
      addSchoolDistrictStateCountryInfo(returnValue);
    }
    LOGGER.debug("AJUserRepo : getUser : findById returned: " + returnValue);
    
    Base.close();
    return returnValue;
  }

  @Override
  public JsonObject getDeletedUser(String userID) {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("AJUserRepo : getDeletedUser: " + userID);
    
    User result = User.findById(getPGObject("id", UUID_TYPE, userID));
    LOGGER.debug("AJUserRepo : getDeletedUser : findById : " + result);
    
    JsonObject returnValue = null;
    String[] attributes =  {"id", "firstname", "lastname", "email_id", "parent_user_id", "user_category",
                            "created_at", "updated_at", "last_login", "birth_date", "grade",
                            "thumbnail_path", "gender", "school_id", "school_district_id", "country_id", "state_id" };
    LOGGER.debug("AJUserRepo : getDeletedUser : findById attributes: " + String.join(", ", attributes) );

    if (result != null) {      
      returnValue =  new JsonObject(result.toJson(false,  attributes ));
      //
      // get following detail and set them in response object
      // "school_name",  "school_district_name", "country_name", "state_name" 
      //
      addSchoolDistrictStateCountryInfo(returnValue);
    }
    LOGGER.debug("AJUserRepo : getDeletedUser : findById returned: " + returnValue);
    
    Base.close();
    return returnValue;
  }
  
  private void addSchoolDistrictStateCountryInfo(JsonObject jsonToModify) {
    String schoolId = jsonToModify.getString("school_id");
    if ((schoolId != null) && !schoolId.isEmpty() ) {
      List<Map> schools = Base.findAll("select name from school where id = ?::uuid", schoolId);
      for (Map school : schools)
        jsonToModify.put("school_name", school.get("name"));
    }
    
    String schoolDistrictId = jsonToModify.getString("school_district_id");
    if ((schoolDistrictId != null) && !schoolDistrictId.isEmpty() ) {
      List<Map> schoolDists = Base.findAll("select name from school_district where id = ?::uuid", schoolDistrictId);
      for (Map district : schoolDists)
        jsonToModify.put("school_district_name", district.get("name"));
    }
    
    String schoolStateId = jsonToModify.getString("school_district_id");
    if ((schoolStateId != null) && !schoolStateId.isEmpty() ) {
      List<Map> states = Base.findAll("select name from state where state_id = ?::uuid", schoolStateId);
      for (Map state : states)
        jsonToModify.put("state_name", state.get("name"));
    }
    
    String countryId = jsonToModify.getString("country_id");
    if ((countryId != null) && !countryId.isEmpty() ) {
      List<Map> countries = Base.findAll("select name from country where id = ?::uuid", countryId);
      for (Map country : countries)
        jsonToModify.put("country_name", country.get("name"));
    }
    
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
