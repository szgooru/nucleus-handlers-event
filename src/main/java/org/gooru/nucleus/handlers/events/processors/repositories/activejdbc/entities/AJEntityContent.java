package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by subbu on 06-Jan-2016.
 */
@Table("content")
@IdName("id")
public class AJEntityContent extends Model {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String PUBLIH_STATUS = "publish_status";
    public static final String ANSWER = "answer";
    public static final String METADATA = "metadata";
    public static final String TAXONOMY = "taxonomy";
    public static final String HINT_EXPLANATION_DETAIL = "hint_explanation_detail";
    public static final String THUMBNAIL = "thumbnail";
    public static final String CREATOR_ID = "creator_id";

    public static final String ORIGINAL_CREATOR_ID = "original_creator_id";
    public static final String PARENT_CONTENT_ID = "parent_content_id";
    public static final String ORIGINAL_CONTENT_ID = "original_content_id";
    public static final String PUBLISH_DATE = "publish_date";
    public static final String NARRATION = "narration";
    public static final String DESCRIPTION = "description";
    public static final String CONTENT_FORMAT = "content_format";
    public static final String CONTENT_SUBFORMAT = "content_subformat";
    public static final String IS_COPYRIGHT_OWNER = "is_copyright_owner";
    public static final String COPYRIGHT_OWNER = "copyright_owner";
    public static final String RESOURCE_INFO = "info";
    public static final String VISIBLE_ON_PROFILE = "visible_on_profile";
    public static final String DISPLAY_GUIDE = "display_guide";
    public static final String ACCESSIBILITY = "accessibility";
    public static final String IS_DELETED = "is_deleted";
    public static final String MODIFIER_ID = "modifier_id";

    public static final String COURSE_ID = "course_id";
    public static final String UNIT_ID = "unit_id";
    public static final String LESSON_ID = "lesson_id";
    public static final String COLLECTION_ID = "collection_id";
    public static final String SEQUENCE_ID = "sequence_id";

    public static final String SELECT_RESOURCE =
        "SELECT id, title, url, creator_id, modifier_id, original_creator_id, original_content_id, parent_content_id, publish_date, publish_status,"
        + " narration, description, content_format, content_subformat, metadata, taxonomy, thumbnail, course_id, unit_id, lesson_id, collection_id,"
        + " is_copyright_owner, copyright_owner, info, visible_on_profile, display_guide, accessibility, is_deleted FROM content WHERE id = ?::uuid"
        + " AND content_format = 'resource'::content_format_type";

    public static final List<String> RESOURCE_FIELDS = Arrays.asList(ID, TITLE, URL, CREATOR_ID, MODIFIER_ID, ORIGINAL_CREATOR_ID, 
        ORIGINAL_CONTENT_ID, PARENT_CONTENT_ID, PUBLISH_DATE, PUBLIH_STATUS, NARRATION, DESCRIPTION, CONTENT_FORMAT, CONTENT_SUBFORMAT, METADATA,
        TAXONOMY, THUMBNAIL, COURSE_ID, UNIT_ID, LESSON_ID, COLLECTION_ID, IS_COPYRIGHT_OWNER, COPYRIGHT_OWNER, RESOURCE_INFO, VISIBLE_ON_PROFILE,
        DISPLAY_GUIDE, ACCESSIBILITY, IS_DELETED);

    public static final String SELECT_QUESTION =
        "SELECT id, title, creator_id, modifier_id, original_creator_id, original_content_id, parent_content_id, publish_date, publish_status,"
        + " description, content_format, content_subformat, answer, metadata, taxonomy, hint_explanation_detail, thumbnail, course_id, unit_id,"
        + " lesson_id, collection_id, is_copyright_owner, copyright_owner, visible_on_profile, is_deleted FROM content WHERE id = ?::uuid AND"
        + " content_format = 'question'::content_format_type";

    public static final List<String> QUESTION_FIELDS = Arrays.asList(ID, TITLE, CREATOR_ID, MODIFIER_ID, ORIGINAL_CREATOR_ID, ORIGINAL_CONTENT_ID,
        PARENT_CONTENT_ID, PUBLISH_DATE, PUBLIH_STATUS, DESCRIPTION, CONTENT_FORMAT, CONTENT_SUBFORMAT, ANSWER, METADATA, TAXONOMY, 
        HINT_EXPLANATION_DETAIL, THUMBNAIL, COURSE_ID, UNIT_ID, LESSON_ID, COLLECTION_ID, IS_COPYRIGHT_OWNER, COPYRIGHT_OWNER, VISIBLE_ON_PROFILE,
        IS_DELETED);


    public static final String SELECT_CONTENT_FORMAT = "SELECT content_format FROM content WHERE id = ?::uuid";

}
