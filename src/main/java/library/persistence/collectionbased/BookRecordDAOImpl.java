package library.persistence.collectionbased;

import library.model.Book;
import library.model.BorrowRecord;
import library.persistence.BookDAO;
import library.persistence.BookRecordDAO;

import java.util.*;

public class BookRecordDAOImpl implements BookRecordDAO {
    private final Map<UUID, BorrowRecord> records = new HashMap<>();

    @Override
    public void saveRecords(BorrowRecord record) {
        records.put(record.getId(), record);
    }

    @Override
    public Collection<BorrowRecord> getAllBorrowedRecords() {
        return  records.values();
    }

    @Override
    public BorrowRecord findRecordById(UUID id) {
        return records.get(id);
    }
}
