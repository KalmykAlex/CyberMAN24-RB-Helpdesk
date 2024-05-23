package com.example.helpdesk.department;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentService {

    DepartmentRepository departmentRepository;

    public boolean createDepartment(String departmentName){

        Optional<Department> tmp = departmentRepository.findDepartmentByDepartmentName(departmentName);
        if(tmp.isEmpty()){
            Department department = new Department();
            department.setDepartmentName(departmentName);
            departmentRepository.save(department);
            return true;
        }
        return false;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}
