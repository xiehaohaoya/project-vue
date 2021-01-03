package com.evan.wj.controller;

import com.evan.wj.pojo.Book;
import com.evan.wj.result.Result;
import com.evan.wj.server.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@RestController = @Controller + @ResponseBody
@Controller
@Slf4j      //使用该注解，可以直接使用log.info("xxx")打印日志
public class BookController {
    @Autowired      //注入bean
    private BookService bookService;

    @CrossOrigin        //允许跨域访问
    @ResponseBody       //返回json格式的http返回体
    @PostMapping(value = "/api/addOrUpdateBook")        //相当于@RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    public Result addOrUpdateBook(@RequestBody Book book) {
        bookService.addOrUpdateBook(book);
        System.out.println("GET BOOK");
        log.info("GET BOOK");
        return new Result(200);
    }
}
