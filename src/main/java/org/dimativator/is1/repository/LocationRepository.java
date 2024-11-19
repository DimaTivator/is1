package org.dimativator.is1.repository;

import org.dimativator.is1.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByXAndYAndZ(Long x, Float y, Float z);
}
