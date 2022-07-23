package com.mazniy.project2.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "person")
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Pattern(regexp = "[А-Я][а-я]* [А-Я][а-я]* [А-Я][а-я]*"
    			, message = "ФИО должно быть в формате: Фамилия Имя Отчество")
    @Column(name = "fullname")
    private String fullName;

    @Min(value = 1900, message = "Год рождения должен быть больше 1900")
    @Column(name = "year")
    private int year;
    
    @OneToMany(mappedBy = "person",fetch = FetchType.LAZY)
    private List<Book> books;

    public Person() {

    }

    public Person(int id, String fullName, int year) {
        this.id = id;
        this.fullName = fullName;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
    
    

}
