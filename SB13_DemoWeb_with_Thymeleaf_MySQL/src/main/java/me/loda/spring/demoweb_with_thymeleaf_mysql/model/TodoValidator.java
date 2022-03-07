package me.loda.spring.demoweb_with_thymeleaf_mysql.model;

import org.thymeleaf.util.StringUtils;

import java.util.Optional;

/*
Đối tượng này dùng để kiểm tra xem một Object Todo có hợp lệ không
 */
public class TodoValidator {
    /**
     * Kiểm tra một object Todo có hợp lệ không
     * @param todo
     * @return
     */
    public boolean isValid(Todo todo) {
        return Optional.ofNullable(todo)
                .filter(t -> !StringUtils.isEmpty(t.getTitle()))
                .filter(t -> !StringUtils.isEmpty(t.getDetail()))
                .isPresent();
    }
}
