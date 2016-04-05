package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

/**
 * Created by subbu on 06-Jan-2016.
 */
public interface ContentRepo {
  public JsonObject createUpdateResourceEvent();
  public JsonObject copyResourceEvent();
  public JsonObject deletedResourceEvent();
  
  public JsonObject createUpdateQuestionEvent();
  public JsonObject copyQuestionEvent();
  public JsonObject deletedQuestionEvent();
  
}