package org.dimativator.is1.repository;

import org.dimativator.is1.model.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;

@Repository
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {
    boolean existsByXAndY(@Max(128) Long x, @Min(-436) Double y);
    Optional<Coordinates> findByXAndY(@Max(128) Long x, @Min(-436) Double y);
}
