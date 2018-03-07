package com.project.glib.dao.interfaces;

import com.project.glib.model.Book;
import com.project.glib.model.DocumentPhysical;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentPhysicalRepository extends JpaRepository<DocumentPhysical, Long> {
    DocumentPhysical getFirstByIdDoc(long id_doc);
    void deleteByIdDoc(long id);
}
