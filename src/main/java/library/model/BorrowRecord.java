package library.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.example.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class BorrowRecord {
    private final Book book;
    private final User user;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private final UUID id;

    public BorrowRecord(Book book, User user, LocalDate borrowDate, LocalDate dueDate) {
        if (dueDate.isBefore(borrowDate)) throw new LibraryException("Due date cannot be before borrow date");
        this.book = book;
        this.user = user;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.id = UUID.randomUUID();
        this.returnDate = null;
    }

    public void returnBook(User user, LocalDate returnDate) {
        if (!this.user.equals(user)) throw new LibraryException("User mismatch");
        this.returnDate = returnDate;
    }

    public boolean isNotReturned() {
        return returnDate == null;
    }

    public long daysLeftToReturn() {
        return returnDate == null ? ChronoUnit.DAYS.between(LocalDate.now(), dueDate) : 0;
    }

    public User getUser() {
        return user;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowRecord that = (BorrowRecord) o;
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("book", book)
                .add("user", user)
                .add("borrowDate", borrowDate)
                .add("dueDate", dueDate)
                .add("returnDate", returnDate)
                .add("id", id)
                .toString();
    }
}
