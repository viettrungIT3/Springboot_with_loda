package me.loda.spring.demoweb_with_thymeleaf_mysql.controller;

import me.loda.spring.demoweb_with_thymeleaf_mysql.model.Todo;
import me.loda.spring.demoweb_with_thymeleaf_mysql.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class TodoController {

    @Autowired
    private TodoService todoService;

    /*
    @RequestParam dùng để đánh dấu một biến là request param trong request gửi lên server.
    Nó sẽ gán dữ liệu của param-name tương ứng vào biến
     */
    @GetMapping("/listTodo")
    public String listToDo(Model model, @RequestParam(value = "limit", required = false) Integer limit) {

        // Trả về đối tượng todoList
        model.addAttribute("todoList", todoService.findAll(limit));

        return "listTodo";
    }

    @GetMapping("/addTodo")
    public String addTodo(Model model) {
         model.addAttribute("todo", new Todo());
         return "addTodo";
    }

    @PostMapping("/addTodo")
    public String addTodo(@ModelAttribute Todo todo) {
        return Optional.ofNullable(todoService.add(todo))
                .map(t -> "success")    // Trả vể success nếu thành công
                .orElse("failed");   // Trả vè failed nếu không thành công
    }
}
