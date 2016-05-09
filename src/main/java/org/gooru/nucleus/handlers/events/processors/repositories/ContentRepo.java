package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

/**
 * Created by subbu on 06-Jan-2016.
 */
public interface ContentRepo {
        JsonObject createUpdateResourceEvent();

    JsonObject copyResourceEvent();

    JsonObject deletedResourceEvent();

    JsonObject createUpdateQuestionEvent();

    JsonObject copyQuestionEvent();

    JsonObject deletedQuestionEvent();
    
    JsonObject getResource(String resourceId);
    
    JsonObject getQuestion(String questionId);
    
    String getContentFormatById(String contentId);

}
