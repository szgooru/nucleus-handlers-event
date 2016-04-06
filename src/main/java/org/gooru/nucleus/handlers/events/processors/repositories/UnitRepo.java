package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

public interface UnitRepo {

  public JsonObject createUpdateUnitEvent();
  public JsonObject copyUnitEvent();
  public JsonObject deleteUnitEvent();
  public JsonObject moveUnitEvent();
  public JsonObject reorderUnitContentEvent();
}
