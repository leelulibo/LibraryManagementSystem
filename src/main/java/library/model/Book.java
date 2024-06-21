package library.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import org.example.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class Book {
    private final String title;
    private final String author;
    private final LocalDate publicationDate;
    private final UUID id;
    private final BookCategory category;
    private final HashMap<UUID, BorrowRecord> borrowRecords;

    public Book(String title, String author, LocalDate publicationDate, BookCategory category) {
        this.title = Strings.isNullOrEmpty(title) ? "Untitled" : title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.category = category;
        this.id = UUID.randomUUID();
        this.borrowRecords = new HashMap<>();
    }

    public BorrowRecord borrowBook(User user, LocalDate borrowDate, LocalDate dueDate) {
        BorrowRecord borrowRecord = new BorrowRecord(this, user, borrowDate, dueDate);
        borrowRecords.put(borrowRecord.getId(), borrowRecord);
        return borrowRecord;
    }

    public Collection<BorrowRecord> listOfBorrowRecords() {
        return borrowRecords.values().stream()
                .sorted(Comparator.comparing(BorrowRecord::daysLeftToReturn))
                .collect(Collectors.toUnmodifiableList());
    }

    public boolean isAvailable() {
        return borrowRecords.values().stream().noneMatch(BorrowRecord::isNotReturned);
    }

    public User getCurrentBorrower() {
        return borrowRecords.values().stream()
                .filter(BorrowRecord::isNotReturned)
                .map(BorrowRecord::getUser)
                .findFirst()
                .orElse(null);
    }

    public void returnBook(UUID borrowRecordId, User user, LocalDate returnDate) {
        this.listOfBorrowRecords().stream()
                .filter(br -> br.getId().equals(borrowRecordId))
                .findFirst()
                .orElseThrow(() -> new LibraryException("Cannot find borrow record"))
                .returnBook(user, returnDate);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public UUID getId() {
        return id;
    }

    public BookCategory getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equal(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("title", title)
                .add("author", author)
                .add("publicationDate", publicationDate)
                .add("category", category)
                .add("id", id)
                .toString();
    }
}
