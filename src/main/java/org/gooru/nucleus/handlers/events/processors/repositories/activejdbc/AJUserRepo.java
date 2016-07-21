package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gooru.nucleus.handlers.events.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.EventResponseConstants;
import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.UserRepo;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityUserDemographic;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities.AJEntityUserIdentity;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;

import io.vertx.core.json.JsonObject;

public class AJUserRepo implements UserRepo {

    private final ProcessorContext context;

    public AJUserRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public List<String> getMultipleEmailIds(List<String> userIds) {
        Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
        LazyList<AJEntityUserDemographic> emailIdsFromDB = AJEntityUserDemographic
            .findBySQL(AJEntityUserDemographic.SELECT_MULTIPLE_EMAILIDS, listToPostgresArrayString(userIds));
        List<String> emailIds = new ArrayList<>();
        emailIdsFromDB.forEach(email -> emailIds.add(email.getString(AJEntityUserDemographic.EMAIL_ID)));
        Base.close();
        return emailIds;
    }
    
    @Override
    public String getUsername(String userId) {
        Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
        String username = null;
        LazyList<AJEntityUserIdentity> usernames = AJEntityUserIdentity.findBySQL(AJEntityUserIdentity.SELECT_USERNAME, userId);
        if (!usernames.isEmpty()) {
            username = usernames.get(0).getString(AJEntityUserIdentity.USERNAME);
        }
        Base.close();
        return username;
    }
    
    @Override
    public String[] getFirstAndLastName(String userId) {
        Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
        String[] firstLastName = new String[2];
        LazyList<AJEntityUserDemographic> firstLastNames = AJEntityUserDemographic.findBySQL(AJEntityUserDemographic.SELECT_FIRST_LAST_NAME, userId);
        if (!firstLastNames.isEmpty()) {
            firstLastName[0] = firstLastNames.get(0).getString(AJEntityUserDemographic.FIRSTNAME);
            firstLastName[1] = firstLastNames.get(0).getString(AJEntityUserDemographic.LASTNAME);
        }
        Base.close();
        return firstLastName;
    }

    private String listToPostgresArrayString(List<String> input) {
        int approxSize = ((input.size() + 1) * 36); // Length of UUID is around
                                                    // 36
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

    @Override
    public JsonObject getUser() {
        Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
        String userId = context.eventBody().getString(EventRequestConstants.ID);
        LazyList<AJEntityUserDemographic> userDemographics = AJEntityUserDemographic.find("id = ?::uuid", userId);
        LazyList<AJEntityUserIdentity> userIdentities = AJEntityUserIdentity.find("user_id = ?::uuid", userId);
        JsonObject userIdentityJson = new JsonObject();
        if (!userIdentities.isEmpty()) {
            userIdentityJson = new JsonObject(new JsonFormatterBuilder()
                .buildSimpleJsonFormatter(false, AJEntityUserIdentity.ALL_FIELDS).toJson(userIdentities.get(0)));
        }
        JsonObject userDemographicJson = new JsonObject();
        if (!userDemographics.isEmpty()) {
            userDemographicJson = new JsonObject(new JsonFormatterBuilder()
                .buildSimpleJsonFormatter(false, AJEntityUserDemographic.ALL_FIELDS).toJson(userDemographics.get(0)));
        }

        JsonObject result = new JsonObject();
        result.put(EventResponseConstants.USER_DEMOGRAPHIC, userDemographicJson);
        result.put(EventResponseConstants.USER_IDENTITY, userIdentityJson);
        Base.close();
        return result;
    }

}
