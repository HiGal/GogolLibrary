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

    public List<Book> searchBook(Book boin) {
        List<Book> books = bookService.getList();

        if (boin.getTitle() != null) {
            books = books.stream().filter(Book -> Book.getTitle().equals(boin.getTitle()))
                    .collect(Collectors.toList());
        }

        if (boin.getAuthor() != null) {
            books = books.stream().filter(Book -> Book.getAuthor().equals(boin.getAuthor()))
                    .collect(Collectors.toList());
        }

        if (boin.getEdition() != null) {
            books = books.stream().filter(Book -> Book.getEdition().equals(boin.getEdition()))
                    .collect(Collectors.toList());
        }

        if (boin.getPublisher() != null) {
            books = books.stream().filter(Book -> Book.getPublisher().equals(boin.getPublisher()))
                    .collect(Collectors.toList());
        }

        if (boin.getTitle() != null) {
            books = books.stream().filter(Book -> Book.getTitle().equals(boin.getTitle()))
                    .collect(Collectors.toList());
        }

        if (boin.getYear() != 0) {
            books = books.stream().filter(Book -> Book.getYear() == boin.getYear())
                    .collect(Collectors.toList());
        }

        return books;
    }

    public List<Journal> searchJournal(Journal join) {
        List<Journal> journals = journalService.getList();

        if (join.getTitle() != null) {
            journals = journals
                    .stream().filter(Journal -> Journal.getTitle().equals(join.getTitle()))
                    .collect(Collectors.toList());
        }

        if (join.getAuthor() != null) {
            journals = journals
                    .stream().filter(Journal -> Journal.getAuthor().equals(join.getAuthor()))
                    .collect(Collectors.toList());
        }

        if (join.getEditor() != null) {
            journals = journals
                    .stream().filter(Journal -> Journal.getEditor().equals(join.getEditor()))
                    .collect(Collectors.toList());
        }

        if (join.getName() != null) {
            journals = journals
                    .stream().filter(Journal -> Journal.getName().equals(join.getName()))
                    .collect(Collectors.toList());
        }

        if (join.getIssue() != 0) {
            journals = journals
                    .stream().filter(Journal -> Journal.getIssue() == join.getIssue())
                    .collect(Collectors.toList());
        }

        return journals;
    }

    public List<AudioVideo> searchAudioVideo(AudioVideo avin) {
        List<AudioVideo> audioVideos = audioVideoService.getList();

        if (avin.getTitle() != null) {
            audioVideos = audioVideos.stream()
                    .filter(AudioVideo -> AudioVideo.getTitle().equals(avin.getTitle()))
                    .collect(Collectors.toList());
        }

        if (avin.getAuthor() != null) {
            audioVideos = audioVideos.stream()
                    .filter(AudioVideo -> AudioVideo.getAuthor().equals(avin.getAuthor()))
                    .collect(Collectors.toList());
        }

        return audioVideos;
    }


}
