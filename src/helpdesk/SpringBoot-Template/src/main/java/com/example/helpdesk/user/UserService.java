package com.example.helpdesk.user;

import com.example.helpdesk.department.Department;
import com.example.helpdesk.department.DepartmentRepository;
import com.example.helpdesk.ticket.Ticket;
import com.example.helpdesk.comment.CommentRepository;
import com.example.helpdesk.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.RhinoSecurityManager;
import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final DepartmentRepository departmentRepository;
    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;


    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();


        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        repository.save(user);
    }

    public boolean updateUserRole(String email, Role role){
        Optional<User> userTmp = repository.findByEmail(email);
        if(userTmp.isPresent()){
            var user = userTmp.get();
            user.setRole(role);
            repository.save(user);
            return true;
        }
        return false;
    }

    public boolean setOwnDepartment(String departmentName) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userTemp = repository.findByEmail(userDetails.getUsername());
        if(userTemp.isPresent()) {
            var user = userTemp.get();

            if(user.getDepartment() != null){
                return false;
            }

            List<Department> departmentList = departmentRepository.findAll();
            if(departmentList.isEmpty()){
                return false;
            }

            for(Department d: departmentList){
                if (d.getDepartmentName().contains(departmentName)){
                    user.setDepartment(d);
                    repository.save(user);
                    return true;
                }
            }
            return false;

        }else {
            return false;
        }
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public List<User> getAllUsersFromOwnDepartment() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userTemp = repository.findByEmail(userDetails.getUsername());

        if(userTemp.isPresent()) {

            User userTmp = userTemp.get();
            Department departmentTmp = userTmp.getDepartment();

            if(departmentTmp == null){
                return null;
            }

            String departmentNameTmp = userTmp.getDepartment().getDepartmentName();
            var result = repository.findAllByDepartmentDepartmentName(departmentNameTmp);
            return result.orElse(null);
        }
        return null;
    }

    public List<User> getAllUsersWithoutDepartment() {
        return repository.findByDepartmentIsNull();
    }


    public int resetPassword(ChangePasswordRequest changePasswordRequest) {
        var user = repository.findByEmail(changePasswordRequest.getUsername());

        if(user.isEmpty()){
            return 1;
        }

        if(!changePasswordRequest.getConfirmationPassword().equals(
                changePasswordRequest.getNewPassword()
        )){
            return 2;
        }

        user.get().setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        repository.save(user.get());
        return 0;
    }

    public String getVIP() {
        int toDecrease = 10000000;
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userTemp = repository.findByEmail(userDetails.getUsername());
        if(userTemp.isPresent()) {
            var user = userTemp.get();
            user.setCoins(user.getCoins() - toDecrease);
            repository.save(user);
            if(user.getCoins() > 0){
                Optional<Ticket> goldenTicket = ticketRepository.findById(1L);
                if(goldenTicket.isPresent()){
                    try {
                        String goldenT = commentRepository.findById(1L).get().getComment();
                        return goldenT;
                    } catch (Exception e){
                        return "Error on backend, contact green team";
                    }
                }
            }
        }
        return "You are not VIP, not yet.";
    }

    public String getRole() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userTemp = repository.findByEmail(userDetails.getUsername());
        if(userTemp.isPresent()) {
            return userTemp.get().getRole().name();
        }
        return "";
    }
}
