package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

/**
 * Created by subbu on 12-Jan-2016.
 *
 */
public interface CollectionRepo {
  JsonObject getCollection(String contentID);
  JsonObject getDeletedCollection(String contentID);
  
  JsonObject getAssessment(String contentID);
  JsonObject getDeletedAssessment(String contentID);

}
