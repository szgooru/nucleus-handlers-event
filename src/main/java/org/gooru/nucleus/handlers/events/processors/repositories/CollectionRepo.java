package org.gooru.nucleus.handlers.events.processors.repositories;

import java.util.List;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by subbu on 12-Jan-2016.
 */
public interface CollectionRepo {
        JsonObject createUpdateCollectionEvent();

    JsonObject copyCollectionEvent();

    JsonObject deleteCollectionEvent();

    JsonObject reorderCollectionContentEvent();

    JsonObject addContentToCollectionEvent();

    JsonObject updateCollectionCollaboratorEvent();

    JsonObject moveCollectionEvent();

    JsonObject createUpdateAssessmentEvent();

    JsonObject copyAssessmentEvent();

    JsonObject deleteAssessmentEvent();

    JsonObject addQuestionToAssessmentEvent();

    JsonObject reorderAssessmentContentEvent();

    JsonObject updateAssessmentCollaboratorEvent();

    List<String> getOwnerAndCreatorIds(JsonArray refCollectionIds);

    JsonObject getCollection(String id);

    JsonObject removeCollection();
}
