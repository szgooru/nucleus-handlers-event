package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.processors.repositories.CollectionRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.ContentRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.UserRepo;


/**
 * Created by subbu on 06-Jan-2016.
 */
public final class ActiveJdbcRepoBuilder {

  public static ContentRepo buildContentRepo() {
    return new AJContentRepo();
  }

  public static CollectionRepo buildCollectionRepo() {
    return new AJCollectionRepo();
  }

  public static UserRepo buildUserRepo() {
    return new AJUserRepo();
  }

  private ActiveJdbcRepoBuilder() {
    throw new AssertionError();
  }
}
