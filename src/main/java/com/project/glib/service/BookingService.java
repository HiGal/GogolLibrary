package com.project.glib.service;

import com.project.glib.dao.implementations.AudioVideoDaoImplementation;
import com.project.glib.dao.implementations.BookDaoImplementation;
import com.project.glib.dao.implementations.JournalDaoImplementation;
import org.springframework.beans.factory.annotation.Autowired;

public class BookingService {
    public final String BOOK = "book";
    public final String JOURNAL = "journal";
    public final String AV = "audio_video";

    public final BookDaoImplementation bookDaoImplementation;
    public final JournalDaoImplementation journalDaoImplementation;
    public final AudioVideoDaoImplementation audioVideoDaoImplementation;

    @Autowired
    BookingService(BookDaoImplementation bookDaoImplementation,
                   JournalDaoImplementation journalDaoImplementation,
                   AudioVideoDaoImplementation audioVideoDaoImplementation) {
        this.bookDaoImplementation = bookDaoImplementation;
        this.journalDaoImplementation = journalDaoImplementation;
        this.audioVideoDaoImplementation = audioVideoDaoImplementation;
    }

    public boolean toBookDocument(long docId, String type, long userId) {
        boolean ans = false;
        if (type.equals(BOOK)) {

        } else if (type.equals(JOURNAL)) {

        } else if (type.equals(AV)) {

        } else {
            return ans;
        }
        return ans;
    }
}
