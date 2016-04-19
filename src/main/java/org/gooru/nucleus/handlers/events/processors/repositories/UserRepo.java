package org.gooru.nucleus.handlers.events.processors.repositories;

import java.util.List;

public interface UserRepo {

    List<String> getMultipleEmailIds(List<String> userIds);
    
    String getUsername(String userId);
    
    String[] getFirstAndLastName(String userId);
}
