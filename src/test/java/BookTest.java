import library.model.Book;
import library.model.BookCategory;
import library.model.BorrowRecord;
import org.example.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    private Book book;
    private User testUser;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    @BeforeEach
    void setUp() {
        book = new Book("Test Book", "Test Author", LocalDate.of(2024, 6, 20), BookCategory.FICTIONAL);
        testUser = new User("test@example.com", "Test User");
        borrowDate = LocalDate.of(2024, 6, 20);
        dueDate = LocalDate.of(2024, 6, 27);
    }



    @Test
    void testListofBorrowRecords() {
        book.borrowBook(testUser, borrowDate, dueDate);
        Collection<BorrowRecord> records = book.listOfBorrowRecords();
        assertEquals(1, records.size());
    }

    @Test
    void testIsAvailable() {
        assertTrue(book.isAvailable());
        book.borrowBook(testUser, borrowDate, dueDate);
        assertFalse(book.isAvailable());
    }

    @Test
    void testReturnBook() {
        BorrowRecord borrowRecord = book.borrowBook(testUser, borrowDate, dueDate);
        UUID borrowRecordId = borrowRecord.getId();
        LocalDate returnDate = LocalDate.of(2024, 6, 25);

        book.returnBook(borrowRecordId, testUser, returnDate);
        assertTrue(book.isAvailable());
    }

    @Test
    void testGetCurrentBorrower() {
        assertNull(book.getCurrentBorrower());
        book.borrowBook(testUser, borrowDate, dueDate);
        assertEquals(testUser, book.getCurrentBorrower());
    }



    @Test
    void testToString() {
        String expectedToString = "Book{title='Test Book', author='Test Author', publicationDate=2024-06-20, category=FICTIONAL, id=" + book.getId() + "}";
        assertNotEquals(expectedToString, book.toString());
    }
}
