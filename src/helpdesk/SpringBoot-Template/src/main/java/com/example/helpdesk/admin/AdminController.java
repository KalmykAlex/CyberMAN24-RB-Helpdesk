package com.example.helpdesk.admin;

import com.example.helpdesk.comment.Comment;
import com.example.helpdesk.comment.CommentService;
import com.example.helpdesk.department.DepartmentService;
import com.example.helpdesk.ticket.Ticket;
import com.example.helpdesk.ticket.TicketService;
import com.example.helpdesk.user.Role;
import com.example.helpdesk.user.User;
import com.example.helpdesk.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/admin")
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;
    private final DepartmentService departmentService;
    private final TicketService ticketService;
    private final CommentService commentService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public String get() {
        return "GET:: admin controller";
    }
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public String post() {
        return "POST:: admin controller";
    }
    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    public String put() {
        return "PUT:: admin controller";
    }
    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
    public String delete() {
        return "DELETE:: admin controller";
    }


    @PostMapping("/updateUserRole")
    ResponseEntity<?> updateUserRole(String email, String role){
        if(role.equals("CLIENT")){
            if(userService.updateUserRole(email, Role.USER))
                return ResponseEntity.ok().body("User role updated");
            else return ResponseEntity.badRequest().build();
        }

        if(role.equals("MANAGER")){
            if(userService.updateUserRole(email, Role.MANAGER))
                return ResponseEntity.ok().body("User role updated");
            else return ResponseEntity.badRequest().build();
        }

        if(role.equals("ADMIN")){
            if(userService.updateUserRole(email, Role.ADMIN))
                return ResponseEntity.ok().body("User role updated");
            else return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/createDepartment")
    public ResponseEntity<?> createDepartment(String departmentName){
        boolean chekc = departmentService.createDepartment(departmentName);
        if(chekc){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Department name already exists");
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        List<User> usr =  userService.getAllUsers();
        return usr;
    }

    @GetMapping("/getAllUsersFromOwnDepartment")
    public ResponseEntity<List<User>> getAllUsersFromOwnDepartment(){
        return ResponseEntity.ok().body(userService.getAllUsersFromOwnDepartment());
    }

    @GetMapping("/getAllUsersWithoutDepartment")
    public ResponseEntity<List<User>> getAllUsersWithoutDepartment(){
        return ResponseEntity.ok().body(userService.getAllUsersWithoutDepartment());
    }

    @PostMapping("/assignTicketToManager")
    public ResponseEntity<?> assignTicketToManager(String email, Long id){
        Integer check = ticketService.assignTicketToManager(email, id);
        if(check == 0){
            return ResponseEntity.ok().build();
        }
        if(check == 1){
            return ResponseEntity.badRequest().body("Utilizatorul nu exista/Ticketul nu exista ori este deja finalizat");
        }

        if(check == 2){
            return ResponseEntity.badRequest().body("Ticketul nu exista");
        }

        if(check == 3){
            return ResponseEntity.badRequest().body("Utilizatorul nu este manager");
        }

        return ResponseEntity.badRequest().body("Unexpected");
    }


    @PostMapping("/getTicket")
    public ResponseEntity<?> getTicket(@RequestBody TicketRequest webSocketTicketRequest) {
        Optional<Ticket> ticket = ticketService.getTicket(webSocketTicketRequest.getId());
        if(ticket.isPresent()){
            Ticket temp = ticket.get();
            temp.setComments(null);
            return ResponseEntity.ok().body(ticket.get());
        }
        return ResponseEntity.badRequest().body("Wrong id or not allowed");
    }

    @PostMapping("/getComment")
    public ResponseEntity<?> getComment(@RequestBody TicketRequest webSocketTicketRequest){
        Optional<Comment> comment = commentService.getComment(webSocketTicketRequest.getId());
        if(comment.isPresent()){
            Comment temp = comment.get();
            temp.setFile(null);    // Let this be like this
            temp.setTicket(null);  // Let this be like this
            return ResponseEntity.ok().body(comment.get());
        }
        return ResponseEntity.noContent().build(); // 204, no need to Google it
    }


}
