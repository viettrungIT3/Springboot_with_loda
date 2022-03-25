package me.loda.spring.sb15_exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Todo {
    private String title;
    private String detail;
}
