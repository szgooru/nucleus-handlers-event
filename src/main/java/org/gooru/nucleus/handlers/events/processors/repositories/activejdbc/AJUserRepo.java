package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.UserRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityUserDemographic;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AJUserRepo implements UserRepo {
  
  private final ProcessorContext context;
  private static final Logger LOGGER = LoggerFactory.getLogger(AJUserRepo.class);

  public AJUserRepo(ProcessorContext context) {
    this.context = context;
  }
  
  @Override
  public List<String> getMultipleEmailIds(List<String> userIds) {
    Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
    LazyList<AJEntityUserDemographic> emailIdsFromDB = AJEntityUserDemographic.findBySQL(AJEntityUserDemographic.SELECT_MULTIPLE_EMAILIDS, listToPostgresArrayString(userIds));
    List<String> emailIds = new ArrayList<>();
    emailIdsFromDB.forEach(email -> emailIds.add(email.getString(AJEntityUserDemographic.EMAIL_ID)));
    Base.close();
    return emailIds;
  }

  private String listToPostgresArrayString(List<String> input) {
    int approxSize = ((input.size() + 1) * 36); // Length of UUID is around 36
                                                // chars
    Iterator<String> it = input.iterator();
    if (!it.hasNext()) {
      return "{}";
    }

    StringBuilder sb = new StringBuilder(approxSize);
    sb.append('{');
    for (;;) {
      String s = it.next();
      sb.append('"').append(s).append('"');
      if (!it.hasNext()) {
        return sb.append('}').toString();
      }
      sb.append(',');
    }
  }
}
