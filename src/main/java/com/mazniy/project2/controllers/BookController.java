package com.mazniy.project2.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mazniy.project2.models.Book;
import com.mazniy.project2.models.Person;
import com.mazniy.project2.repositories.BooksRepository;
import com.mazniy.project2.services.BooksService;
import com.mazniy.project2.services.PeopleService;

@Controller
@RequestMapping("/books")
public class BookController {
	
	private final BooksService booksService;
	private final PeopleService peopleService;

	@Autowired
	public BookController(BooksService booksService
			, PeopleService peopleService) {
		this.booksService = booksService;
		this.peopleService = peopleService;
	}
	
	
	@GetMapping()
	public String index(Model model) {
		
		model.addAttribute("books", booksService.findAll());
		 
		return "books/index";
	}
	
	@GetMapping(params = {"page", "books_per_page"})
	public String index(Model model
			, @RequestParam(name = "page", required = false) Optional<Integer> page
			, @RequestParam(name = "books_per_page", required = false) Optional<Integer> books_per_page) {
		
		model.addAttribute("books", booksService.findAll(PageRequest.of(page.get(), books_per_page.get())));
		 
		return "books/index";
	}
	
	@GetMapping(params = {"sort_by_year"})
	public String index(Model model
			, @RequestParam(name = "sort_by_year", required = false) Optional<Boolean> sort_by_year) {
		
		if(sort_by_year.get() == true) {
			model.addAttribute("books", booksService.findAll(Sort.by("year")));
		} else {
			model.addAttribute("books", booksService.findAll());
		}
			
		return "books/index";
	}
	
	@GetMapping(params = {"page", "books_per_page", "sort_by_year"})
	public String index(Model model
			, @RequestParam(name = "page", required = false) Optional<Integer> page
			, @RequestParam(name = "books_per_page", required = false) Optional<Integer> books_per_page
			, @RequestParam(name = "sort_by_year", required = false) Optional<Boolean> sort_by_year) {
		
		if(sort_by_year.get() == true) {
			model.addAttribute("books"
					, booksService.findAll(PageRequest.of(page.get()
									, books_per_page.get(), Sort.by("year"))));
		} else {
			model.addAttribute("books", booksService.findAll());
		}
		 
		return "books/index";
	}
	
	@GetMapping("/search")
	public String search(Model model) {
		
		model.addAttribute("searching", false);
		return "books/search";
	}
	
	@GetMapping(value = "/search", params = {"search_text"})
	public String search(Model model
			,@RequestParam(name = "search_text") Optional<String> search_text) {
		
		model.addAttribute("searching", true);
		
		Optional<Book> book = booksService.findByTitleStartingWith(search_text.get());
		
		if(book.isPresent()) {
			model.addAttribute("bookIsFound", true);
			model.addAttribute("book", book.get());
			
			if(book.get().getPerson() == null) {
				model.addAttribute("isFree", true);
			} else {
				model.addAttribute("isFree", false);
				model.addAttribute("person", book.get().getPerson().getFullName());
			}
			
		} else {
			model.addAttribute("bookIsFound", false);
		}
		
		return "books/search";
	}
	
	@GetMapping("/new")
	public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
	}
	
	@GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findById(id));
        model.addAttribute("bookTaken", booksService.bookHasTaken(id));
        model.addAttribute("person", booksService.showByBookId(id));
        model.addAttribute("people", peopleService.findAll());
        model.addAttribute("chPerson", new Person());
        return "books/show";
    }
	
	@GetMapping("/{id}/edit")
	public String newBook(@PathVariable("id") int id, Model model) {
		model.addAttribute("book", booksService.findById(id));
		return "books/edit";
	}
	
	@PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        booksService.save(book);
        return "redirect:/books";
    }
	
	@PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);
        return "redirect:/books";
    }
	
	@DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
		booksService.delete(id);
        return "redirect:/books";
    }
	
	@PostMapping("/release/{id}")
    public String release(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult
    		, @PathVariable("id") int id) {
        
		booksService.release(id);
        return "redirect:/books/" + id;
    }
	
	@PatchMapping("/takeABook/{id}")
    public String takeABook(@ModelAttribute("chPerson") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        
		booksService.takeABook(id, person.getId());
        return "redirect:/books/" + id;
    }

}
