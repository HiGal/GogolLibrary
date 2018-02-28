package com.project.glib.dao;

import com.project.glib.model.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.logging.Logger;

@Repository
public class BookDaoImplementation implements BookDao {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addBook(Book book) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(book);
        logger.info("Book successfully saved. Book details : " + book);
    }

    @Override
    public void updateBook(Book book) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(book);
        logger.info("Book successfully update. Book details : " + book);
    }

    @Override
    public void removeBook(int bookId) {
        Session session = this.sessionFactory.getCurrentSession();
        Book book = (Book) session.load(Book.class, bookId);

        if (book != null){
            session.delete(book);
            logger.info("Book successfully removed. Book details : " + book);
        } else {
            logger.info("Try to remove not-existed book");
        }
    }

    @Override
    public Book getBookById(int bookId) {
        Session session = this.sessionFactory.getCurrentSession();
        Book book = (Book) session.load(Book.class, bookId);
        logger.info("Book successfully loaded. Book details : " + book);
        return book;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Book> listBooks() {
        Session session = sessionFactory.getCurrentSession();
        List<Book> bookList = session.createQuery("from Book").list();

        for (Book book : bookList){
            logger.info("Book list : " + book);
        }

        return bookList;
    }
}
