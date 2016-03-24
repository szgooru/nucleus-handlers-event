package org.gooru.nucleus.handlers.events.processors;

import org.gooru.nucleus.handlers.events.constants.MessageConstants;
import org.gooru.nucleus.handlers.events.processors.exceptions.InvalidRequestException;
import org.gooru.nucleus.handlers.events.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.events.processors.responseobject.ResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

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

      final String msgOp = request.getString(MessageConstants.MSG_EVENT_NAME);
      LOGGER.debug("## Processing Event: " + msgOp);

      switch (msgOp) {
        case MessageConstants.MSG_OP_EVT_COURSE_CREATE:
        case MessageConstants.MSG_OP_EVT_COURSE_UPDATE:
        case MessageConstants.MSG_OP_EVT_COURSE_COPY:
          result = processEventCourseCreateUpdateCopy();
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
        case MessageConstants.MSG_OP_EVT_UNIT_COPY:
          result = processEventUnitCreateUpdateCopy();
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
        case MessageConstants.MSG_OP_EVT_LESSON_COPY:
          result = processEventLessonCreateUpdateCopy();
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
        case MessageConstants.MSG_OP_EVT_COLLECTION_COPY:
          result = processEventCollectionCreateUpdateCopy();
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
          
        case MessageConstants.MSG_OP_EVT_ASSESSMENT_CREATE:
        case MessageConstants.MSG_OP_EVT_ASSESSMENT_UPDATE:
        case MessageConstants.MSG_OP_EVT_ASSESSMENT_COPY:
          result = processEventAssessmentCreateUpdateCopy();
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
          
        case MessageConstants.MSG_OP_EVT_RESOURCE_CREATE:
        case MessageConstants.MSG_OP_EVT_RESOURCE_UPDATE:
        case MessageConstants.MSG_OP_EVT_RESOURCE_COPY:
          result = processEventResourceCreateUpdateCopy();
          break;
          
        case MessageConstants.MSG_OP_EVT_RESOURCE_DELETE:
          result = processEventResourceDelete();
          break;
          
        case MessageConstants.MSG_OP_EVT_QUESTION_CREATE:
        case MessageConstants.MSG_OP_EVT_QUESTION_UPDATE:
        case MessageConstants.MSG_OP_EVT_QUESTION_COPY:
          result = processEventQuestionCreateUpdateCopy();
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
          
        default:
          LOGGER.error("Invalid operation type passed in, not able to handle");
          throw new InvalidRequestException();
      }
      return result;
    } catch (InvalidRequestException e) {
      TRANSMIT_FAIL_LOGGER.error( ResponseFactory.generateErrorResponse((JsonObject) (message != null ? message.body() : null)).toString() );
    }
    return result;
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
    String eventName = request.getString(MessageConstants.MSG_EVENT_NAME);
    JsonObject eventBody = request.getJsonObject(MessageConstants.MSG_EVENT_BODY);
    String id = eventBody.getString(MessageConstants.MSG_EVENT_CONTENT_ID);
    return new ProcessorContext(eventName, eventBody, id);
  }

  private JsonObject processEventCourseCreateUpdateCopy() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildCourseRepo(context).createUpdateCopyCourseEvent();
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
    // TODO Do NOT call create context here as it will not contain single course id
    //It will contain subject bucket
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
  
  private JsonObject processEventUnitCreateUpdateCopy() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildUnitRepo(context).createUpdateCopyUnitEvent();
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

  private JsonObject processEventLessonCreateUpdateCopy() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildLessonRepo(context).createUpdateCopyLessonEvent();
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

  private JsonObject processEventCollectionCreateUpdateCopy() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildCollectionRepo(context).createUpdateCopyCollectionEvent();
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

  private JsonObject processEventAssessmentCreateUpdateCopy() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildCollectionRepo(context).createUpdateCopyAssessmentEvent();
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
  
  private JsonObject processEventResourceCreateUpdateCopy() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildContentRepo(context).createUpdateCopyResourceEvent();
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
  
  private JsonObject processEventQuestionCreateUpdateCopy() {
    try {
      ProcessorContext context = createContext();
      JsonObject result = RepoBuilder.buildContentRepo(context).createUpdateCopyQuestionEvent();
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
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventClassStudentInvite() {
    // TODO Auto-generated method stub
    return null;
  }

  private JsonObject processEventClassStudentJoin() {
    // TODO Auto-generated method stub
    return null;
  }
  
  private JsonObject processEventClassContentVisible() {
    // TODO Auto-generated method stub
    return null;
  }
}
