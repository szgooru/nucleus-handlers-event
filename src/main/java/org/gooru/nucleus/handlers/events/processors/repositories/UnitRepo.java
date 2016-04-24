package org.gooru.nucleus.handlers.events.processors.repositories;

import io.vertx.core.json.JsonObject;

public interface UnitRepo {

    JsonObject createUpdateUnitEvent();

    JsonObject copyUnitEvent();

    JsonObject deleteUnitEvent();

    JsonObject moveUnitEvent();

    JsonObject reorderUnitContentEvent();
    
    JsonObject getUnit(String unitId);
}
