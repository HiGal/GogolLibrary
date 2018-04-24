package com.project.glib.dao.interfaces;

import com.project.glib.model.DocumentsKeywords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource()
public interface DocumentsKeywordsRepository extends JpaRepository<DocumentsKeywords, Long> {
}
