package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("user_identity")
@IdName("id")
public class AJEntityUserIdentity extends Model {

    public static final String USERNAME = "username";
    
    public static final String SELECT_USERNAME = "SELECT username FROM user_identity WHERE user_id = ?::uuid";

    public static final List<String> ALL_FIELDS =
        Arrays.asList("id", "user_id", "username", "canonical_username", "reference_id", "email_id", "password",
            "client_id", "login_type", "provision_type", "email_confirm_status", "status", "created_at", "updated_at");

}
