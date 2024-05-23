package com.example.helpdesk.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findDepartmentByDepartmentName(String departmentName);
    Optional<Department> findDepartmentByDepartmentNameContains(String departmentName);


}
