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

    public static final String EMAIL_ID = "email_id";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";

    public final static String SELECT_MULTIPLE_EMAILIDS =
        "SELECT email_id FROM user_demographic WHERE id = ANY (?::uuid[])";
    
    public final static String SELECT_FIRST_LAST_NAME =
        "SELECT firstname, lastname FROM user_demographic WHERE id = ?::uuid";
    
    public static final List<String> ALL_FIELDS = Arrays.asList("id", "firstname", "lastname", "parent_user_id",
        "user_category", "created_at", "updated_at", "birth_date", "grade", "course", "thumbnail_path", "gender",
        "about_me", "school_id", "school", "school_district_id", "school_district", "email_id", "country_id", "country",
        "state_id", "state", "metadata", "roster_id", "roster_global_userid");

}