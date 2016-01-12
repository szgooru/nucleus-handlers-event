package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.processors.repositories.CollectionRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.ContentRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.UserRepo;


/**
 * Created by subbu on 06-Jan-2016.
 */
public class ActiveJdbcRepoBuilder {
  
  public ContentRepo buildContentRepo() {
    return new AJContentRepo();
  }
  
  public CollectionRepo buildCollectionRepo() {
    return new AJCollectionRepo();
  }

  public UserRepo buildUserRepo() {
    return new AJUserRepo();
  }
}
