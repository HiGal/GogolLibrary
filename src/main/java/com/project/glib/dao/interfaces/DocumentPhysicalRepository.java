package com.project.glib.dao.interfaces;

import com.project.glib.model.DocumentPhysical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource()
public interface DocumentPhysicalRepository extends JpaRepository<DocumentPhysical, Long> {
}
