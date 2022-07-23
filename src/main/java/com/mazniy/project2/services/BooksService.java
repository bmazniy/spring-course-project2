package com.mazniy.project2.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mazniy.project2.models.Book;
import com.mazniy.project2.models.Person;
import com.mazniy.project2.repositories.BooksRepository;
import com.mazniy.project2.repositories.PeopleRepository;

@Service
@Transactional(readOnly = true)
public class BooksService {

	private final BooksRepository booksRepository;
	private final PeopleRepository peopleRepository;
	
	@Autowired
	public BooksService(BooksRepository booksRepository
			, PeopleRepository peopleRepository) {
		this.booksRepository = booksRepository;
		this.peopleRepository = peopleRepository;
	}

	public List<Book> findAll(){
		return booksRepository.findAll();
	}
	
	public List<Book> findAll(Pageable var1){
		Page<Book> page = booksRepository.findAll(var1);
		return page.toList();
	}
	
	public List<Book> findAll(Sort sort){
		return booksRepository.findAll(sort);
	}
	
	public Book findById(int id) {
		Optional<Book> bookOptional = booksRepository.findById(id);
		return bookOptional.orElse(null);
	}
	
	@Transactional
	public void save(Book book) {
		booksRepository.save(book);
	}
	
	@Transactional
	public void update(int id, Book updatedBook) {
		updatedBook.setId(id);
		booksRepository.save(updatedBook);
	}
	
	@Transactional
	public void delete(int id) {
		booksRepository.deleteById(id);
	}
	
	public Person showByBookId(int bookId) {
		
		Optional<Book> bookOptional = booksRepository.findById(bookId);
		
		if(!bookOptional.isEmpty()) {
			return bookOptional.get().getPerson();
		} else {
			return null;
		}
	     
	}
	
	public boolean bookHasTaken(int id) {
	
		Optional<Book> bookOptional = booksRepository.findById(id);
		
		if(bookOptional.isPresent()) {
			
			if(bookOptional.get().getPerson() == null) {
				
				return false;
				
			} else {
			
				return true;
				
			}
			
		} else {
		
			return false;
			
		}
		
	}
	
	@Transactional
	public void takeABook(int bookId, int personId) {
	
		Optional<Book> bookOptional = booksRepository.findById(bookId);
		Optional<Person> personOptional = peopleRepository.findById(personId);
		
		Book book = bookOptional.get();
		Person person = personOptional.get();
		
		book.setPerson(person);
		book.setTakeDate(new Date());
		
		if(person.getBooks() == null) {
			
			person.setBooks(new ArrayList<Book>(Arrays.asList(book)));
			
		} else {
			
			person.getBooks().add(book);
			
		}
		
		booksRepository.save(book);
		peopleRepository.save(person);
		
	}

	@Transactional
	public void release(int id) {
		
		Optional<Book> bookOptional = booksRepository.findById(id);
		Book book = bookOptional.get();
		
		Person person = book.getPerson();
		
		book.setPerson(null);
		book.setTakeDate(null);
		
		person.getBooks().remove(book);
		
		booksRepository.save(book);
		peopleRepository.save(person);		
		
	}
	
	public Optional<Book> findByTitleStartingWith(String startingText) {
		return booksRepository.findByTitleStartingWith(startingText);
	}
	
	
	
}
