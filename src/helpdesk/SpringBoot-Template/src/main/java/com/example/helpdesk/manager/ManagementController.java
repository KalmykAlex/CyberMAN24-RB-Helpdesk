package com.example.helpdesk.manager;

import com.example.helpdesk.ticket.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api/v1/management")
@AllArgsConstructor
public class ManagementController {

    private final TicketService ticketService;

    @GetMapping
    public String get() {
        return "GET:: management controller";
    }
    @PostMapping
    public String post() {
        return "POST:: management controller";
    }

    @PostMapping("/takeTicketInWork")
    public ResponseEntity<?> post(Long id) {
        boolean check = ticketService.takeTicketInWork(id);
        if(check){
            return ResponseEntity.ok().body(check);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(check);
    }

    @PostMapping("/solveTicket")
    public ResponseEntity<?> solveTicket(Long id){
        Integer check = ticketService.solveTicket(id);
        if(check == 0){
            return ResponseEntity.ok().build();
        }

        if(check == 1){
            return ResponseEntity.badRequest().body("You are not assignet to this ticket");
        }

        if(check == 2){
            return ResponseEntity.badRequest().body("You are not assignet to this ticket");
        }

        return ResponseEntity.badRequest().body("Unexpected error");
    }


    @GetMapping("/getAsignedTickets")
    public ResponseEntity<?> getAsignedTickets(){
        return ResponseEntity.ok().body(ticketService.getAsignedTickets());
    }
}