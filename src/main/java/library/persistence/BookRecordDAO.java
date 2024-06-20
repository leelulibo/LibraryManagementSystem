package library.persistence;

import library.model.Book;
import library.model.BorrowRecord;

import java.util.Collection;
import java.util.UUID;

public interface BookRecordDAO {


    void saveRecords(BorrowRecord record);
    Collection<BorrowRecord> getAllBorrowedRecords();
    BorrowRecord findRecordById(UUID id);
}

