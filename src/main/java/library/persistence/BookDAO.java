package library.persistence;

import library.model.Book;

import java.util.Collection;
import java.util.UUID;

public interface BookDAO {
    void saveBook(Book book);
    Collection<Book> getAllBooks();
    Book findBookById(UUID id);
}
