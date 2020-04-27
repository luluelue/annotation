package com.example.resource;

import com.example.pojo.Result;
import com.example.pojo.Student;
import com.example.annotation.CheckWebParam;
import com.example.annotation.WebLog;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestResource {

    @WebLog(description = "返回一个学生")
    @CheckWebParam(notNull = {"aa", "bb"}, checkToken = true)
    @GetMapping(value = "/get")
    public String getStudent() {
        Student student = new Student("张三", 20, "男");
        Result result = new Result(student);
        return new Gson().toJson(result);
    }

}
