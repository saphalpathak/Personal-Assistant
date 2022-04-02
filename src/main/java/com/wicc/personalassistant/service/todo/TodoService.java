package com.wicc.personalassistant.service.todo;

import com.wicc.personalassistant.dto.todo.TodoDto;
import com.wicc.personalassistant.entity.todo.Todo;
import com.wicc.personalassistant.entity.user.User;
import com.wicc.personalassistant.service.GenericCrudService;

import java.util.List;

public interface TodoService extends GenericCrudService<TodoDto,Integer> {

    //filter data by current date and active status
    List<TodoDto> findTodayAndActiveTask(Integer userId);

    //filter data by task_status
    List<TodoDto> findCompletedTasks(Integer userId);

    List<TodoDto> findAllActiveTask(Integer userId);

    List<TodoDto> findImportantTask(Integer userId);



}
