package com.ajs.service.impl;

import com.ajs.bo.Book;
import com.ajs.dao.JdbcTemplateDao;
import com.ajs.service.BookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("bookService")
public class BookServiceImpl implements BookService {

    @Resource
    private JdbcTemplateDao jdbcTemplateDao;

    @Override
    public List<Book> findAllBook() {
        String sql = "select * from tb_book";
        return jdbcTemplateDao.queryForList(sql,Book.class);
    }

}
