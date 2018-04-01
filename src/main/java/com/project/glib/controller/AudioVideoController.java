package com.project.glib.controller;

import com.project.glib.model.AudioVideo;
import com.project.glib.service.AudioVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AudioVideoController {

    private final AudioVideoService audioVideoService;

    @Autowired
    public AudioVideoController(AudioVideoService audioVideoService) {
        this.audioVideoService = audioVideoService;
    }

    @RequestMapping(value = "/librarian/add/AV")
    public String addAV(@RequestBody AudioVideo audioVideo,
                        @RequestParam(value = "shelf") String shelf) {
        try {
            audioVideoService.add(audioVideo, shelf);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    //    @RequestMapping(value = "/book/remove/{num_copies}", method = RequestMethod.POST)
    public String removeAV(@RequestBody AudioVideo audioVideo) {
        try {
            audioVideoService.remove(audioVideo.getId());
            return "AV is successfully removed";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(value = "/book/remove/{id_copy}", method = RequestMethod.POST)
    public String removeCopyOfAV(@RequestBody AudioVideo audioVideo, @PathVariable("id_copy") long copyId) {
        try {
            audioVideoService.removeCopy(audioVideo.getId(), copyId);
            return "Copy of AV is successfully removed";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
