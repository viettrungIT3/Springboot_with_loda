package me.loda.spring.demoweb_with_thymeleaf_mysql.repository;

import me.loda.spring.demoweb_with_thymeleaf_mysql.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
