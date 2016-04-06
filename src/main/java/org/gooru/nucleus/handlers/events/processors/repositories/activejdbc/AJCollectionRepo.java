package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.CollectionRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityCollection;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  public JsonObject createUpdateCollectionEvent() {
    return getCollection();
  }

  @Override
  public JsonObject copyCollectionEvent() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JsonObject deleteCollectionEvent() {
    return getCollection();
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
    return context.eventBody();
  }

  @Override
  public JsonObject moveCollectionEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject createUpdateAssessmentEvent() {
    return getAssessment();
  }

  @Override
  public JsonObject copyAssessmentEvent() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JsonObject deleteAssessmentEvent() {
    return getAssessment();
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

  private JsonObject getCollection() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    String contentId = context.eventBody().getString(EventRequestConstants.ID);
    LOGGER.debug("getting collection for id {}", contentId);

    JsonObject result = null;
    LazyList<AJEntityCollection> collections = AJEntityCollection.findBySQL(AJEntityCollection.SELECT_COLLECTION, contentId);
    if (!collections.isEmpty()) {
      result =
        new JsonObject(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityCollection.COLLECTION_FIELDS).toJson(collections.get(0)));
    }

    Base.close();
    return result;
  }

  private JsonObject getAssessment() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    String contentId = context.eventBody().getString(EventRequestConstants.ID);
    LOGGER.debug("getting assessment for id {}", contentId);

    JsonObject result = null;
    LazyList<AJEntityCollection> assessments = AJEntityCollection.findBySQL(AJEntityCollection.SELECT_ASSESSMENT, contentId);
    if (!assessments.isEmpty()) {
      result =
        new JsonObject(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityCollection.ASSESSMENT_FIELDS).toJson(assessments.get(0)));
    }

    Base.close();
    return result;
  }


}
