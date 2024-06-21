package library.persistence.collectionbased;

import library.model.Book;
import library.persistence.BookDAO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookDAOImpl implements BookDAO {
    private final Map<UUID, Book> books = new HashMap<>();

    @Override
    public void saveBook(Book book) {
        books.put(book.getId(), book);
    }

    @Override
    public Collection<Book> getAllBooks() {
        return books.values();
    }

    @Override
    public Book findBookById(UUID id) {
        return books.get(id);
    }
}
