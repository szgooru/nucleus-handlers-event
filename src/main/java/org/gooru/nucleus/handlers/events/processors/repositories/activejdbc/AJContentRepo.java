package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.ContentRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityContent;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by subbu on 06-Jan-2016.
 */
public class AJContentRepo implements ContentRepo {

  private static final Logger LOGGER = LoggerFactory.getLogger(AJContentRepo.class);
  private final ProcessorContext context;

  public AJContentRepo(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public JsonObject createUpdateResourceEvent() {
    return getResource();
  }

  @Override
  public JsonObject copyResourceEvent() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JsonObject deletedResourceEvent() {
    return getResource();
  }

  @Override
  public JsonObject createUpdateQuestionEvent() {
    return getQuestion();
  }

  @Override
  public JsonObject copyQuestionEvent() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JsonObject deletedQuestionEvent() {
    return getQuestion();
  }

  private JsonObject getResource() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    String contentId = context.eventBody().getString(EventRequestConstants.ID);
    LOGGER.debug("getting resource for id {}", contentId);

    JsonObject result = null;
    LazyList<AJEntityContent> resources = AJEntityContent.findBySQL(AJEntityContent.SELECT_RESOURCE, contentId);
    if (!resources.isEmpty()) {
      result = new JsonObject(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityContent.RESOURCE_FIELDS).toJson(resources.get(0)));
    }

    Base.close();
    return result;
  }

  private JsonObject getQuestion() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    String contentId = context.eventBody().getString(EventRequestConstants.ID);
    LOGGER.debug("getting question for id {}", contentId);

    JsonObject result = null;
    LazyList<AJEntityContent> questions = AJEntityContent.findBySQL(AJEntityContent.SELECT_QUESTION, contentId);
    if (!questions.isEmpty()) {
      result = new JsonObject(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityContent.QUESTION_FIELDS).toJson(questions.get(0)));
    }

    Base.close();
    return result;
  }
}
