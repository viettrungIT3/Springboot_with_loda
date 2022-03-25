package com.loda.spring.restful_api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/v1")
public class RestApiController {

    private List<Todo> todoList = new CopyOnWriteArrayList<>();

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

    // Tạo mới
    @PostMapping("/todo")
    public ResponseEntity<?> addTodo(@RequestBody Todo todo) {
        todoList.add(todo);

        return ResponseEntity.ok().body(todo);
    }

    // Sửa
    @PutMapping("/todo/{id}")
    public Todo editTodoByID(@PathVariable(name = "id") Integer id,
                         @RequestBody Todo todo) {
        todoList.set(id, todo);

        return todo;
    }

    // Xóa
    @DeleteMapping("/todo/{id}")
    public ResponseEntity<?> deleteTodoByID(@PathVariable(name = "id") Integer id) {
        todoList.remove(id.intValue());

        return ResponseEntity.status(200).build();
    }
}
