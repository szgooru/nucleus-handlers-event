package org.gooru.nucleus.handlers.events.processors.repositories;

import org.gooru.nucleus.handlers.events.processors.ProcessorContext;
import org.gooru.nucleus.handlers.events.processors.repositories.activejdbc.AJRepoBuilder;

/**
 * Created by subbu on 06-Jan-2016.
 */
public final class RepoBuilder {

    public static ContentRepo buildContentRepo(ProcessorContext context) {
        return AJRepoBuilder.buildContentRepo(context);
    }

    public static CollectionRepo buildCollectionRepo(ProcessorContext context) {
        return AJRepoBuilder.buildCollectionRepo(context);
    }

    public static CourseRepo buildCourseRepo(ProcessorContext context) {
        return AJRepoBuilder.buildCourseRepo(context);
    }

    public static UnitRepo buildUnitRepo(ProcessorContext context) {
        return AJRepoBuilder.buildUnitRepo(context);
    }

    public static LessonRepo buildLessonRepo(ProcessorContext context) {
        return AJRepoBuilder.buildLessonRepo(context);
    }

    public static ClassRepo buildClassRepo(ProcessorContext context) {
        return AJRepoBuilder.buildClassRepo(context);
    }

    public static UserRepo buildUserRepo(ProcessorContext context) {
        return AJRepoBuilder.buildUserRepo(context);
    }

    public static ProfileRepo buildProfileRepo(ProcessorContext context) {
        return AJRepoBuilder.buildProfileRepo(context);
    }

    private RepoBuilder() {
        throw new AssertionError();
    }
}
