package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

/**
 * Created by subbu on 06-Jan-2016.
 */
public interface ContentRepo {
  JsonObject getResource(String contentID);
  JsonObject getDeletedResource(String contentID);
  
  JsonObject getQuestion(String contentID);
  JsonObject getDeletedQuestion(String contentID);
  
}