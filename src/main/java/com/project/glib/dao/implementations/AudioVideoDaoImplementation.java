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
     * Add new item of AudioVideo in library
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
     * Remove AudioVideo from library
     *
     * @param audioVideoId id of AudioVideo
     */
    @Override
    public void remove(long audioVideoId) {
        audioVideoRepository.delete(audioVideoId);
        logger.info(REMOVE_AV + audioVideoId);
    }

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
     * get AudioVideo by it id
     *
     * @param audioVideoId id of AudioVideo
     * @return AudioVideo object
     */
    @Override
    public AudioVideo getById(long audioVideoId) {
        return audioVideoRepository.findOne(audioVideoId);
    }

    /**
     * get how many copies of AudioVideo we already have in library
     *
     * @param audioVideoId id of AudioVideo
     * @return count of copies
     * @throws Exception
     */
    @Override
    public int getCountById(long audioVideoId) throws Exception {
        try {
            return audioVideoRepository.findOne(audioVideoId).getCount();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    /**
     * set count to count - 1 for AudioVideo
     *
     * @param avId id of AudioVideo
     */
    @Override
    public void decrementCountById(long avId) {
        AudioVideo audioVideo = audioVideoRepository.findOne(avId);
        audioVideo.setCount(audioVideo.getCount() - 1);
        audioVideoRepository.saveAndFlush(audioVideo);
    }

    /**
     * set count to count + 1 for AudioVideo
     *
     * @param avId id of AudioVideo
     */
    @Override
    public void incrementCountById(long avId) {
            AudioVideo audioVideo = audioVideoRepository.findOne(avId);
            audioVideo.setCount(audioVideo.getCount() + 1);
            audioVideoRepository.saveAndFlush(audioVideo);
    }

    /**
     * get price of AudioVideo by id
     *
     * @param avId id of AudioVideo
     * @return price of book
     * @throws Exception
     */
    @Override
    public int getPriceById(long avId) throws Exception {
        try {
            return audioVideoRepository.findOne(avId).getPrice();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

//    /**
//     * get list of all AudioVideo with count bigger than zero or renewed
//     *
//     * @return list of AudioVideo with count bigger than zero or renewed
//     */
//    @Override
//    public List<AudioVideo> getListCountNotZeroOrRenewed() {
//        try {
//            List<AudioVideo> audioVideos = audioVideoRepository.findAll().stream().filter(audioVideo -> audioVideo.getCount() > 0).collect(Collectors.toList());
//
//            for (AudioVideo audioVideo : audioVideos) {
//                logger.info("AudioVideo list : " + audioVideo);
//            }
//
//            return audioVideos;
//        } catch (NoSuchElementException | NullPointerException e) {
//            return new ArrayList<>();
//        }
//    }

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

    @Override
    public long getId(AudioVideo audioVideo) throws Exception {
        try {
            return isAlreadyExist(audioVideo).getId();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }
}
