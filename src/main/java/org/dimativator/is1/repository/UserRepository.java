package org.dimativator.is1.repository;

import org.dimativator.is1.model.Role;
import org.dimativator.is1.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByRole(Role role);

    Optional<User> findByLogin(String login);

    Page<User> findAllByRole(Role role, Pageable pageable);
}
