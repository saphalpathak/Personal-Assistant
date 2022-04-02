package com.wicc.personalassistant.dto.todo;

import com.wicc.personalassistant.entity.todo.Todo;
import com.wicc.personalassistant.entity.user.User;
import com.wicc.personalassistant.enums.ImportantStatus;
import com.wicc.personalassistant.enums.TaskStatus;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TodoDto {

    private Integer id;

    @NotEmpty(message = "Title can not be empty!!")
    private String title;

    @NotEmpty(message = "Description can not be empty!!")
    private String description;

    @NotEmpty(message = "Date can not be empty!!")
    private String dueDate;

    private Date startingDate;

    private TaskStatus taskStatus;

    private ImportantStatus importantStatus;

    private User user;


    //converting entity to dto
    public TodoDto(Todo todo){
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.description = todo.getDescription();
        this.dueDate = todo.getDueDate().toString();
        this.startingDate =  todo.getStartingDate();
        this.taskStatus = todo.getTaskStatus();
        this.importantStatus = todo.getImportantStatus();
        this.user = todo.getUser();
    }

    //converting list of entities to dto
    public static List<TodoDto> toDtos(List<Todo> todos){
        List<TodoDto> todoDtos = new ArrayList<>();
        todos.forEach(todo-> todoDtos.add(new TodoDto(todo)));
        return todoDtos;
    }
}
