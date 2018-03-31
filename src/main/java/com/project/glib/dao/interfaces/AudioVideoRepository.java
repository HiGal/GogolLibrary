package com.project.glib.dao.interfaces;

import com.project.glib.model.AudioVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource()
public interface AudioVideoRepository extends JpaRepository<AudioVideo, Long> {
    boolean existsAllByTitle(String title);
}
