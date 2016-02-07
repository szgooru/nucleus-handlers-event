package org.gooru.nucleus.handlers.events.processors.repositories;

import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.ActiveJdbcRepoBuilder;

/**
 * Created by subbu on 06-Jan-2016.
 */
public final class RepoBuilder {

  public static ContentRepo buildContentRepo() {
    return ActiveJdbcRepoBuilder.buildContentRepo();
  }

  public static CollectionRepo buildCollectionRepo() {
    return ActiveJdbcRepoBuilder.buildCollectionRepo();
  }

  public static UserRepo buildUserRepo() {
    return ActiveJdbcRepoBuilder.buildUserRepo();
  }

  private RepoBuilder() {
    throw new AssertionError();
  }
}
