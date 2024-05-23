package com.example.helpdesk.department;


import com.example.helpdesk.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Department {

    @Id
    @GeneratedValue
    private Long departmentId;

    private String departmentName;

    @OneToMany(mappedBy = "department")
    private List<User> users;
}
