package com.evan.wj.server.impl;

import com.evan.wj.mapper.BookMapper;
import com.evan.wj.pojo.Book;
import com.evan.wj.server.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public void addOrUpdateBook(Book book) {
        bookMapper.addOrUpdateBook(book);
    }
}
