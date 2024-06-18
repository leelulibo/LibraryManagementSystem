package org.example;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.eclipse.jetty.server.session.DefaultSessionCache;
import org.eclipse.jetty.server.session.NullSessionDataStore;
import org.eclipse.jetty.server.session.SessionCache;
import org.eclipse.jetty.server.session.SessionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import library.model.Book;
import library.model.BookCategory;
import library.persistence.BookDAO;
import library.persistence.UserDAO;
import library.persistence.collectionbased.BookDAOImpl;
import library.persistence.collectionbased.UserDAOImpl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Server {
    public static final String SESSION_USER_KEY = "User";
    private static final String STATIC_FILES_DIR = "/templates/";

    private final Javalin appServer;

    public Server() {
        JavalinThymeleaf.configure(templateEngine());
        appServer = Javalin.create(config -> {
            config.addStaticFiles(STATIC_FILES_DIR, Location.CLASSPATH);
//            config.accessManager(accessManager());
            config.sessionHandler(sessionHandler());
        });

        ServiceRegistry.configure(UserDAO.class, new UserDAOImpl());
        ServiceRegistry.configure(BookDAO.class, new BookDAOImpl());
        Routes.configure(this);
        configureExceptionsPage();
    }

    public static void main(String[] args) {
        Server server = new Server();
        seedDemoData();
        server.start(5052);
    }

    @Nullable
    public static User getUserLoggedIn(Context context) {
        return context.sessionAttribute(SESSION_USER_KEY);
    }

    private static Supplier<SessionHandler> sessionHandler() {
        SessionHandler sessionHandler = new SessionHandler();
        SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
        sessionCache.setSessionDataStore(new NullSessionDataStore());
        sessionHandler.setSessionCache(sessionCache);
        sessionHandler.setHttpOnly(true);
        return () -> sessionHandler;
    }

    private static void seedDemoData() {
        UserDAO userDAO = ServiceRegistry.lookup(UserDAO.class);
        BookDAO bookDAO = ServiceRegistry.lookup(BookDAO.class);

        User user1 = new User("user1@example.com", "User One");
        User user2 = new User("user2@example.com", "User Two");
        Stream.of(user1, user2).forEach(userDAO::saveUser);

        Book book1 = new Book("Fictional Book 1", "Author A", LocalDate.of(2020, 1, 1), BookCategory.FICTIONAL);
        Book book2 = new Book("Non-Fictional Book 1", "Author B", LocalDate.of(2018, 5, 20), BookCategory.NON_FICTIONAL);
        Stream.of(book1, book2).forEach(bookDAO::saveBook);
    }

    private Javalin configureExceptionsPage() {
        return appServer.exception(Exception.class, (e, context) -> {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTrace = sw.toString().replace(System.getProperty("line.separator"), "<br/>\n");
            context.render("exception.html",
                    Map.of("exception", e,
                            "stacktrace", stackTrace));
        });
    }

    public void routes(EndpointGroup group) {
        appServer.routes(group);
    }

    public void start(int port) {
        this.appServer.start(port);
    }

    public void stop() {
        this.appServer.stop();
    }

    public int port() {
        return appServer.port();
    }

    private AccessManager accessManager() {
        return new AccessManager() {
            @Override
            public void manage(@NotNull Handler handler, @NotNull Context context, @NotNull Set<RouteRole> set) throws Exception {
                if (hasNoSession(context)) {
                    context.redirect(Routes.LOGIN_PAGE);
                } else {
                    handler.handle(context);
                }
            }

            private boolean hasNoSession(@NotNull Context context) {
                User loggedInUser = context.sessionAttribute(SESSION_USER_KEY);
                return Objects.isNull(loggedInUser) && !context.path().equals(Routes.LOGIN_ACTION);
            }
        };
    }

    private TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix(STATIC_FILES_DIR); // Adjusted to match the static files directory
        templateEngine.setTemplateResolver(resolver);
        templateEngine.addDialect(new LayoutDialect());
        return templateEngine;
    }

    public static class Routes {
        public static final String LOGIN_PAGE = "/login";
        public static final String LOGIN_ACTION = "/login-action";
        public static final String REGISTER_ACTION = "/register-action";
        public static final String BOOKS = "/books";
        public static final String REGISTER = "/register";


        public static void configure(Server server) {
            server.routes(() -> {
                io.javalin.apibuilder.ApiBuilder.get(BOOKS, Routes::view);
                io.javalin.apibuilder.ApiBuilder.post("/books/borrow", Routes::borrowBook);
                io.javalin.apibuilder.ApiBuilder.post("/books/return/{id}", Routes::returnBook);
                io.javalin.apibuilder.ApiBuilder.get(LOGIN_PAGE, Routes::showLoginPage);
                io.javalin.apibuilder.ApiBuilder.post(LOGIN_ACTION, Routes::login);
                io.javalin.apibuilder.ApiBuilder.post(REGISTER_ACTION, Routes::register);
                io.javalin.apibuilder.ApiBuilder.get(REGISTER, Routes::showRegister);


            });
        }

        private static void showRegister(Context context) {
            context.render("register.html");
        }

        private static void view(Context ctx) {
            BookDAO bookDAO = ServiceRegistry.lookup(BookDAO.class);
            Map<String, Object> viewModel = Map.of(
                    "books", bookDAO.getAllBooks()
            );
            ctx.render("books.html", viewModel);

        }

        private static void borrowBook(Context ctx) {
            BookDAO bookDAO = ServiceRegistry.lookup(BookDAO.class);
            UserDAO userDAO = ServiceRegistry.lookup(UserDAO.class);

            UUID bookId = UUID.fromString(ctx.formParam("bookId"));
            String userEmail = ctx.formParam("userEmail");

            Book book = bookDAO.findBookById(bookId);
            User user = userDAO.findUserByEmail(userEmail).orElse(null);

            if (book.isAvailable()) {
                book.borrowBook(user, LocalDate.now(), LocalDate.now().plusWeeks(2));
                ctx.status(204);
            } else {
                ctx.status(400).result("Book is already borrowed.");
            }
        }

        private static void returnBook(Context ctx) {
            BookDAO bookDAO = ServiceRegistry.lookup(BookDAO.class);
            UserDAO userDAO = ServiceRegistry.lookup(UserDAO.class);

            UUID bookId = UUID.fromString(ctx.pathParam("id"));
            String userEmail = ctx.formParam("userEmail");

            Book book = bookDAO.findBookById(bookId);
            User user = userDAO.findUserByEmail(userEmail).orElse(null);

            book.returnBook(book.listOfBorrowRecords().stream()
                    .filter(br -> br.getUser().equals(user))
                    .findFirst()
                    .get().getId(), user, LocalDate.now());

            ctx.status(204);
        }

        private static void showLoginPage(Context ctx) {
            ctx.redirect("login.html");
        }

        private static void login(Context ctx) {
            UserDAO userDAO = ServiceRegistry.lookup(UserDAO.class);
            String email = ctx.formParam("email");
            User user = userDAO.findUserByEmail(email).orElse(null);

            if (user != null) {
                ctx.sessionAttribute(SESSION_USER_KEY, user);
                ctx.redirect(BOOKS); // Redirect to the books page after successful login
            } else {
                ctx.status(401).result("Invalid email.");
            }
        }

        private static void register(Context ctx) {
            String email = ctx.formParam("username");
            String password = ctx.formParam("password");
            String confirmPassword = ctx.formParam("confirmPassword");

            System.out.println("Email: " + email);
            System.out.println("Password: " + password);

            ctx.redirect("/login");
        }



    }
}
