package com.sln.bshop.service;

import java.util.List;

import com.sln.bshop.domain.Book;

public interface BookService {
	List<Book> findAll();
	
	List<Book> findAllActive();
	
	Book findOne(Long id);
	
	List<Book> findByCategory(String category);
	
	List<Book> blurrySearch(String title);
	
	void removeOne(Long id);
	
	Book save(Book book);
}
