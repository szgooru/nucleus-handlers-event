package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.UserDemographicRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityUserDemographic;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;


/**
 * Created by subbu on 07-Jan-2016.
 */
public class AJUserRepo implements UserDemographicRepo {

  private static final Logger LOGGER = LoggerFactory.getLogger(AJUserRepo.class);
  private final ProcessorContext context;

  public AJUserRepo(ProcessorContext context) {
    this.context = context;
  }

  /**
   * getUser: generates event with the following data items: id, firstname,
   * lastname, parent_user_id, user_category, created_at, updated_at,
   * last_login, birth_date, grade, thumbnail_path, gender, email_id, school_id,
   * school_name, school_district_id, school_district_name, country_id,
   * country_name, state_id, state_name
   *
   * Consumer needs to check for null / existence of values
   */
  @Override
  public JsonObject getUser() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("AJUserRepo : getUser: " + context.id());

    LazyList<AJEntityUserDemographic> users = AJEntityUserDemographic.findBySQL(AJEntityUserDemographic.SELECT_USER, context.id());
    JsonObject result = null;
    if (!users.isEmpty()) {
      result = new JsonObject(
              new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityUserDemographic.USER_DEMOGRAPHIC_FIELDS).toJson(users.get(0)));
    }
    Base.close();
    return result;
  }

  /*private void addSchoolDistrictStateCountryInfo(JsonObject jsonToModify) {
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
  }*/
}
