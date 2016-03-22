package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by subbu on 07-Jan-2016.
 */
@Table("user_demographic")
@IdName("id")
public class AJEntityUserDemographic extends Model {

  public static final String ID = "id";
  public static final String FIRST_NAME = "firstname";
  public static final String LAST_NAME = "lastname";
  public static final String EMAIL_ID = "email_id";
  public static final String PARENT_USER_ID = "parent_user_id";
  public static final String USER_CATEGORY = "user_category";
  public static final String SCHOOL_ID = "school_id";
  public static final String SCHOOL_DISTRICT_ID = "school_district_id";
  public static final String COUNTRY_ID = "country_id";
  public static final String STATE_ID = "state_id";

  public final static String SELECT_USER =
    "SELECT id, firstname, lastname, email_id, parent_user_id, user_category, school_id, school_district_id, country_id, state_id FROM"
    + " user_demographic WHERE id = ?::uuid";
  public final static List<String> USER_DEMOGRAPHIC_FIELDS =
          Arrays.asList(ID, FIRST_NAME, LAST_NAME, EMAIL_ID, PARENT_USER_ID, USER_CATEGORY, SCHOOL_ID, SCHOOL_DISTRICT_ID, COUNTRY_ID, STATE_ID);
}