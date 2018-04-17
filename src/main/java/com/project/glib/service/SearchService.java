package com.project.glib.service;

import com.project.glib.model.AudioVideo;
import com.project.glib.model.Book;
import com.project.glib.model.Journal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final BookService bookService;
    private final JournalService journalService;
    private final AudioVideoService audioVideoService;

    public SearchService(BookService bookService,
                         JournalService journalService,
                         AudioVideoService audioVideoService) {
        this.bookService = bookService;
        this.journalService = journalService;
        this.audioVideoService = audioVideoService;
    }

    public List<Book> searchBook(Book bookIn) {
        return bookService.getList().stream()
                .filter(book -> book.getTitle().equals(bookIn.getTitle()) || bookIn.getTitle().equals(""))
                .filter(book -> book.getAuthor().equals(bookIn.getAuthor()) || bookIn.getAuthor().equals(""))
                .filter(book -> book.getEdition().equals(bookIn.getEdition()) || bookIn.getEdition().equals(""))
                .filter(book -> book.getPublisher().equals(bookIn.getPublisher()) || bookIn.getPublisher().equals(""))
                .filter(book -> book.getYear() == bookIn.getYear() || bookIn.getYear() == 0)
                .collect(Collectors.toList());
    }

    public List<Journal> searchJournal(Journal journalIn) {
        return journalService.getList().stream()
                .filter(journal -> journal.getTitle().equals(journalIn.getTitle()) || journalIn.getTitle().equals(""))
                .filter(journal -> journal.getAuthor().equals(journalIn.getAuthor()) || journalIn.getAuthor().equals(""))
                .filter(journal -> journal.getEditor().equals(journalIn.getEditor()) || journalIn.getEditor().equals(""))
                .filter(journal -> journal.getName().equals(journalIn.getName()) || journalIn.getName().equals(""))
                .filter(journal -> journal.getIssue() == journalIn.getIssue() || journalIn.getIssue() == 0)
                .collect(Collectors.toList());
    }

    public List<AudioVideo> searchAudioVideo(AudioVideo avIn) {
        return audioVideoService.getList().stream()
                .filter(av -> av.getTitle().equals(avIn.getTitle()) || avIn.getTitle().equals(""))
                .filter(av -> av.getAuthor().equals(avIn.getAuthor()) || avIn.getAuthor().equals(""))
                .collect(Collectors.toList());
    }
}
