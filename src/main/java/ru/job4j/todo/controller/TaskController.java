package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.SimpleTaskService;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    SimpleTaskService simpleTaskService;

    public TaskController(SimpleTaskService simpleTaskService) {
        this.simpleTaskService = simpleTaskService;
    }

    @GetMapping("/task")
    public String getAll(Model model) {
        model.addAttribute("tasks", simpleTaskService.findAll());
        model.addAttribute("task", new Task());
        return "tasks/task";
    }

    @GetMapping("/active")
    public String getPendingTasks(Model model) {
        model.addAttribute("tasks", simpleTaskService.findAllPendingTasks());
        return "tasks/active";
    }

    @GetMapping("/completed")
    public String getCompletedTasks(Model model) {
        model.addAttribute("tasks", simpleTaskService.findAllCompletedTasks());
        return "tasks/completed";
    }

    @PostMapping("/task/create")
    public String createNewTask(@ModelAttribute Task task, Model model) {
        simpleTaskService.save(task);
        return "redirect:/tasks/task";
    }

    @GetMapping("/task/{id}")
    public String getById(Model model, @PathVariable int id) {
        var taskOptional = simpleTaskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "������� � ��������� ��������������� �� �������");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/taskDescription";
    }

    @PostMapping("/task/update")
    public String update(@ModelAttribute Task task, Model model) {
        var isUpdated = simpleTaskService.update(task);
        if (!isUpdated) {
            model.addAttribute("message", "������� � ��������� ��������������� �� �������");
            return "errors/404";
        }
        return "redirect:/tasks/task";
    }

    @GetMapping("/task/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        simpleTaskService.deleteById(id);
        return "redirect:/tasks/task";
    }

    @GetMapping("/task/complete/{id}")
    public String completeTask(Model model, @ModelAttribute Task task) {
        simpleTaskService.completeTask(task);
        return "redirect:/tasks/task";
    }
}