package org.gooru.nucleus.handlers.events.processors;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gooru.nucleus.handlers.events.constants.EventRequestConstants;
import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.gooru.nucleus.handlers.events.processors.exceptions.InvalidRequestException;
import org.gooru.nucleus.handlers.events.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.events.processors.responseobject.ResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MessageProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);

    // collect all failed event transmissions in this logger....
    private static final Logger TRANSMIT_FAIL_LOGGER = LoggerFactory.getLogger("org.gooru.nucleus.transmission-errors");

    private final Message<Object> message;
    private JsonObject request;

    public MessageProcessor(Message<Object> message) {
        this.message = message;
    }

    @Override
    public JsonObject process() {
        JsonObject result = null;
        try {
            if (!validateAndInitialize()) {
                LOGGER.error("Invalid message received, either null or body of message is not JsonObject");
                throw new InvalidRequestException();
            }
            
            final String msgHeader = message.headers().get(MessageConstants.MSG_HEADER_OP);
            if (msgHeader != null && msgHeader.equalsIgnoreCase(MessageConstants.MSG_OP_EVENT_PUBLISH)) {
                LOGGER.debug("event publisher API request, no need for further execution");
                return request;
            }

            final String msgOp = request.getString(EventRequestConstants.EVENT_NAME);
            LOGGER.debug("## Processing Event: {} ##", msgOp);

            switch (msgOp) {
            case MessageConstants.MSG_OP_EVT_COURSE_CREATE:
            case MessageConstants.MSG_OP_EVT_COURSE_UPDATE:
                result = processEventCourseCreateUpdate();
                break;

            case MessageConstants.MSG_OP_EVT_COURSE_COPY:
                result = processEventCourseCopy();
                break;

            case MessageConstants.MSG_OP_EVT_COURSE_DELETE:
                result = processEventCourseDelete();
                break;

            case MessageConstants.MSG_OP_EVT_COURSE_REORDER:
                result = processEventCourseReorder();
                break;

            case MessageConstants.MSG_OP_EVT_COURSE_CONTENT_REORDER:
                result = processEventCourseContentReorder();
                break;

            case MessageConstants.MSG_OP_EVT_COURSE_COLLABORATOR_UPDATE:
                result = processEventCourseCollaboratorUpdate();
                break;

            case MessageConstants.MSG_OP_EVT_UNIT_CREATE:
            case MessageConstants.MSG_OP_EVT_UNIT_UPDATE:
                result = processEventUnitCreateUpdate();
                break;

            case MessageConstants.MSG_OP_EVT_UNIT_COPY:
                result = processEventUnitCopy();
                break;

            case MessageConstants.MSG_OP_EVT_UNIT_DELETE:
                result = processEventUnitDelete();
                break;

            case MessageConstants.MSG_OP_EVT_UNIT_MOVE:
                result = processEventUnitMove();
                break;

            case MessageConstants.MSG_OP_EVT_UNIT_CONTENT_REORDER:
                result = processEventUnitContentReorder();
                break;

            case MessageConstants.MSG_OP_EVT_LESSON_CREATE:
            case MessageConstants.MSG_OP_EVT_LESSON_UPDATE:
                result = processEventLessonCreateUpdate();
                break;

            case MessageConstants.MSG_OP_EVT_LESSON_COPY:
                result = processEventLessonCopy();
                break;

            case MessageConstants.MSG_OP_EVT_LESSON_DELETE:
                result = processEventLessonDelete();
                break;

            case MessageConstants.MSG_OP_EVT_LESSON_MOVE:
                result = processEventLessonMove();
                break;

            case MessageConstants.MSG_OP_EVT_LESSON_CONTENT_REORDER:
                result = processEventLessonContentReorder();
                break;

            case MessageConstants.MSG_OP_EVT_COLLECTION_CREATE:
            case MessageConstants.MSG_OP_EVT_COLLECTION_UPDATE:
                result = processEventCollectionCreateUpdate();
                break;

            case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
                result = processEventCollectionCopy();
                break;

            case MessageConstants.MSG_OP_EVT_COLLECTION_DELETE:
                result = processEventCollectionDelete();
                break;

            case MessageConstants.MSG_OP_EVT_COLLECTION_COLLABORATOR_UPDATE:
                result = processEventCollectionCollaboratorUpdate();
                break;

            case MessageConstants.MSG_OP_EVT_COLLECTION_CONTENT_ADD:
                result = processEventCollectionContentAdd();
                break;

            case MessageConstants.MSG_OP_EVT_COLLECTION_MOVE:
                result = processEventCollectionMove();
                break;

            case MessageConstants.MSG_OP_EVT_COLLECTION_CONTENT_REORDER:
                result = processEventCollectionContentReorder();
                break;
                
            case MessageConstants.MSG_OP_EVT_COLLECTION_REMOVE:
                result = processEventCollectionRemove();
                break;
                
            case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
            case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
                result = processEventAssessmentCreateUpdate();
                break;

            case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
                result = processEventAssessmentCopy();
                break;

            case MessageConstants.MSG_OP_EVT_ASSESSMENT_DELETE:
                result = processEventAssessmentDelete();
                break;

            case MessageConstants.MSG_OP_EVT_ASSESSMENT_COLLABORATOR_UPDATE:
                result = processEventAssessmentCollaboratorUpdate();
                break;

            case MessageConstants.MSG_OP_EVT_ASSESSMENT_QUESTION_ADD:
                result = processEventAssessmentQuestionAdd();
                break;

            case MessageConstants.MSG_OP_EVT_ASSESSMENT_CONTENT_REORDER:
                result = processEventAssessmentContentReorder();
                break;
                
            case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
            case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
                result = processEventResourceCreateUpdate();
                break;

            case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
                result = processEventResourceCopy();
                break;

            case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
                result = processEventResourceDelete();
                break;

            case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
            case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
                result = processEventQuestionCreateUpdate();
                break;

            case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
                result = processEventQuestionCopy();
                break;

            case MessageConstants.MSG_OP_EVT_QUESTION_DELETE:
                result = processEventQuestionDelete();
                break;

            case MessageConstants.MSG_OP_EVT_CLASS_CREATE:
            case MessageConstants.MSG_OP_EVT_CLASS_UPDATE:
                result = processEventClassCreateUpdate();
                break;

            case MessageConstants.MSG_OP_EVT_CLASS_DELETE:
                result = processEventClassDelete();
                break;

            case MessageConstants.MSG_OP_EVT_CLASS_COLLABORATOR_UPDATE:
                result = processEventClassCollaboratorUpdate();
                break;

            case MessageConstants.MSG_OP_EVT_CLASS_COURSE_ASSIGNED:
                result = processEventClassCourseAssigned();
                break;

            case MessageConstants.MSG_OP_EVT_CLASS_STUDENT_INVITE:
                result = processEventClassStudentInvite();
                break;

            case MessageConstants.MSG_OP_EVT_CLASS_STUDENT_JOIN:
                result = processEventClassStudentJoin();
                break;

            case MessageConstants.MSG_OP_EVT_CLASS_CONTENT_VISIBLE:
                result = processEventClassContentVisible();
                break;
                
            case MessageConstants.MSG_OP_EVT_CLASS_STUDENT_REMOVAL:
                result = processEventClassStudentRemove();
                break;

            case MessageConstants.MSG_OP_EVT_PROFILE_FOLLOW:
            case MessageConstants.MSG_OP_EVT_PROFILE_UNFOLLOW:
                result = processEventProfileFollowUnfollow();
                break;

            default:
                LOGGER.error("Invalid operation type passed in, not able to handle");
                throw new InvalidRequestException();
            }
            return result;
        } catch (InvalidRequestException e) {
            TRANSMIT_FAIL_LOGGER.error(ResponseFactory
                .generateErrorResponse((JsonObject) (message != null ? message.body() : null)).toString());
        }
        return result;
    }

    private JsonObject processEventCollectionRemove() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCollectionRepo(context).removeCollection();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemRemoveResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private boolean validateAndInitialize() {
        if (message == null || !(message.body() instanceof JsonObject)) {
            LOGGER.error("Invalid message received, either null or body of message is not JsonObject ");
            return false;
        }

        request = (JsonObject) message.body();
        if (request == null) {
            LOGGER.error("Invalid JSON payload on message bus. aborting");
            return false;
        }

        return true;
    }

    private ProcessorContext createContext() {
        String eventName = request.getString(EventRequestConstants.EVENT_NAME);
        JsonObject eventBody = request.getJsonObject(EventRequestConstants.EVENT_BODY);
        return new ProcessorContext(eventName, eventBody);
    }

    private JsonObject processEventCourseCreateUpdate() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCourseRepo(context).createUpdateCourseEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemCreateResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventCourseCopy() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCourseRepo(context).copyCourseEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemCopyResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventCourseDelete() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCourseRepo(context).deleteCourseEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemDeleteResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventCourseReorder() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCourseRepo(context).reorderCourseEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemReorderResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventCourseContentReorder() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCourseRepo(context).reorderCourseContentEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemContentReorderResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventCourseCollaboratorUpdate() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCourseRepo(context).updateCourseCollaboratorEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemCollaboratorUpdateResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventUnitCreateUpdate() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildUnitRepo(context).createUpdateUnitEvent();
            if (result != null) {
                LOGGER.debug("getUnit() returned: {}", result);
                return ResponseFactory.generateItemCreateResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventUnitCopy() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildUnitRepo(context).copyUnitEvent();
            if (result != null) {
                LOGGER.debug("getUnit() returned: {}", result);
                return ResponseFactory.generateItemCopyResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventUnitDelete() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildUnitRepo(context).deleteUnitEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemDeleteResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventUnitMove() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildUnitRepo(context).moveUnitEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemMoveResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventUnitContentReorder() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildUnitRepo(context).reorderUnitContentEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemContentReorderResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventLessonCreateUpdate() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildLessonRepo(context).createUpdateLessonEvent();
            if (result != null) {
                LOGGER.debug("getLesson() returned: {}", result);
                return ResponseFactory.generateItemCreateResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventLessonCopy() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildLessonRepo(context).copyLessonEvent();
            if (result != null) {
                LOGGER.debug("getLesson() returned: {}", result);
                return ResponseFactory.generateItemCopyResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventLessonDelete() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildLessonRepo(context).deleteLessonEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemDeleteResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventLessonMove() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildLessonRepo(context).moveLessonEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemMoveResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventLessonContentReorder() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildLessonRepo(context).reorderLessonContentEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemContentReorderResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventCollectionCreateUpdate() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCollectionRepo(context).createUpdateCollectionEvent();
            if (result != null) {
                LOGGER.debug("getCollection() returned: {}", result);
                return ResponseFactory.generateItemCreateResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventCollectionCopy() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCollectionRepo(context).copyCollectionEvent();
            if (result != null) {
                LOGGER.debug("getCollection() returned: {}", result);
                return ResponseFactory.generateItemCopyResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventCollectionDelete() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCollectionRepo(context).deleteCollectionEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemDeleteResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventCollectionCollaboratorUpdate() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCollectionRepo(context).updateCollectionCollaboratorEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemCollaboratorUpdateResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventCollectionContentAdd() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCollectionRepo(context).addContentToCollectionEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemContentAddResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventCollectionMove() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCollectionRepo(context).moveCollectionEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemMoveResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventCollectionContentReorder() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCollectionRepo(context).reorderCollectionContentEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemContentReorderResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventAssessmentCreateUpdate() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCollectionRepo(context).createUpdateAssessmentEvent();
            if (result != null) {
                LOGGER.debug("getAssessment() returned: {}", result);
                return ResponseFactory.generateItemCreateResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventAssessmentCopy() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCollectionRepo(context).copyAssessmentEvent();
            if (result != null) {
                LOGGER.debug("getAssessment() returned: {}", result);
                return ResponseFactory.generateItemCopyResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventAssessmentDelete() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCollectionRepo(context).deleteAssessmentEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemDeleteResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventAssessmentCollaboratorUpdate() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCollectionRepo(context).updateAssessmentCollaboratorEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemCollaboratorUpdateResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventAssessmentQuestionAdd() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCollectionRepo(context).addQuestionToAssessmentEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemContentAddResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventAssessmentContentReorder() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildCollectionRepo(context).reorderAssessmentContentEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemContentReorderResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventResourceCreateUpdate() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildContentRepo(context).createUpdateResourceEvent();
            if (result != null) {
                LOGGER.debug("getResource() returned: {}", result);
                return ResponseFactory.generateItemCreateResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventResourceCopy() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildContentRepo(context).copyResourceEvent();
            if (result != null) {
                LOGGER.debug("getResource() returned: {}", result);
                return ResponseFactory.generateItemCopyResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventResourceDelete() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildContentRepo(context).deletedResourceEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemDeleteResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventQuestionCreateUpdate() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildContentRepo(context).createUpdateQuestionEvent();
            if (result != null) {
                LOGGER.debug("getQuestion() returned: {}", result);
                return ResponseFactory.generateItemCreateResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventQuestionCopy() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildContentRepo(context).copyQuestionEvent();
            if (result != null) {
                LOGGER.debug("getQuestion() returned: {}", result);
                return ResponseFactory.generateItemCopyResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventQuestionDelete() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildContentRepo(context).deletedQuestionEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemDeleteResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventClassCreateUpdate() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildClassRepo(context).craeteUpdateClassEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemCreateResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventClassDelete() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildClassRepo(context).deleteClassEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemDeleteResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventClassCollaboratorUpdate() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildClassRepo(context).updateClassCollaboratorEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateItemCollaboratorUpdateResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventClassCourseAssigned() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildClassRepo(context).assignClassToCourseEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateAssignClassToCourseResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventClassStudentInvite() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildClassRepo(context).inviteStudentToClassEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateInviteStudentResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventClassStudentJoin() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildClassRepo(context).joinClassEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateJoinClassResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventClassContentVisible() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildClassRepo(context).classContentVisibleEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateClassContentVisibleResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }
    
    private JsonObject processEventClassStudentRemove() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildClassRepo(context).classRemoveStudentEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateClassStudentRemoveResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }

    private JsonObject processEventProfileFollowUnfollow() {
        try {
            ProcessorContext context = createContext();
            JsonObject result = RepoBuilder.buildProfileRepo(context).followUnfollowProfileEvent();
            if (result != null) {
                LOGGER.debug("result returned: {}", result);
                return ResponseFactory.generateFollowUnfollowProfileResponse(request, result);
            }
        } catch (Throwable t) {
            LOGGER.error("Error while getting content from database:", t);
        }
        LOGGER.error("Failed to generate event. Input data received {}", request);
        TRANSMIT_FAIL_LOGGER.error(ResponseFactory.generateErrorResponse(request).toString());
        return null;
    }
}
