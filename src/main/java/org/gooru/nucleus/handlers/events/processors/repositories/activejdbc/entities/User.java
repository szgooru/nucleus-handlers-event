package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by subbu on 07-Jan-2016.
 */
@Table("user")
@IdName("user_id")
public class User extends Model {
  
}
