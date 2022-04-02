package com.wicc.personalassistant.service.todo;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.todo.TodoDto;
import com.wicc.personalassistant.entity.todo.Todo;
import com.wicc.personalassistant.enums.ImportantStatus;
import com.wicc.personalassistant.enums.TaskStatus;
import com.wicc.personalassistant.repo.todo.TodoRepo;
import com.wicc.personalassistant.service.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepo todoRepo;

    private final UserService userService;

    public TodoServiceImpl(TodoRepo todoRepo, UserService userService) {
        this.todoRepo = todoRepo;
        this.userService = userService;
    }

    //create new task
    @Override
    public ResponseDto save(TodoDto todoDto) {
        try {
            Todo todo = new Todo(todoDto);
            todoRepo.save(todo);
            //returning value as a response dto
            return new ResponseDto("Task added!!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDto("Task addition failed!!");
        }
    }

    //finding all the tasks
    @Override
    public List<TodoDto> findAll(Integer id) {
        return TodoDto.toDtos(todoRepo.findAllByUserId(id));
    }

    @Override
    public TodoDto findById(Integer integer) {
        Todo todo;
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

    @Override
    public void deleteById(Integer integer) {
        todoRepo.deleteById(integer);
    }

    //filter data by current date and active status and sort according to date
    @Override
    public List<TodoDto> findTodayAndActiveTask(Integer userId) {
        return TodoDto.toDtos(todoRepo.findAllByDate(userId))
                .stream()
                .filter(task -> task.getTaskStatus().equals(TaskStatus.ACTIVE))
                .sorted((o1, o2) -> o1.getDueDate().compareTo(o2.getDueDate()))
                .collect(Collectors.toList());
    }

    //all the completed task
    @Override
    public List<TodoDto> findCompletedTasks(Integer userId) {
        List<TodoDto> all = findAll(userId);
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
    public List<TodoDto> findAllActiveTask(Integer userId) {
        return findAll(userId)
                .stream()
                .filter(task->task.getTaskStatus().equals(TaskStatus.ACTIVE))
                .sorted((o1, o2) -> o1.getDueDate().compareTo(o2.getDueDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findImportantTask(Integer userId) {
        return findAllActiveTask(userId).stream()
                .filter(task-> task.getImportantStatus().equals(ImportantStatus.IMPORTANT))
                .collect(Collectors.toList());
    }


}
