package com.wicc.personalassistant.controller.todo;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.todo.TodoDto;
import com.wicc.personalassistant.entity.user.User;
import com.wicc.personalassistant.enums.ImportantStatus;
import com.wicc.personalassistant.enums.TaskStatus;
import com.wicc.personalassistant.repo.user.UserRepo;
import com.wicc.personalassistant.service.todo.TodoService;
import com.wicc.personalassistant.service.user.CurrentUserDetailService;
import com.wicc.personalassistant.utils.CurrentDateTimeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;
    private final CurrentDateTimeUtil currentDateTimeUtil;
    private final UserRepo userRepo;
    private final CurrentUserDetailService currentUserDetailService;

    public TodoController(TodoService todoService, CurrentDateTimeUtil currentDateTimeUtil, UserRepo userRepo, CurrentUserDetailService currentUserDetailService) {
        this.todoService = todoService;
        this.currentDateTimeUtil = currentDateTimeUtil;
        this.userRepo = userRepo;
        this.currentUserDetailService = currentUserDetailService;
    }


    @GetMapping("/home")
    public String todoHome(Model model) {
        //all today's task
        List<TodoDto> todayAndActiveTask = todoService.findTodayAndActiveTask(currentUserId());
        List<TodoDto> completedTasks = todoService.findCompletedTasks(currentUserId());
        model.addAttribute("currentDate", currentDateTimeUtil.getDayAndMonth());
        model.addAttribute("completedTask", completedTasks);
        model.addAttribute("todayTask", todayAndActiveTask);
        return "todo/todo_landing";
    }

    @GetMapping("add")
    public String addTask(Model model) {
        model.addAttribute("todoDto", new TodoDto());
        return "todo/add_task";
    }

    //creating new task
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute TodoDto todoDto, BindingResult bindingResult,
                         RedirectAttributes attributes,
                         Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("todoDto", todoDto);
            model.addAttribute("message", "Task addition failed!!");
            return "todo/add_task";
        }
//        User userByEmail = userRepo.getUserByEmail(principal.getName());
        //always active while creating task
        todoDto.setUser(currentUserDetailService.getCurrentUser());
        todoDto.setTaskStatus(TaskStatus.ACTIVE);
        todoDto.setImportantStatus(ImportantStatus.NOT_IMPORTANT);
        todoDto.setStartingDate(new Date());
        ResponseDto save = todoService.save(todoDto);
        attributes.addFlashAttribute("message", save.getMessage());
        return "redirect:/todo/add";
    }

    //mark as completed
    @GetMapping("/completed/{id}")
    public String completed(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        TodoDto todoDto = todoService.findById(id);
        todoDto.setTaskStatus(TaskStatus.COMPLETED);
        todoService.save(todoDto);
        return "redirect:/todo/home";
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
        List<TodoDto> allActiveTask = todoService.findAllActiveTask(currentUserId());
        model.addAttribute("heading", "All Task");
        model.addAttribute("allTask", allActiveTask);
        return "todo/todo_full_detail";
    }

    @GetMapping("/all-today")
    public String allTodayTask(Model model) {
        List<TodoDto> todayAndActiveTask = todoService.findTodayAndActiveTask(currentUserId());
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
        List<TodoDto> importantTask = todoService.findImportantTask(currentUserId());
        //reversing list to find the latest important commit task
        Collections.reverse(importantTask);
        model.addAttribute("heading", "Important Task");
        model.addAttribute("allTask", importantTask);
        return "todo/todo_full_detail";
    }

    //Getting the current user id
    public Integer currentUserId() {
        return currentUserDetailService.getCurrentUserId();
    }
}
