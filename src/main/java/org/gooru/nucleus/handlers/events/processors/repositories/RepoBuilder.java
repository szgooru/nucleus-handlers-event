package org.gooru.nucleus.handlers.events.processors.repositories;

import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.ActiveJdbcRepoBuilder;

/**
 * Created by subbu on 06-Jan-2016.
 */
public class RepoBuilder {

  public ContentRepo buildContentRepo() {
    return new ActiveJdbcRepoBuilder().buildContentRepo();
  }
  
  public CollectionRepo buildCollectionRepo() {
    return new ActiveJdbcRepoBuilder().buildCollectionRepo();
  }

  public UserRepo buildUserRepo() {
    return new ActiveJdbcRepoBuilder().buildUserRepo();
  }
  

}