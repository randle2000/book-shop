package com.sln.bshop.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sln.bshop.domain.Book;
import com.sln.bshop.service.BookService;
import com.sln.bshop.service.UserService;

@Controller
public class BookController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	BookService bookService;
	
	@RequestMapping("/bookshelf")
	public String bookshelf(Model model) {
		List<Book> bookList = bookService.findAllActive();
		model.addAttribute("bookList", bookList);
		model.addAttribute("activeAll",true);
		return "bookshelf";
	}
	
	@RequestMapping("/bookDetail")
	public String bookDetail(@RequestParam("id") Long id, Model model) {
		Book book = bookService.findOne(id);
		
		model.addAttribute("book", book);
		
		List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);
		
		return "bookDetail";
	}
	
	@RequestMapping("/searchByCategory")
	public String searchByCategory(@RequestParam("category") String category, Model model) {
		String classActiveCategory = "active"+category;
		classActiveCategory = classActiveCategory.replaceAll("\\s+", "");
		classActiveCategory = classActiveCategory.replaceAll("&", "");
		model.addAttribute(classActiveCategory, true);
		
		List<Book> bookList = bookService.findByCategory(category);
		if (bookList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return "bookshelf";
		}
		model.addAttribute("bookList", bookList);
		
		return "bookshelf";
	}
	
	@RequestMapping("/searchBook")
	public String searchBook(@RequestParam("keyword") String keyword, Model model) {
		List<Book> bookList = bookService.blurrySearch(keyword);
		if (bookList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return "bookshelf";
		}
		model.addAttribute("bookList", bookList);
		
		return "bookshelf";
	}
	


}
