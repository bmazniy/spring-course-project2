package com.mazniy.project2.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mazniy.project2.models.Book;
import com.mazniy.project2.models.Person;
import com.mazniy.project2.repositories.PeopleRepository;

@Service
@Transactional(readOnly = true)
public class PeopleService {
	
	private final PeopleRepository peopleRepository;
	
	@Autowired
	public PeopleService(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}
	
	public List<Person> findAll(){
		return peopleRepository.findAll();
	}
	
	public Person findById(int id) {
		Optional<Person> person = peopleRepository.findById(id);
		return person.orElse(null);
	}
	
	@Transactional
	public void save(Person person) {
		peopleRepository.save(person);		
	}
	
	@Transactional
	public void update(int id, Person updatedPerson) {
		updatedPerson.setId(id);
		peopleRepository.save(updatedPerson);
	}
	
	@Transactional
	public void delete(int id) {
		peopleRepository.deleteById(id);
	}
	
	public boolean personTakeABook(int id) {
		Optional<Person> personOptional = peopleRepository.findById(id);
		if(!personOptional.isEmpty()) {
		
			List<Book> books = personOptional.get().getBooks();
			
			if (books == null | books.size() == 0) {
				
				return false;
				
			} else {
			
				return true;
				
			}
			
		} else {
		
			return false;
			
		}
	}
	
	public List<Book> getPersonBook(int id){
	
		Optional<Person> personOptional = peopleRepository.findById(id);
		
		if(personOptional.isPresent()) {
			
			Hibernate.initialize(personOptional.get().getBooks());
			return personOptional.get().getBooks();
			
		} else {
			
			return Collections.emptyList();
			
		}
				
	}

}
