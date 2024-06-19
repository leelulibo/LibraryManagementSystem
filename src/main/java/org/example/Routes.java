package org.example;

import io.javalin.apibuilder.ApiBuilder;
import library.controller.AdminController;
import library.controller.BooksController;
import library.controller.BorrowRecordController;
import library.controller.UserController;

import static library.controller.BorrowRecordController.viewRecords;

public class Routes {
    public static final String LOGIN_ACTION = "/login-action";
    public static final String LOGIN_PAGE = "/";
    public static final String LOGIN = "/login";
    public static final String LOGOUT_ACTION = "/logout";
    public static final String BOOKS = "/books";
    public static final String BORROW_BOOK = "/book/borrow";
    public static final String RETURN_BOOKS = "/books/return/{id}";

    public static final String REGISTER = "/register";
    public static final String REGISTER_ACTION = "/register-action";

    public static final String ADMIN = "/admin";

    public static void configure(Server server) {
        server.routes(() -> {
            ApiBuilder.post(LOGIN_ACTION, UserController.login);
            ApiBuilder.get(LOGIN, UserController.viewLogin);
            ApiBuilder.post(LOGOUT_ACTION, UserController.logout);
            ApiBuilder.post(REGISTER, UserController.viewRegister);
            ApiBuilder.post(REGISTER_ACTION, UserController.register);
            ApiBuilder.get(BOOKS, BooksController.viewBooks);
            ApiBuilder.get(BORROW_BOOK, BooksController.borrowBook);
            ApiBuilder.post(BORROW_BOOK, BooksController.borrowBook);
            ApiBuilder.post(RETURN_BOOKS, BooksController.returnBook);
            ApiBuilder.get(ADMIN, AdminController.viewAdminLogin);
//            ApiBuilder.get(BORROW_BOOK,BooksController.borrowBook);
//            ApiBuilder.get(BORROW_BOOK, BorrowRecordController.borrowBook);
//            ApiBuilder.post(BORROW_BOOKS, BorrowRecordController.borrowBook);

        });
    }
}
