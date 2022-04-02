package com.wicc.personalassistant.service.todo;

import com.wicc.personalassistant.dto.todo.TodoDto;
import com.wicc.personalassistant.entity.todo.Todo;
import com.wicc.personalassistant.entity.user.User;
import com.wicc.personalassistant.service.GenericCrudService;

import java.util.List;

public interface TodoService extends GenericCrudService<TodoDto,Integer> {

    //filter data by current date and active status
    List<TodoDto> findTodayAndActiveTask();

    //filter data by task_status
    List<TodoDto> findCompletedTasks();

    List<TodoDto> findAllActiveTask();

    List<TodoDto> findImportantTask();



}
