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
        Routes.configure(this)
        ;
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

        Book book1 = new Book("Hamlet", "William Shakespeare", LocalDate.of(2024, 6, 24), BookCategory.FICTIONAL);
        Book book2 = new Book("Romeo & Juliet", "William Shakespeare", LocalDate.of(2024, 6, 24), BookCategory.FICTIONAL);
        Book book3 = new Book("Holly", "Stephen King", LocalDate.of(2024, 6, 24), BookCategory.FICTIONAL);
        Book book4 = new Book("Misery", "Stephen King", LocalDate.of(2024, 6, 24), BookCategory.FICTIONAL);
        Book book5 = new Book("Halowe'en Party", "Agatha Christie", LocalDate.of(2024, 6, 24), BookCategory.FICTIONAL);

        Book book6 = new Book("Becoming", "Michelle Obama", LocalDate.of(2024, 6, 24), BookCategory.NON_FICTIONAL);
        Book book7 = new Book("The Light We Carry", "Michelle Obama", LocalDate.of(2024, 6, 24), BookCategory.NON_FICTIONAL);
        Book book8 = new Book("A Brief History Of Time", "Stephen Hawking", LocalDate.of(2024, 6, 24), BookCategory.NON_FICTIONAL);
        Book book9 = new Book("The Grand Design", "Stephen Hawking.", LocalDate.of(2024, 6, 24), BookCategory.NON_FICTIONAL);
        Book book10 = new Book("I Know Why The Caged Bird", "Maya Angelou", LocalDate.of(2024, 6, 24), BookCategory.NON_FICTIONAL);

        Stream.of(book1, book2, book3,book4,book5,book6,book7,book8,book9,book10).forEach(bookDAO::saveBook);
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
}
