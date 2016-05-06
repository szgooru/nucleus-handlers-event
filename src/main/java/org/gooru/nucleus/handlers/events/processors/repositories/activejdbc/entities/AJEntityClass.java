package org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("class")
@IdName("id")
public class AJEntityClass extends Model {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String GREETING = "greeting";
    public static final String CLASS_SHARING = "class_sharing";
    public static final String COVER_IMAGE = "cover_image";
    public static final String GRADE = "grade";
    public static final String CODE = "code";
    public static final String MIN_SCORE = "min_score";
    public static final String END_DATE = "end_date";
    public static final String GOORU_VERSION = "gooru_version";
    public static final String CONTENT_VISIBILITY = "content_visibility";
    public static final String IS_ARCHIVED = "is_archived";
    public static final String COLLABORATOR = "collaborator";
    public static final String COURSE_ID = "course_id";
    public static final String ROSTER_ID = "roster_id";
    public static final String IS_DELETED = "is_deleted";
    public static final String CREATOR_ID = "creator_id";
    public static final String MODIFIER_ID = "modifier_id";

    public static final String SELECT_QUERY = "id = ?::uuid";

    public static final String SELECT_COLLABORATOR = "SELECT collaborator FROM class WHERE id = ?::uuid";

    public static final List<String> ALL_FIELDS =
        Arrays.asList(ID, CREATOR_ID, TITLE, DESCRIPTION, GREETING, GRADE, CLASS_SHARING, COVER_IMAGE, GRADE, CODE,
            MIN_SCORE, END_DATE, COURSE_ID, COLLABORATOR, GOORU_VERSION, CONTENT_VISIBILITY, IS_ARCHIVED);
    public static final String SELECT_CLASSID_FOR_COURSE = "SELECT id FROM class WHERE course_id = ?::uuid";

    public static final String SELECT_CONTENT_VISIBILITY = "SELECT content_visibility FROM class WHERE id = ?::uuid";
}
