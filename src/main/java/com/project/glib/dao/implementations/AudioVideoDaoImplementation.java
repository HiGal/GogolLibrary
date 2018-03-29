package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.AudioVideoRepository;
import com.project.glib.dao.interfaces.DocumentDao;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.AudioVideo;
import com.project.glib.model.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class AudioVideoDaoImplementation implements DocumentDao<AudioVideo> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AudioVideoDaoImplementation.class);
    private static final String TYPE = Document.BOOK;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
    private static final String ADD_AV = TYPE + ADD;
    private static final String UPDATE_AV = TYPE + UPDATE;
    private static final String REMOVE_AV = TYPE + REMOVE;
    private static final String LIST = TYPE + ModifyByLibrarian.LIST;
    private final AudioVideoRepository audioVideoRepository;

    @Autowired
    public AudioVideoDaoImplementation(AudioVideoRepository audioVideoRepository) {
        this.audioVideoRepository = audioVideoRepository;
    }

    /**
     * Add new item of AudioVideo in database library
     *
     * @param audioVideo new AudioVideo
     */
    @Override
    public void add(AudioVideo audioVideo) {
        audioVideoRepository.saveAndFlush(audioVideo);
        logger.info(ADD_AV + audioVideo);
    }

    /**
     * Update existed AudioVideo or create if it not exist
     *
     * @param audioVideo - updated AudioVideo
     */
    @Override
    public void update(AudioVideo audioVideo) {
        audioVideoRepository.saveAndFlush(audioVideo);
        logger.info(UPDATE_AV + audioVideo);
    }

    /**
     * Removes AudioVideo instance from library
     *
     * @param audioVideoId ID of AudioVideo
     */
    @Override
    public void remove(long audioVideoId) {
        audioVideoRepository.delete(audioVideoId);
        logger.info(REMOVE_AV + audioVideoId);
    }

    /**
     * Checks existence of AudioVideo in database
     *
     * @param audioVideo instance of AudioVideo module
     * @return instance of AudioVideo module
     */
    @Override
    public AudioVideo isAlreadyExist(AudioVideo audioVideo) {
        try {
            return audioVideoRepository.findAll().stream()
                    .filter(av -> av.getTitle().equals(audioVideo.getTitle()))
                    .filter(av -> av.getAuthor().equals(audioVideo.getAuthor()))
                    .findFirst().get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Gets AudioVideo by it ID
     *
     * @param audioVideoId id of AudioVideo instance
     * @return instance of AudioVideo module
     */
    @Override
    public AudioVideo getById(long audioVideoId) {
        return audioVideoRepository.findOne(audioVideoId);
    }

    /**
     * Gets the number of copies of AudioVideo that are already in a library
     *
     * @param audioVideoId ID of AudioVideo instance
     * @return count of copies
     * @throws Exception
     */
    @Override
    public int getCountById(long audioVideoId) throws Exception {
        try {
            return getById(audioVideoId).getCount();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    /**
     * Decrements the number of one AudioVideo document
     *
     * @param av_ID ID of AudioVideo instance
     */
    @Override
    public void decrementCountById(long av_ID) {
        AudioVideo audioVideo = getById(av_ID);
        audioVideo.setCount(audioVideo.getCount() - 1);
        audioVideoRepository.saveAndFlush(audioVideo);
    }

    /**
     * Increments the number of one AudioVideo document
     *
     * @param av_ID ID of AudioVideo
     */
    @Override
    public void incrementCountById(long av_ID) {
        AudioVideo audioVideo = getById(av_ID);
        audioVideo.setCount(audioVideo.getCount() + 1);
        audioVideoRepository.saveAndFlush(audioVideo);
    }

    /**
     * Gets price of AudioVideo instance by ID
     *
     * @param av_ID ID of AudioVideo
     * @return price of book
     * @throws Exception
     */
    @Override
    public int getPriceById(long av_ID) throws Exception {
        try {
            return getById(av_ID).getPrice();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    /**
     * Collects all AudioVideo instances from the database to the list
     *
     * @return list of all AudioVideo instances
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<AudioVideo> getList() {
        try {
            List<AudioVideo> audioVideos = audioVideoRepository.findAll();

            for (AudioVideo audioVideo : audioVideos) {
                logger.info(LIST + audioVideo);
            }

            return audioVideos;
        } catch (NoSuchElementException | NullPointerException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Gets ID of AudioVideo instance
     *
     * @param audioVideo AudioVideo instance
     * @return ID of AudioVideo
     * @throws Exception
     */
    @Override
    public long getId(AudioVideo audioVideo) throws Exception {
        try {
            return isAlreadyExist(audioVideo).getId();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }
}
