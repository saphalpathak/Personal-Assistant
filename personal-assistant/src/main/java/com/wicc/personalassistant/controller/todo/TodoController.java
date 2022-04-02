package com.wicc.personalassistant.controller.todo;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.todo.TodoDto;
import com.wicc.personalassistant.enums.ImportantStatus;
import com.wicc.personalassistant.enums.TaskStatus;
import com.wicc.personalassistant.service.todo.TodoService;
import com.wicc.personalassistant.utils.CurrentDateTimeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;
    private final CurrentDateTimeUtil currentDateTimeUtil;

    public TodoController(TodoService todoService, CurrentDateTimeUtil currentDateTimeUtil) {
        this.todoService = todoService;
        this.currentDateTimeUtil = currentDateTimeUtil;
    }


    @GetMapping("/home")
    public String todoHome(Model model) {
        //find only 8 task because front-end has only that much space
        List<TodoDto> todayAndActiveTask = todoService.findTodayAndActiveTask()
                .stream()
                .limit(8)
                .collect(Collectors.toList());
        List<TodoDto> completedTasks = todoService.findCompletedTasks()
                .stream()
                .limit(8)
                .collect(Collectors.toList());
        model.addAttribute("currentDate", currentDateTimeUtil.getDayAndMonth());
        model.addAttribute("completedTask", completedTasks);
        model.addAttribute("todayTask", todayAndActiveTask);
        return "todo/todo_landing";
    }

    @GetMapping("add")
    public String addTask(Model model) {
        model.addAttribute("todoDto", new TodoDto());
        model.addAttribute("heading", "Add Task");
        return "todo/add_task";
    }

    //creating new task
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute TodoDto todoDto, BindingResult bindingResult,
                         RedirectAttributes attributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("todoDto", todoDto);
            model.addAttribute("message", "Task addition failed!!");
            return "todo/add_task";
        }
        //always active while creating task
        todoDto.setTaskStatus(TaskStatus.ACTIVE);

        //set important status not important if it is null (for update purpose)
        if (todoDto.getImportantStatus() == null) {
            todoDto.setImportantStatus(ImportantStatus.NOT_IMPORTANT);
        }
        //set important status starting date if it is null (for update purpose)
        if (todoDto.getStartingDate() == null) {
            todoDto.setStartingDate(new Date());
        }
        ResponseDto save = todoService.save(todoDto);
        attributes.addFlashAttribute("message", save.getMessage());
        return "redirect:/todo/add";
    }

    //mark as completed
    @GetMapping("/completed/{id}")
    public String completed(@PathVariable Integer id) {
        TodoDto todoDto = todoService.findById(id);
        todoDto.setTaskStatus(TaskStatus.COMPLETED);
        todoService.save(todoDto);
        return "redirect:/todo/home";
    }

    //mark as active
    @GetMapping("/active/{id}")
    public String uncompleted(@PathVariable Integer id) {
        TodoDto todoDto = todoService.findById(id);
        todoDto.setTaskStatus(TaskStatus.ACTIVE);
        todoService.save(todoDto);
        return "redirect:/todo/home";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Integer id, Model model) {
        TodoDto byId = todoService.findById(id);
        model.addAttribute("todoDto", byId);
        model.addAttribute("heading", "Update Task");
        return "todo/add_task";
    }

    //delete task
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        todoService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Task deleted");
        return "redirect:/todo/home";
    }

    //description
    @GetMapping("/desc/{id}")
    public String getDesc(@PathVariable Integer id, Model model) {
        TodoDto byId = todoService.findById(id);
        model.addAttribute("data", byId);
        model.addAttribute("heading", "Task");
        return "todo/task_description";
    }

    //all active task
    @GetMapping("/all")
    public String getAll(Model model) {
        List<TodoDto> allActiveTask = todoService.findAllActiveTask();
        model.addAttribute("heading", "Pending Tasks");
        model.addAttribute("allTask", allActiveTask);
        return "todo/todo_full_detail";
    }

    @GetMapping("/all-today")
    public String allTodayTask(Model model) {
        List<TodoDto> todayAndActiveTask = todoService.findTodayAndActiveTask();
        model.addAttribute("heading", "Today's Task");
        model.addAttribute("allTask", todayAndActiveTask);
        return "todo/todo_full_detail";
    }

    //mark as important
    @GetMapping("/important/{id}")
    public String markImportant(@PathVariable Integer id) {
        TodoDto byId = todoService.findById(id);
        byId.setImportantStatus(ImportantStatus.IMPORTANT);
        todoService.save(byId);
        return "redirect:/todo/home";
    }

    //get all important
    @GetMapping("/all-important")
    public String getAllImportant(Model model) {
        List<TodoDto> importantTask = todoService.findImportantTask();
        //reversing list to find the latest important commit task
        Collections.reverse(importantTask);
        model.addAttribute("heading", "Important Task");
        model.addAttribute("allTask", importantTask);
        return "todo/todo_full_detail";
    }
}
