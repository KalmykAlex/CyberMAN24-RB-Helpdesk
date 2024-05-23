package com.example.helpdesk.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    User findFirstByEmail(String email);

    Optional<List<User>> findAllByDepartmentDepartmentName(String departmentName);

    List<User> findByDepartmentIsNull();
}
