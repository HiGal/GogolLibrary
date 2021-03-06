package com.project.glib.dao.interfaces;

import com.project.glib.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource()
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findByDocVirIdAndDocTypeOrderByPriority(long docId, String docType);
}
