package com.project.glib.dao.interfaces;

import com.project.glib.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource()
public interface BookRepository extends JpaRepository<Book, Long> {
//    List<Book> getBooksByBookAuthor(String author);
}
