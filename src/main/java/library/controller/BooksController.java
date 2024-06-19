package library.controller;

import io.javalin.http.Handler;
import library.model.Book;
import library.persistence.BookDAO;

import library.persistence.UserDAO;
import org.example.Server;
import org.example.User;
import org.example.ServiceRegistry;
import org.thymeleaf.context.IContext;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class BooksController {


    public static final Handler viewBooks = context -> {
        BookDAO bookDAO = ServiceRegistry.lookup(BookDAO.class);

        Collection<Book> books = bookDAO.getAllBooks();
        Map<String, Object> viewModel = Map.of(
          "books",books
        );
        context.render("books.html", viewModel);
    };

    // TODO Implement borrow books
    public static Handler borrowBook = context -> {


        BookDAO bookDAO = ServiceRegistry.lookup(BookDAO.class);
        User personLoggedIn = Server.getUserLoggedIn(context);

        UserDAO userDAO = ServiceRegistry.lookup(UserDAO.class);
        String id = context.queryParam("bookId");
        System.out.println("borrowing book");

        UUID bookId = UUID.fromString(id);

//        String userEmail = context.formParam("userEmail");

        Book book = bookDAO.findBookById(bookId);
//        User user = userDAO.findUserByEmail(userEmail).orElse(null);
        Map<String, Object> viewModel = Map.of("book", book);
        context.render("borrow.html", viewModel);

//        if (book.isAvailable()) {
//            book.borrowBook(user, LocalDate.now(), LocalDate.now().plusWeeks(2));
//            context.status(204);
//        } else {
//            context.status(400).result("Book is already borrowed.");
//        }
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