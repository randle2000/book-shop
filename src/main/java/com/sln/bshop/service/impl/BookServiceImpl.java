package com.sln.bshop.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sln.bshop.domain.Book;
import com.sln.bshop.repository.BookRepository;
import com.sln.bshop.service.BookService;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	public List<Book> findAll() {
		return (List<Book>) bookRepository.findAll();
	}

	public List<Book> findAllActive() {
		List<Book> bookList = (List<Book>) bookRepository.findAll();
		List<Book> activeBookList = bookList.stream()	// convert list to stream
			.filter(book -> book.isActive())     		// only include active books
			.collect(Collectors.toList()); 				// collect the output and convert streams to a List
		return activeBookList;
	}
	
	public Book findOne(Long id) {
		return bookRepository.findOne(id);
	}

	public List<Book> findByCategory(String category){
		List<Book> bookList = bookRepository.findByCategory(category);
		List<Book> activeBookList = bookList.stream()	// convert list to stream
				.filter(book -> book.isActive())     		// only include active books
				.collect(Collectors.toList()); 				// collect the output and convert streams to a List
		return activeBookList;
	}
	
	public List<Book> blurrySearch(String title) {
		List<Book> bookList = bookRepository.findByTitleContaining(title);
		List<Book> activeBookList = bookList.stream()	// convert list to stream
				.filter(book -> book.isActive())     		// only include active books
				.collect(Collectors.toList()); 				// collect the output and convert streams to a List
		return activeBookList;
	}
	
	public void removeOne(Long id) {
		bookRepository.delete(id);
	}
	
	public Book save(Book book) {
		return bookRepository.save(book);
	}

}
