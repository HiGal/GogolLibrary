package com.project.glib.controller;

import com.project.glib.dao.interfaces.AudioVideoRepository;
import com.project.glib.model.AudioVideo;
import com.project.glib.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AudioVideoController {

    private final AudioVideoRepository audioVideoRepository;

    @Autowired
    public AudioVideoController(AudioVideoRepository audioVideoRepository) {
        this.audioVideoRepository = audioVideoRepository;
    }

    @RequestMapping(value = "audiovideos", method = RequestMethod.GET)
    public String listBooks(Model model) {
        model.addAttribute("audiovideo", new Book());
        model.addAttribute("listAudioVideos", this.audioVideoRepository.findAll());

        return "audiovideos";
    }

    @RequestMapping(value = "/audiovideos/add", method = RequestMethod.POST)
    public String addBook(@ModelAttribute("audiovideo") AudioVideo audioVideo) {
        if (audioVideo.getId() == 0) {
            this.audioVideoRepository.save(audioVideo);
        } else {
            this.audioVideoRepository.save(audioVideo);
        }

        return "redirect:/audiovideos";
    }

    @RequestMapping("audiovideos/remove/{id}")
    public String removeBook(@PathVariable("id") long id) {
        this.audioVideoRepository.delete(id);

        return "redirect:/audiovideos";
    }

    @RequestMapping("audiovideos/edit/{id}")
    public String editBook(@PathVariable("id") long id, Model model) {
        model.addAttribute("audiovideo", this.audioVideoRepository.getOne(id));
        model.addAttribute("listAudioVideos", this.audioVideoRepository.findAll());

        return "audiovideos";
    }

    @RequestMapping("audiovideodata/{id}")
    public String bookData(@PathVariable("id") long id, Model model) {
        model.addAttribute("audiovideo", this.audioVideoRepository.getOne(id));

        return "audiovideodata";
    }
}
