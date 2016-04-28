package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.Arrays;
import java.util.List;

@Table("course")
public class AJEntityCourse extends Model {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String OWNER_ID = "owner_id";
    public static final String CREATOR_ID = "creator_id";
    public static final String MODIFIER_ID = "modifier_id";
    public static final String ORIGINAL_CREATOR_ID = "original_creator_id";
    public static final String ORIGINAL_COURSE_ID = "original_course_id";
    public static final String PUBLISH_DATE = "publish_date";
    public static final String PUBLISH_STATUS = "publish_status";
    public static final String THUMBNAIL = "thumbnail";
    public static final String METADATA = "metadata";
    public static final String TAXONOMY = "taxonomy";
    public static final String COLLABORATOR = "collaborator";
    public static final String VISIBLE_ON_PROFILE = "visible_on_profile";
    public static final String IS_DELETED = "is_deleted";
    public static final String SEQUENCE_ID = "sequence_id";
    public static final String SUBJECT_BUCKET = "subject_bucket";
    public static final String CREATOR_SYSTEM = "creator_system";

    public static final String SELECT_COURSE =
        "SELECT id, title, description, created_at, updated_at, owner_id, creator_id, modifier_id, original_creator_id, original_course_id,"
            + " parent_course_id, publish_date, publish_status, thumbnail, metadata, taxonomy, collaborator, visible_on_profile, is_deleted,"
            + " sequence_id, subject_bucket, creator_system FROM course WHERE id = ?::uuid";
    
    public static final String SELECT_COLLABORATOR = "SELECT collaborator FROM course where  id = ?::uuid";

    public static final List<String> ALL_FIELDS =
        Arrays.asList(ID, TITLE, DESCRIPTION, OWNER_ID, CREATOR_ID, ORIGINAL_CREATOR_ID, MODIFIER_ID,
            ORIGINAL_COURSE_ID, PUBLISH_STATUS, PUBLISH_DATE, THUMBNAIL, METADATA, TAXONOMY, COLLABORATOR,
            VISIBLE_ON_PROFILE, IS_DELETED, CREATED_AT, UPDATED_AT, SEQUENCE_ID, SUBJECT_BUCKET, CREATOR_SYSTEM);
}
