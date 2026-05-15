package vn.edu_hub.service.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu_hub.service.constants.ApiResponseCode;
import vn.edu_hub.service.domain.Answer;
import vn.edu_hub.service.domain.Question;
import vn.edu_hub.service.domain.Task;
import vn.edu_hub.service.dto.request.AnswerRequestDTO;
import vn.edu_hub.service.dto.request.QuestionRequestDTO;
import vn.edu_hub.service.dto.request.SaveQuestionsRequestDTO;
import vn.edu_hub.service.dto.response.AnswerResponseDTO;
import vn.edu_hub.service.dto.response.CommonResponseDTO;
import vn.edu_hub.service.dto.response.QuestionResponseDTO;
import vn.edu_hub.service.exception.BusinessException;
import vn.edu_hub.service.repository.AnswerRepository;
import vn.edu_hub.service.repository.QuestionRepository;
import vn.edu_hub.service.repository.TaskRepository;
import vn.edu_hub.service.utils.ResponseUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionService {
    QuestionRepository questionRepository;
    AnswerRepository answerRepository;
    TaskRepository taskRepository;

    public Page<@NonNull QuestionResponseDTO> getQuestions(Long taskId, Pageable pageable) {
        if(!taskRepository.existsById(taskId)){
            throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Không tìm thấy bài tập");
        }
        Page<@NonNull Question> questions = questionRepository.findByTaskId(taskId, pageable);
        List<Long> questionIds = questions.getContent().stream().map(Question::getId).toList();
        List<Answer> answers = answerRepository.findByQuestionIdIn(questionIds);
        Map<Long, List<AnswerResponseDTO>> answersMap = answers.stream()
                .collect(Collectors.groupingBy(
                        Answer::getQuestionId,
                        Collectors.mapping(answer -> AnswerResponseDTO.builder()
                                        .id(answer.getId())
                                        .content(answer.getContent())
                                        .isCorrect(answer.isCorrect())
                                        .label(answer.getLabel())
                                        .build(),
                                Collectors.toList())));
        return questions.map(question -> QuestionResponseDTO.builder()
                    .id(question.getId())
                    .position(question.getPosition())
                    .content(question.getContent())
                    .score(question.getScore())
                    .answers(answersMap.get(question.getId()))
                    .build());
    }

    public CommonResponseDTO saveQuestions(Long taskId, SaveQuestionsRequestDTO requestDTO) {
        if (!taskRepository.existsById(taskId)) {
            throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Bài tập không tồn tại");
        }
        List<QuestionRequestDTO> questions = requestDTO.getQuestions();
        validateQuestions(questions);
        validateAnswers(questions);

        List<QuestionRequestDTO> newQuestions = questions.stream().filter(q -> q.id() == null).toList();
        List<QuestionRequestDTO> existingQuestions = questions.stream().filter(q -> q.id() != null).toList();

        List<Question> dbQuestions = questionRepository.findByTaskId(taskId);
        processQuestionDeletions(dbQuestions, existingQuestions);
        updateExistingQuestions(existingQuestions, dbQuestions);
        saveNewQuestions(taskId, newQuestions);

        return ResponseUtils.success();
    }

    private void processQuestionDeletions(List<Question> dbQuestions, List<QuestionRequestDTO> existingQuestions) {
        Set<Long> incomingIds = existingQuestions.stream()
                .map(QuestionRequestDTO::id)
                .collect(Collectors.toSet());
        List<Long> toDeleteIds = dbQuestions.stream()
                .map(Question::getId)
                .filter(id -> !incomingIds.contains(id))
                .toList();
        if (toDeleteIds.isEmpty()) return;
        answerRepository.deleteByQuestionIdIn(toDeleteIds);
        questionRepository.deleteByIdIn(toDeleteIds);
    }

    private void updateExistingQuestions(List<QuestionRequestDTO> existingQuestions, List<Question> dbQuestions) {
        if (existingQuestions.isEmpty()) return;
        Map<Long, Question> dbQuestionMap = dbQuestions.stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));
        existingQuestions.forEach(dto -> {
            if (!dbQuestionMap.containsKey(dto.id()))
                throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Câu hỏi không tồn tại: " + dto.id());
        });
        List<Question> toUpdate = existingQuestions.stream()
                .map(dto -> applyQuestionUpdate(dbQuestionMap.get(dto.id()), dto))
                .toList();
        questionRepository.saveAll(toUpdate);
        updateAnswersInBatch(existingQuestions);
    }

    private Question applyQuestionUpdate(Question question, QuestionRequestDTO dto) {
        question.setPosition(dto.position());
        question.setContent(dto.content());
        question.setScore(dto.score());
        return question;
    }

    private void updateAnswersInBatch(List<QuestionRequestDTO> existingQuestions) {
        List<Long> questionIds = existingQuestions.stream().map(QuestionRequestDTO::id).toList();
        List<Answer> dbAnswers = answerRepository.findByQuestionIdIn(questionIds);
        Map<Long, List<Answer>> dbAnswersByQuestionId = dbAnswers.stream()
                .collect(Collectors.groupingBy(Answer::getQuestionId));
        List<Long> toDeleteIds = computeAnswerDeletions(existingQuestions, dbAnswersByQuestionId);
        List<Answer> toSave = computeAnswerUpserts(existingQuestions, dbAnswersByQuestionId);
        if (!toDeleteIds.isEmpty()) answerRepository.deleteByIdIn(toDeleteIds);
        if (!toSave.isEmpty()) answerRepository.saveAll(toSave);
    }

    private List<Long> computeAnswerDeletions(List<QuestionRequestDTO> questions,
                                              Map<Long, List<Answer>> dbAnswersByQuestionId) {
        return questions.stream()
                .flatMap(dto -> {
                    Set<Long> incomingIds = dto.answers().stream()
                            .map(AnswerRequestDTO::id)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());
                    return dbAnswersByQuestionId.getOrDefault(dto.id(), List.of()).stream()
                            .map(Answer::getId)
                            .filter(id -> !incomingIds.contains(id));
                })
                .toList();
    }

    private List<Answer> computeAnswerUpserts(List<QuestionRequestDTO> questions,
                                              Map<Long, List<Answer>> dbAnswersByQuestionId) {
        return questions.stream()
                .flatMap(dto -> {
                    Map<Long, Answer> dbAnswerMap = dbAnswersByQuestionId
                            .getOrDefault(dto.id(), List.of()).stream()
                            .collect(Collectors.toMap(Answer::getId, Function.identity()));
                    return dto.answers().stream().map(a -> {
                        if (a.id() == null) return buildAnswer(dto.id(), a);
                        Answer existing = dbAnswerMap.get(a.id());
                        if (existing == null)
                            throw new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND, "Câu trả lời không tồn tại: " + a.id());
                        return applyAnswerUpdate(existing, a);
                    });
                })
                .toList();
    }

    private Answer applyAnswerUpdate(Answer answer, AnswerRequestDTO dto) {
        answer.setLabel(dto.label());
        answer.setContent(dto.content());
        answer.setCorrect(dto.isCorrect());
        return answer;
    }

    private void saveNewQuestions(Long taskId, List<QuestionRequestDTO> newQuestions) {
        if (newQuestions.isEmpty()) return;
        List<Question> saved = questionRepository.saveAll(
                newQuestions.stream().map(dto -> buildQuestion(taskId, dto)).toList()
        );
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < saved.size(); i++) {
            Long questionId = saved.get(i).getId();
            newQuestions.get(i).answers().stream()
                    .map(dto -> buildAnswer(questionId, dto))
                    .forEach(answers::add);
        }
        answerRepository.saveAll(answers);
    }

    private Question buildQuestion(Long taskId, QuestionRequestDTO dto) {
        return Question.builder()
                .taskId(taskId)
                .position(dto.position())
                .content(dto.content())
                .score(dto.score())
                .build();
    }

    private Answer buildAnswer(Long questionId, AnswerRequestDTO dto) {
        return Answer.builder()
                .questionId(questionId)
                .label(dto.label())
                .content(dto.content())
                .isCorrect(dto.isCorrect())
                .build();
    }

    private void validateQuestions(List<QuestionRequestDTO> questions) {
        Set<Integer> positions = questions.stream()
                .map(QuestionRequestDTO::position)
                .collect(Collectors.toSet());
        if (positions.size() != questions.size()) {
            throw new BusinessException(ApiResponseCode.BAD_REQUEST, "Vị trí của câu hỏi không được trùng nhau");
        }

        Set<String> content = questions.stream()
                .map(QuestionRequestDTO::content)
                .collect(Collectors.toSet());
        if (content.size() != questions.size()) {
            throw new BusinessException(ApiResponseCode.BAD_REQUEST, "Nội dung của câu hỏi không được trùng nhau");
        }
    }

    private void validateAnswers(List<QuestionRequestDTO> questions) {
        questions.forEach(question -> {
            Set<String> labels = new HashSet<>();
            Set<String> content = new HashSet<>();
            boolean hasCorrect = false;
            for (AnswerRequestDTO answer : question.answers()) {
                labels.add(answer.label());
                content.add(answer.content());
                if (answer.isCorrect()) hasCorrect = true;
            }
            if (labels.size() != question.answers().size() || content.size() != question.answers().size()) {
                throw new BusinessException(ApiResponseCode.BAD_REQUEST,
                        "Danh sách câu trả lời của câu " + question.position() + " không hợp lệ");
            }
            if (!hasCorrect) {
                throw new BusinessException(ApiResponseCode.BAD_REQUEST,
                        "Câu hỏi " + question.position() + " phải có ít nhất một đáp án đúng");
            }
        });
    }
}
