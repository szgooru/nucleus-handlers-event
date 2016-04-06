package org.gooru.nucleus.handlers.events.processors.repositories;

import java.util.List;

public interface UserRepo {

  public List<String> getMultipleEmailIds(List<String> userIds);
}
