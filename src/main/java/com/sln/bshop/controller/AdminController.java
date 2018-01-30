package com.sln.bshop.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sln.bshop.domain.Book;
import com.sln.bshop.service.BookService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	BookService bookService;
	
	@RequestMapping({"/", ""})
	public String index(Model model){
		/*
		// Print all files/folders from current dir
		// Files.walk API is available from Java 8
		try (Stream<Path> paths = Files.walk(Paths.get("."))) {
		    paths
		        .filter(Files::isRegularFile)
		        .forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		*/
		
		List<Book> bookList = bookService.findAll();
		model.addAttribute("bookList", bookList);		
		return "admin/bookList";
	}
	
	@RequestMapping(value="/remove", method=RequestMethod.POST)
	public String remove(@RequestParam("id") String id, Model model) {
		bookService.removeOne(Long.parseLong(id.substring(8)));
		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addBook(Model model) {
		Book book = new Book();
		model.addAttribute("book", book);
		return "admin/addBook";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addBookPost(@ModelAttribute("book") Book book, HttpServletRequest request) {
		bookService.save(book);

		MultipartFile bookImage = book.getBookImage();

		try {
			byte[] bytes = bookImage.getBytes();
			String name = book.getId() + ".png";
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/image/book/" + name)));
			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/admin";
	}

	@RequestMapping("/bookInfo")
	public String bookInfo(@RequestParam("id") Long id, Model model) {
		Book book = bookService.findOne(id);
		model.addAttribute("book", book);
		
		return "admin/bookInfo";
	}
	
	@RequestMapping("/updateBook")
	public String updateBook(@RequestParam("id") Long id, Model model) {
		Book book = bookService.findOne(id);
		model.addAttribute("book", book);
		
		return "admin/addBook";
	}
	
	@RequestMapping(value="/updateBook", method=RequestMethod.POST)
	public String updateBookPost(@ModelAttribute("book") Book book, HttpServletRequest request) {
		
		bookService.save(book);
		
		MultipartFile bookImage = book.getBookImage();
		
		if(!bookImage.isEmpty()) {
			try {
				byte[] bytes = bookImage.getBytes();
				//String name = "src/main/resources/static/image/book/" + book.getId() + ".png";
				String name = "target/classes/static/image/book/" + book.getId() + ".png";

				Files.delete(Paths.get(name));
				
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(name)));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return "redirect:/admin/bookInfo?id="+book.getId();
	}

	

}
