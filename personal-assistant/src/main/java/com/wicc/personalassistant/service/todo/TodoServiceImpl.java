package com.wicc.personalassistant.service.todo;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.todo.TodoDto;
import com.wicc.personalassistant.entity.todo.Todo;
import com.wicc.personalassistant.enums.ImportantStatus;
import com.wicc.personalassistant.enums.TaskStatus;
import com.wicc.personalassistant.repo.todo.TodoRepo;
import com.wicc.personalassistant.utils.CurrentUserDetailService;
import com.wicc.personalassistant.utils.validators.UserValidator;
import org.springframework.stereotype.Service;

import javax.validation.constraints.AssertTrue;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepo todoRepo;

    private final CurrentUserDetailService currentUserDetailService;

    private final UserValidator userValidator;

    public TodoServiceImpl(TodoRepo todoRepo, CurrentUserDetailService currentUserDetailService, UserValidator userValidator) {
        this.todoRepo = todoRepo;
        this.currentUserDetailService = currentUserDetailService;
        this.userValidator = userValidator;
    }

    //create and update task
    @Override
    public ResponseDto save(TodoDto todoDto) {
        try {
            todoDto.setUser(currentUserDetailService.getCurrentUser());
            Todo todo = new Todo(todoDto);
            todo = todoRepo.save(todo);

            //saving data of todo to user for better communication
            currentUserDetailService.getCurrentUser().getTodos().add(todo);

            //returning value as a response dto
            //if id is present the purpose of save is update
            if (todoDto.getId() == null) {
                return new ResponseDto("Task added!!");
            } else {
                return new ResponseDto("Task updated!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (todoDto.getId() == null) {
                return new ResponseDto("Task addition failed!!");
            } else {
                return new ResponseDto("Task update failed!!");
            }
        }
    }

    //finding all the tasks
    @Override
    public List<TodoDto> findAll() {
        //finding all the todos of logged user
        return TodoDto.toDtos(currentUserDetailService.getCurrentUser().getTodos());
    }

    @Override
    public TodoDto findById(Integer integer) {
        Todo todo;
        boolean validTodoId = userValidator.isValidTodoId(integer);
        if (validTodoId) {
            try {
                todo = todoRepo.findById(integer).orElseThrow(
                        () -> new RuntimeException("Not Found")
                );
                return new TodoDto(todo);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public void deleteById(Integer integer) {
        boolean validTodoId = userValidator.isValidTodoId(integer);
        if (validTodoId) {
            todoRepo.deleteById(integer);
        }

    }

    //filter data by current date and active status and sort according to date
    @Override
    public List<TodoDto> findTodayAndActiveTask() {
        return TodoDto.toDtos(todoRepo.findAllByDate(currentUserDetailService.getCurrentUserId()))
                .stream()
                .filter(task -> task.getTaskStatus().equals(TaskStatus.ACTIVE))
                .sorted((o1, o2) -> o1.getDueDate().compareTo(o2.getDueDate()))
                .collect(Collectors.toList());
    }

    //all the completed task
    @Override
    public List<TodoDto> findCompletedTasks() {
        List<TodoDto> all = findAll();
        //getting all the  COMPLETED enum type tasks
        List<TodoDto> collect = all.stream()
                .filter(task -> task.getTaskStatus().equals(TaskStatus.COMPLETED))
                .collect(Collectors.toList());
        //reversing list to find recently completed task
        Collections.reverse(collect);
        return collect;
    }

    //all active tasks and sort
    @Override
    public List<TodoDto> findAllActiveTask() {
        return findAll()
                .stream()
                .filter(task -> task.getTaskStatus().equals(TaskStatus.ACTIVE))
                //sort according to date
                .sorted((o1, o2) -> o1.getDueDate().compareTo(o2.getDueDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findImportantTask() {
        return findAllActiveTask().stream()
                .filter(task -> task.getImportantStatus().equals(ImportantStatus.IMPORTANT))
                .collect(Collectors.toList());
    }


}
