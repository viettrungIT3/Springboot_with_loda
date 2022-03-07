package me.loda.spring.demoweb_with_thymeleaf_mysql.service;

import me.loda.spring.demoweb_with_thymeleaf_mysql.model.Todo;
import me.loda.spring.demoweb_with_thymeleaf_mysql.model.TodoValidator;
import me.loda.spring.demoweb_with_thymeleaf_mysql.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoValidator todoValidator;

    /**
     * Lấy ra danh sách List<Todo>
     * @param limit - Giới hạn số lượng lấy ra
     * @return Trả về danh sách List<Todo> dựa theo limit, nếu limit == null thì trả findAll()
     */
    public List<Todo> findAll(Integer limit) {
        return Optional.ofNullable(limit)
                .map(value -> todoRepository.findAll(PageRequest.of(0, value)).getContent())
                .orElseGet(() -> todoRepository.findAll());
    }

    /**
     * Thêm một Todo mới vào danh sách công việc cần làm
     * @return Trả về đối tượng Todo sau khi lưu vào DB, trả về null nếu không thành công
     */
    public Todo add(Todo todo) {
        if (todoValidator.isValid(todo)) {
            return todoRepository.save(todo);
        }
        return null;
    }
}
