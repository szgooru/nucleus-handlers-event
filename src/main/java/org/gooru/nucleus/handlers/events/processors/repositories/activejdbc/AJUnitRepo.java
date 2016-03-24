package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.UnitRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityUnit;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

public class AJUnitRepo implements UnitRepo {

  private static final Logger LOGGER = LoggerFactory.getLogger(AJUnitRepo.class);
  private final ProcessorContext context;
  
  public AJUnitRepo(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public JsonObject createUpdateCopyUnitEvent() {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LazyList<AJEntityUnit> units = AJEntityUnit.findBySQL(AJEntityUnit.SELECT_UNIT, context.id());
    JsonObject result = null;
    if (!units.isEmpty()) {
      LOGGER.info("found unit for id {} : " + context.id());
      result = new JsonObject(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityUnit.ALL_FIELDS).toJson(units.get(0)));
    } 
    Base.close();
    return result;
  }

  @Override
  public JsonObject deleteUnitEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject moveUnitEvent() {
    return new JsonObject();
  }

  @Override
  public JsonObject reorderUnitContentEvent() {
    return new JsonObject();
  }

}