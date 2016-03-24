package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.CollectionRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityCollection;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

/**
 * Created by Subbu on 12-Jan-2016.
 */
public class AJCollectionRepo implements CollectionRepo {

  private static final Logger LOGGER = LoggerFactory.getLogger(AJCollectionRepo.class);
  private final ProcessorContext context;

  public AJCollectionRepo(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public JsonObject createUpdateCopyCollectionEvent() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("getting collection for id {}", context.id());

    JsonObject result = null;
    LazyList<AJEntityCollection> collections = AJEntityCollection.findBySQL(AJEntityCollection.SELECT_COLLECTION, context.id());
    if (!collections.isEmpty()) {
      result = new JsonObject(
              new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityCollection.COLLECTION_FIELDS).toJson(collections.get(0)));
    }

    Base.close();
    return result;
  }

  @Override
  public JsonObject deleteCollectionEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject reorderCollectionContentEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject addContentToCollectionEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject updateCollectionCollaboratorEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject moveCollectionEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject createUpdateCopyAssessmentEvent() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LOGGER.debug("getting assessment for id {}", context.id());

    JsonObject result = null;
    LazyList<AJEntityCollection> assessments = AJEntityCollection.findBySQL(AJEntityCollection.SELECT_ASSESSMENT, context.id());
    if (!assessments.isEmpty()) {
      result = new JsonObject(
              new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityCollection.ASSESSMENT_FIELDS).toJson(assessments.get(0)));
    }

    Base.close();
    return result;
  }

  @Override
  public JsonObject deleteAssessmentEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject addQuestionToAssessmentEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject reorderAssessmentContentEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject updateAssessmentCollaboratorEvent() {
    return new JsonObject();
  }
}
