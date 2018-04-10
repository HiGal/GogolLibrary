package com.project.glib.service;

import com.project.glib.dao.implementations.AudioVideoDaoImplementation;
import com.project.glib.model.AudioVideo;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentPhysical;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AudioVideoService implements DocumentServiceInterface<AudioVideo> {
    public static final String TYPE = Document.AV;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String UPDATE_EXCEPTION = ModifyByLibrarianService.UPDATE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String REMOVE_EXCEPTION = ModifyByLibrarianService.REMOVE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
    private final AudioVideoDaoImplementation avDao;
    private final DocumentPhysicalService docPhysService;

    public AudioVideoService(AudioVideoDaoImplementation avDao, DocumentPhysicalService docPhysService) {
        this.avDao = avDao;
        this.docPhysService = docPhysService;
    }

    /**
     * Adds physical AudioVideo instance
     *
     * @param audioVideo AV model
     * @param shelf      shelf of document in library
     * @throws Exception
     */
    @Override
    public void add(AudioVideo audioVideo, String shelf) throws Exception {
        if (shelf.equals("")) {
            throw new Exception(SHELF_EXCEPTION);
        }
        add(audioVideo);
        for (int i = 0; i < audioVideo.getCount(); i++) {
            // TODO add keywords options
            docPhysService.add(new DocumentPhysical(shelf,
                    true, audioVideo.getId(), Document.AV, null));
        }
    }

    /**
     * Adds new virtual AV in library
     *
     * @param audioVideo AV model
     * @throws Exception
     */
    private void add(AudioVideo audioVideo) throws Exception {
        checkValidParameters(audioVideo);
        try {
            AudioVideo existedAV = isAlreadyExist(audioVideo);
            if (existedAV == null) {
                avDao.add(audioVideo);
            } else {
                existedAV.setCount(existedAV.getCount() + audioVideo.getCount());
                existedAV.setPrice(audioVideo.getPrice());
                avDao.update(existedAV);
            }
        } catch (Exception e) {
            throw new Exception(ADD_EXCEPTION);
        }
    }

    /**
     * Updates the AudioVideo instance in DB
     *
     * @param audioVideo AudioVideo model
     * @throws Exception
     */
    @Override
    public void update(AudioVideo audioVideo) throws Exception {
        checkValidParameters(audioVideo);
        // TODO solve case then librarian change count of AVs
        try {
            AudioVideo existedAV = isAlreadyExist(audioVideo);
            if (existedAV != null && !existedAV.equals(audioVideo)) {
                remove(audioVideo.getId());
                existedAV.setCount(existedAV.getCount() + audioVideo.getCount());
                existedAV.setPrice(audioVideo.getPrice());
                audioVideo = existedAV;
            }
            avDao.update(audioVideo);
        } catch (Exception e) {
            throw new Exception(UPDATE_EXCEPTION);
        }
    }

    /**
     * Removes AudioVideo instance from DB
     *
     * @param audioVideoId AudioVideo virtual id
     * @throws Exception
     */
    @Override
    public void remove(long audioVideoId) throws Exception {
        try {
            docPhysService.removeAllByDocVirIdAndDocType(audioVideoId, Document.AV);
            avDao.remove(audioVideoId);
        } catch (Exception e) {
            throw new Exception(REMOVE_EXCEPTION);
        }
    }

    /**
     * Removes copy of AudioVideo instance from DB
     *
     * @param avId   virtual ID
     * @param copyId ID of AudioVideo copy
     * @throws Exception
     */
    @Override
    public void removeCopy(long avId, long copyId) throws Exception {
        docPhysService.remove(copyId);
        decrementCountById(avId);
    }

    /**
     * Removes all AudioVideo copies on the same shelf
     *
     * @param avId  virtual ID
     * @param shelf shelf of AV copies
     * @throws Exception
     */
    @Override
    public void removeAllCopiesByShelf(long avId, String shelf) throws Exception {
        List<DocumentPhysical> docPhysList = docPhysService.getByDocVirIdAndDocType(avId, Document.AV)
                .stream()
                .filter(doc -> doc.getShelf().equals(shelf))
                .collect(Collectors.toList());

        for (DocumentPhysical docPhys : docPhysList) removeCopy(avId, docPhys.getId());
    }

    /**
     * Checks model for requirements
     *
     * @param audioVideo AV model
     * @throws Exception
     */
    @Override
    public void checkValidParameters(AudioVideo audioVideo) throws Exception {
        if (audioVideo.getPrice() <= 0) {
            throw new Exception(PRICE_EXCEPTION);
        }

        if (audioVideo.getCount() < 0) {
            throw new Exception(COUNT_EXCEPTION);
        }

        if (audioVideo.getTitle().equals("")) {
            throw new Exception(TITLE_EXCEPTION);
        }

        if (audioVideo.getAuthor().equals("")) {
            throw new Exception(AUTHOR_EXCEPTION);
        }
    }

    /**
     * Checks the AV copy for being special
     *
     * @param note special case of AV model
     * @return true if equals
     */
    @Override
    public boolean isNote(String note) {
        return false;
    }

    /**
     * Checks the existence of AudioVideo instance in DB
     *
     * @param audioVideo AV model
     * @return
     */
    @Override
    public AudioVideo isAlreadyExist(AudioVideo audioVideo) {
        try {
            return avDao.isAlreadyExist(audioVideo);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Gets instance by virtual ID
     *
     * @param audioVideoId AV model
     * @return
     */
    @Override
    public AudioVideo getById(long audioVideoId) {
        return avDao.getById(audioVideoId);
    }

    /**
     * Gets count of copies of AV instance in DB
     *
     * @param audioVideoId AV model
     * @return number of copies
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
     * Decrements number of copies of AV instance by one
     *
     * @param avId AV virtual id
     */
    @Override
    public void decrementCountById(long avId) {
        AudioVideo audioVideo = getById(avId);
        audioVideo.setCount(audioVideo.getCount() - 1);
        avDao.update(audioVideo);
    }

    /**
     * Increments number of copies of AV instance by one
     *
     * @param avId AV virtual id
     */
    @Override
    public void incrementCountById(long avId) {
        AudioVideo audioVideo = getById(avId);
        audioVideo.setCount(audioVideo.getCount() + 1);
        avDao.update(audioVideo);
    }

    /**
     * Gets the price of AV instance
     *
     * @param avId AV virtual id
     * @return price
     * @throws Exception
     */
    @Override
    public int getPriceById(long avId) throws Exception {
        try {
            return getById(avId).getPrice();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    /**
     * Return List of all AV in DB
     *
     * @return list of AV
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<AudioVideo> getList() {
        try {
            return avDao.getList();
        } catch (NoSuchElementException | NullPointerException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Gets ID of AV instance. Also, checks existance in DB
     *
     * @param audioVideo model AV
     * @return ID in DB
     * @throws Exception
     */
    @Override
    public long getId(AudioVideo audioVideo) throws Exception {
        try {
            return avDao.getId(audioVideo);
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }
}
