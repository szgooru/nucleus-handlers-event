package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

/**
 * Created by subbu on 12-Jan-2016.
 *
 */
public interface CollectionRepo {
  public JsonObject createUpdateCollectionEvent();
  public JsonObject copyCollectionEvent();
  public JsonObject deleteCollectionEvent();
  public JsonObject reorderCollectionContentEvent();
  public JsonObject addContentToCollectionEvent();
  public JsonObject updateCollectionCollaboratorEvent();
  public JsonObject moveCollectionEvent();
  
  public JsonObject createUpdateAssessmentEvent();
  public JsonObject copyAssessmentEvent();
  public JsonObject deleteAssessmentEvent();
  public JsonObject addQuestionToAssessmentEvent();
  public JsonObject reorderAssessmentContentEvent();
  public JsonObject updateAssessmentCollaboratorEvent();
}
