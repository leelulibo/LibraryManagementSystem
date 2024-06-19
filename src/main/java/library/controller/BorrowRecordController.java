package library.controller;

import io.javalin.http.Handler;
import library.model.Book;
import library.model.BorrowRecord;
import library.persistence.BookDAO;
import library.persistence.BookRecordDAO;
import library.persistence.UserDAO;
import org.example.ServiceRegistry;
import org.example.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;


public class BorrowRecordController {



    public static final Handler viewRecords = context -> {
        BookRecordDAO bookRecordDAO = ServiceRegistry.lookup(BookRecordDAO.class);

        Collection<BorrowRecord> records = bookRecordDAO.getAllBorrowedRecords();
        Map<String, Object> viewModel = Map.of(
                "records",records
        );
        context.render("booksHistory.html", viewModel);
    };

    // TODO Implement borrow books
    public static Handler borrowBook = context -> {
        BookDAO bookDAO = ServiceRegistry.lookup(BookDAO.class);
        UserDAO userDAO = ServiceRegistry.lookup(UserDAO.class);

        UUID bookId = UUID.fromString(context.formParam("bookId"));
        String userEmail = context.formParam("userEmail");

        Book book = bookDAO.findBookById(bookId);
        User user = userDAO.findUserByEmail(userEmail).orElse(null);



        if (book.isAvailable()) {
            book.borrowBook(user, LocalDate.now(), LocalDate.now().plusWeeks(2));
            context.status(204);
            BookRecordDAO bookRecordDAO = ServiceRegistry.lookup(BookRecordDAO.class);

            Collection<BorrowRecord> records = bookRecordDAO.getAllBorrowedRecords();
            Map<String, Object> viewModel = Map.of(
                    "records",records
            );
            context.render("booksHistory.html", viewModel);

        } else {
            context.status(400).result("Book is already borrowed.");
        }
    };

    // TODO Implement return books
    public static Handler returnBook = context -> {
        // IMPLEMENT ME
        BookDAO bookDAO = ServiceRegistry.lookup(BookDAO.class);
        UserDAO userDAO = ServiceRegistry.lookup(UserDAO.class);

        UUID bookId = UUID.fromString(context.pathParam("id"));
        String userEmail = context.formParam("userEmail");

        Book book = bookDAO.findBookById(bookId);
        User user = userDAO.findUserByEmail(userEmail).orElse(null);

        book.returnBook(book.listOfBorrowRecords().stream()
                .filter(br -> br.getUser().equals(user))
                .findFirst()
                .get().getId(), user, LocalDate.now());

        context.status(204);

    };

}

