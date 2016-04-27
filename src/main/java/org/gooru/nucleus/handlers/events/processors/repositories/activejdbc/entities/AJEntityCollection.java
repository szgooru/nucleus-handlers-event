package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

import java.util.Arrays;
import java.util.List;

/**
 * Created by subbu on 12-Jan-2016.
 */
@Table("collection")
@IdName("id")
public class AJEntityCollection extends Model {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String OWNER_ID = "owner_id";
    public static final String CREATOR_ID = "creator_id";
    public static final String ORIGINAL_CREATOR_ID = "original_creator_id";
    public static final String ORIGINAL_COLLECTION_ID = "original_collection_id";
    public static final String PUBLISH_DATE = "publish_date";
    public static final String PUBLISH_STATUS = "publish_status";
    public static final String THUMBNAIL = "thumbnail";
    public static final String LEARNING_OBJECTIVE = "learning_objective";
    public static final String AUDIENCE = "audience";
    public static final String METADATA = "metadata";
    public static final String TAXONOMY = "taxonomy";
    public static final String ORIENTATION = "orientation";
    public static final String SETTING = "setting";
    public static final String GRADING = "grading";
    public static final String VISIBLE_ON_PROFILE = "visible_on_profile";
    public static final String COLLABORATOR = "collaborator";
    public static final String COURSE_ID = "course_id";
    public static final String LESSON_ID = "lesson_id";
    public static final String IS_DELETED = "is_deleted";

    public static final String SELECT_COLLECTION =
        "SELECT id, title, owner_id, creator_id, original_creator_id, original_collection_id, publish_date, publish_status, thumbnail,"
            + " learning_objective, audience, metadata, taxonomy, orientation, setting, grading, visible_on_profile, collaborator, course_id, unit_id,"
            + " lesson_id FROM collection WHERE id = ?::uuid AND format = 'collection'::content_container_type";

    public static final String SELECT_COLLABORATOR =
        "SELECT collaborator FROM collection WHERE id = ?::uuid";
    
    public static final List<String> COLLECTION_FIELDS =
        Arrays.asList(ID, TITLE, OWNER_ID, CREATOR_ID, ORIGINAL_CREATOR_ID, ORIGINAL_COLLECTION_ID, PUBLISH_DATE,
            PUBLISH_STATUS, THUMBNAIL, LEARNING_OBJECTIVE, AUDIENCE, METADATA, TAXONOMY, ORIENTATION, SETTING, GRADING,
            VISIBLE_ON_PROFILE, COLLABORATOR, COURSE_ID, LESSON_ID);

    public static final String SELECT_ASSESSMENT =
        "SELECT id, title, owner_id, creator_id, original_creator_id, original_collection_id, publish_date, publish_status, thumbnail,"
            + " learning_objective, audience, metadata, taxonomy, orientation, setting, grading, visible_on_profile, collaborator, course_id, unit_id,"
            + " lesson_id FROM collection WHERE id = ?::uuid AND format = 'assessment'::content_container_type";

    public static final List<String> ASSESSMENT_FIELDS =
        Arrays.asList(ID, TITLE, OWNER_ID, CREATOR_ID, ORIGINAL_CREATOR_ID, ORIGINAL_COLLECTION_ID, PUBLISH_DATE,
            PUBLISH_STATUS, THUMBNAIL, LEARNING_OBJECTIVE, AUDIENCE, METADATA, TAXONOMY, ORIENTATION, SETTING, GRADING,
            VISIBLE_ON_PROFILE, COLLABORATOR, COURSE_ID, LESSON_ID);

    public static final String SELECT_OWNER_CREATOR =
        "SELECT owner_id, creator_id FROM collection where id = ANY(?::uuid[])";
}
