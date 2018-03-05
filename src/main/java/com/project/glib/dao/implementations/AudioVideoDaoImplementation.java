package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.AudioVideoRepository;
import com.project.glib.dao.interfaces.DocumentDao;
import com.project.glib.model.AudioVideo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AudioVideoDaoImplementation implements DocumentDao<AudioVideo> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AudioVideoDaoImplementation.class);
    private final AudioVideoRepository audioVideoRepository;

    @Autowired
    public AudioVideoDaoImplementation(AudioVideoRepository audioVideoRepository) {
        this.audioVideoRepository = audioVideoRepository;
    }

    @Override
    public void add(AudioVideo audioVideo) {
        audioVideoRepository.save(audioVideo);
        logger.info("AudioVideo successfully saved. AudioVideo details : " + audioVideo);
    }

    @Override
    public void update(AudioVideo audioVideo) {
        audioVideoRepository.save(audioVideo);
        logger.info("AudioVideo successfully update. AudioVideo details : " + audioVideo);
    }

    @Override
    public void remove(long audioVideoId) {
        audioVideoRepository.delete(audioVideoId);
    }

    @Override
    public AudioVideo getById(long audioVideoId) {
        return audioVideoRepository.findOne(audioVideoId);
    }

    @Override
    public int getCountById(long audioVideoId) {
        return audioVideoRepository.findOne(audioVideoId).getCount();
    }

    @Override
    public void decrementCountById(long avId) {
        audioVideoRepository.findOne(avId).setCount(audioVideoRepository.findOne(avId).getCount() - 1);
    }

    @Override
    public int getPriceById(long avId) {
        return audioVideoRepository.findOne(avId).getPrice();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AudioVideo> getList() {
        List<AudioVideo> audioVideos = audioVideoRepository.findAll();

        for (AudioVideo audioVideo : audioVideos) {
            logger.info("AudioVideo list : " + audioVideo);
        }

        return audioVideos;
    }

    @Override
    public List<AudioVideo> getListCountNotZero() {
        List<AudioVideo> audioVideos = audioVideoRepository.findAll().stream().filter(audioVideo -> audioVideo.getCount() > 0).collect(Collectors.toList());

        for (AudioVideo audioVideo : audioVideos) {
            logger.info("AudioVideo list : " + audioVideo);
        }

        return audioVideos;
    }
}
