package com.example.helpdesk.client;

import com.example.helpdesk.HelpdeskLogger;
import com.example.helpdesk.admin.TicketRequest;
import com.example.helpdesk.comment.Comment;
import com.example.helpdesk.comment.CommentService;
import com.example.helpdesk.department.Department;
import com.example.helpdesk.department.DepartmentService;
import com.example.helpdesk.ticket.Ticket;
import com.example.helpdesk.ticket.TicketSecurity;
import com.example.helpdesk.ticket.TicketService;
import com.example.helpdesk.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/client")
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@AllArgsConstructor
public class ClientController {

    private final TicketService ticketService;
    private final DepartmentService departmentService;
    private final UserService userService;
    private final CommentService commentService;

    @PostMapping("/createTicket")
    public ResponseEntity<?> addTicket(@RequestBody Ticket ticket){

        Long check = ticketService.addTicket(ticket);
        if(check != -1L){
            return ResponseEntity.status(HttpStatus.CREATED).body(check);
        }
        return ResponseEntity.badRequest().body("Ticket has not been added");
    }

    @PostMapping("/createSecurityTicket")
    public ResponseEntity<?> addSecurityTicket(TicketSecurity ticket){

        Long check = ticketService.addTicketSecurity(ticket);
        if(check != -1L){
            return ResponseEntity.status(HttpStatus.CREATED).body(check);
        }
        return ResponseEntity.badRequest().body("Ticket has not been added");
    }

    @GetMapping("/getAllMyTickets")
    public ResponseEntity<Page<Ticket>> getMyTickets(@PageableDefault Pageable pageable){
        Page<Ticket> tickets = ticketService.getAllTickets(pageable);
        if(tickets != null){
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getAllDepartments")
    public List<Department> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

    @PostMapping("/setOwnDepartment")
    public ResponseEntity<?> setOwnDepartment(@RequestBody DepartmentRequest departmentRequest){
        boolean check = userService.setOwnDepartment(departmentRequest.getDepartmentName());
        if(check){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("You are already in a department or this department does not exists");
    }


    @PostMapping("/addCommentToTicket")
    public ResponseEntity<?> addCommentToTicket(@RequestParam("file") Optional<MultipartFile> file, @RequestParam("comment") String comment, @RequestParam("ticketId") Long id) throws IOException {
        int returnCode = commentService.addCommentToTicket(file, comment, id);

        if(returnCode == 0){
            return ResponseEntity.ok().body("File was uploaded");
        }

        if(returnCode == 1){
            return ResponseEntity.badRequest().body("No ticket with such id");
        }
        if(returnCode == 2){
            return ResponseEntity.badRequest().body("Not allowed to upload this kind of file");
        }
        if(returnCode == 3){
            return ResponseEntity.badRequest().body("Not allowed to comment to this ticket. You are not admin/ticket's creator/ticket's assignee");
        }

        return ResponseEntity.badRequest().body("Unexpected event");
    }

    @GetMapping("/getVIP")
    public ResponseEntity<?> getVIP(){
        String coins = userService.getVIP();
        return ResponseEntity.ok().body(coins);
    }


    @GetMapping("/myRole")
    public ResponseEntity<?> getRole(){
        String role = userService.getRole();
        if(role.isEmpty()){
            return ResponseEntity.badRequest().body("Something went wrong while getting user's role");
        }
        return ResponseEntity.ok().body(role);
    }


    // I am not supposed to see John's tickets, or am I ?
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


    // I am not supposed to see John's comments, or am I ?
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
