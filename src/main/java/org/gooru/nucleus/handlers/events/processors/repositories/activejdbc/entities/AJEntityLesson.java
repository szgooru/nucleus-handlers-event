package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

import java.util.Arrays;
import java.util.List;

@Table("lesson")
@IdName("lesson_id")
public class AJEntityLesson extends Model {

    public static final String LESSON_ID = "lesson_id";
    public static final String UNIT_ID = "unit_id";
    public static final String COURSE_ID = "course_id";
    public static final String TITLE = "title";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String OWNER_ID = "owner_id";
    public static final String CREATOR_ID = "creator_id";
    public static final String MODIFIER_ID = "modifier_id";
    public static final String ORIGINAL_CREATOR_ID = "original_creator_id";
    public static final String ORIGINAL_LESSON_ID = "original_lesson_id";
    public static final String METADATA = "metadata";
    public static final String TAXONOMY = "taxonomy";
    public static final String SEQUENCE_ID = "sequence_id";
    public static final String IS_DELETED = "is_deleted";
    public static final String CREATOR_SYSTEM = "creator_system";

    public static final String SELECT_LESSON =
        "SELECT lesson_id, unit_id, course_id, title, created_at, updated_at, owner_id, creator_id, modifier_id, original_creator_id, "
            + "original_lesson_id, metadata, taxonomy, sequence_id, is_deleted, creator_system FROM lesson WHERE lesson_id = ?::uuid";

    public static final List<String> ALL_FIELDS =
        Arrays.asList(LESSON_ID, UNIT_ID, COURSE_ID, TITLE, CREATED_AT, UPDATED_AT, CREATOR_ID, MODIFIER_ID, OWNER_ID,
            ORIGINAL_CREATOR_ID, ORIGINAL_LESSON_ID, METADATA, TAXONOMY, SEQUENCE_ID, IS_DELETED, CREATOR_SYSTEM);
}
