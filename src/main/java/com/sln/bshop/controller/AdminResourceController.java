package com.sln.bshop.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sln.bshop.service.BookService;

// If you annotate your controller class with @RestController instead of @Controller, Spring applies message conversion
// to all handler methods in the controller. You don’t need to annotate each method with @ResponseBody
// (see page 431 in "Spring in Action 4th Edition" book)
//
// Alternatively, removeList() could return ResponseEntity<>, see page 437 in "Spring in Action 4th Edition" book
//
// Again, if the controller class is annotated with @RestController, you can remove the @ResponseBody annotation and clean up the code a little more
// see page 436 in "Spring in Action 4th Edition" book
@RestController
public class AdminResourceController {

	@Autowired
	private BookService bookService;
	
	@RequestMapping(value="/admin/removeList", method=RequestMethod.POST, consumes="application/json")
	@ResponseStatus(HttpStatus.OK)  // can add response status for client - see page 437 in "Spring in Action 4th Edition" book
	public String removeList(@RequestBody ArrayList<String> bookIdList, Model model) {

		for (String id : bookIdList) {
			String bookId = id.substring(8);
			bookService.removeOne(Long.parseLong(bookId));
		}
		
		return "delete success";
	}
}
