package com.mazniy.project2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mazniy.project2.models.Book;
import com.mazniy.project2.models.Person;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
	
	Page<Book> findAll(Pageable var1);
	
	List<Book> findAll(Sort sort);
	
	Optional<Book> findByTitleStartingWith(String text);
}
