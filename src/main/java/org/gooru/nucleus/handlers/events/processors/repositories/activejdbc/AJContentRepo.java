package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.ContentRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityContent;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

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
  public JsonObject getResource() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("getting resource for id {}", context.id());

    JsonObject result = null;
    LazyList<AJEntityContent> resources = AJEntityContent.findBySQL(AJEntityContent.SELECT_RESOURCE, context.id());
    if (!resources.isEmpty()) {
      result = new JsonObject(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityContent.RESOURCE_FIELDS).toJson(resources.get(0)));
    }

    Base.close();
    return result;
  }

  @Override
  public JsonObject getDeletedResource() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("getting deleted resource for id {}", context.id());

    // TODO: ...
    Base.close();
    return null;
  }

  @Override
  public JsonObject getQuestion() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("getting question for id {}", context.id());

    JsonObject result = null;
    LazyList<AJEntityContent> questions = AJEntityContent.findBySQL(AJEntityContent.SELECT_QUESTION, context.id());
    if (!questions.isEmpty()) {
      result = new JsonObject(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityContent.QUESTION_FIELDS).toJson(questions.get(0)));
    }

    Base.close();
    return result;
  }

  @Override
  public JsonObject getDeletedQuestion() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());

    // TODO:
    Base.close();
    return null;
  }
}
