package com.wicc.personalassistant.entity.todo;

import com.wicc.personalassistant.dto.todo.TodoDto;
import com.wicc.personalassistant.entity.user.User;
import com.wicc.personalassistant.enums.ImportantStatus;
import com.wicc.personalassistant.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "todo")
public class Todo {
    @Id
    @SequenceGenerator(name = "todo_id_GEN",sequenceName = "todo_id_GEN",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "todo_id_GEN")
    private Integer id;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "starting_date",nullable = false)
    private Date startingDate;

    @Column(name = "due_date",nullable = false)
    private Date dueDate;
    @Column(name = "task_status")
    private TaskStatus taskStatus;

    private ImportantStatus importantStatus;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",foreignKey = @ForeignKey(name = "FK_todo_user"))
    private User user;

    //converting dto to entity
    public Todo(TodoDto todoDto) throws ParseException {
        this.id = todoDto.getId();
        this.title = todoDto.getTitle();
        this.description = todoDto.getDescription();
        this.dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(todoDto.getDueDate());
        this.taskStatus = todoDto.getTaskStatus();
        this.importantStatus = todoDto.getImportantStatus();
        this.user = todoDto.getUser();
        this.startingDate = todoDto.getStartingDate();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setImportantStatus(ImportantStatus importantStatus) {
        this.importantStatus = importantStatus;
    }
}
