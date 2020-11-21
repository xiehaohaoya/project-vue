package com.evan.wj.controller;

import com.evan.wj.pojo.Book;
import com.evan.wj.result.Result;
import com.evan.wj.server.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    @CrossOrigin
    @ResponseBody
    @PostMapping(value = "/api/addOrUpdateBook")
    public Result addOrUpdateBook(@RequestBody Book book) {
        bookService.addOrUpdateBook(book);
        System.out.println("asdfadsfasdf");
        log.info("dafasdfasdasd");
        return new Result(200);
    }
}
