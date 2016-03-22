package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

/**
 * Created by subbu on 12-Jan-2016.
 *
 */
public interface CollectionRepo {
  JsonObject getCollection();
  JsonObject getDeletedCollection();
  
  JsonObject getAssessment();
  JsonObject getDeletedAssessment();

}
