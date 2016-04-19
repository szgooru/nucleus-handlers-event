package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities;

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
}