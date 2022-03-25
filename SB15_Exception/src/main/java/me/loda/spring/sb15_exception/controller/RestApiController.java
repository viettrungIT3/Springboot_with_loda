package me.loda.spring.sb15_exception.controller;


import me.loda.spring.sb15_exception.model.Todo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("api/v1")
public class RestApiController {

    private List<Todo> todoList;

    @PostConstruct
    public void init() {
        todoList = IntStream.range(0, 10)
                .mapToObj(i -> new Todo("title-" + i, "detail-" + i))
                .collect(Collectors.toList());
    }

    // hiển thị all
    @GetMapping("/todo")
    public List<Todo> getTodoList() {
        return  todoList;
    }

    // hiển thị theo id
    @GetMapping("/todo/{id}")
    public Todo getTodoByID(@PathVariable(name = "id") Integer id) {
        return todoList.get(id);
    }
}
