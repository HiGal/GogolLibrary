package com.project.glib.controller;

import com.project.glib.dao.interfaces.JournalRepository;
import com.project.glib.model.Book;
import com.project.glib.model.Journal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class JournalController {

    private final JournalRepository journalRepository;

    @Autowired
    public JournalController(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @RequestMapping(value = "journals", method = RequestMethod.GET)
    public String listJournals(Model model) {
        model.addAttribute("journal", new Book());
        model.addAttribute("listJournals", this.journalRepository.findAll());

        return "journals";
    }

    @RequestMapping(value = "/journals/add", method = RequestMethod.POST)
    public String addJournal(@ModelAttribute("journal") Journal journal) {
        if (journal.getId() == 0) {
            this.journalRepository.save(journal);
        } else {
            this.journalRepository.save(journal);
        }

        return "redirect:/journals";
    }

    @RequestMapping("journals/remove/{id}")
    public String removeJournal(@PathVariable("id") long id) {
        this.journalRepository.delete(id);

        return "redirect:/journals";
    }

    @RequestMapping("journals/edit/{id}")
    public String editJournal(@PathVariable("id") long id, Model model) {
        model.addAttribute("journal", this.journalRepository.getOne(id));
        model.addAttribute("listJournals", this.journalRepository.findAll());

        return "journals";
    }

    @RequestMapping("journaldata/{id}")
    public String journalData(@PathVariable("id") long id, Model model) {
        model.addAttribute("journal", this.journalRepository.getOne(id));

        return "journaldata";
    }
}

