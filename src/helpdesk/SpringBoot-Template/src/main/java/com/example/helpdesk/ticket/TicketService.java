package com.example.helpdesk.ticket;

import com.example.helpdesk.comment.Comment;
import com.example.helpdesk.user.Role;
import com.example.helpdesk.user.User;
import com.example.helpdesk.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.example.helpdesk.user.Role.MANAGER;

@Service
@AllArgsConstructor
public class TicketService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketSecurityRepository ticketSecurityRepository;


    public Long addTicket(Ticket ticket){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> temp = userRepository.findByEmail(userDetails.getUsername());
        if(temp.isPresent()){
            ticket.setUserCreator(temp.get());
            ticket.setStatus(TicketStatus.NEW);

            var ticketTemp = ticketRepository.save(ticket);
            if(ticketTemp != null){
                return ticketTemp.getIdTicket();
            }
        }
        return -1L;
    }

    public Long addTicketSecurity(TicketSecurity ticket){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> temp = userRepository.findByEmail(userDetails.getUsername());
        if(temp.isPresent()){
            ticket.setUserCreator(temp.get());
            ticket.setStatus(TicketStatus.NEW);
            
            var ticketTemp = ticketSecurityRepository.save(ticket);
            if(ticketTemp != null){
                return ticketTemp.getIdTicket();
            }
        }
        return -1L;
    }

    public boolean takeTicketInWork(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userTemp = userRepository.findByEmail(userDetails.getUsername());
        Optional<Ticket> ticketTemp;
        if(userTemp.isPresent()) {
            ticketTemp = ticketRepository.findById(id);
        }else {
            return false;
        }

        if(ticketTemp.isPresent()){
            var ticket = ticketTemp.get();

            if(ticket.getUserAssignee() != null){
                return false;
            }
            ticket.setStatus(TicketStatus.IN_PROGRESS);
            ticket.setUserAssignee(userTemp.get());
            ticketRepository.save(ticket);
            return true;
        }
        return false;
    }

    public Page<Ticket> getAllTickets(Pageable pageable) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userTemp = userRepository.findByEmail(userDetails.getUsername());
        if(userTemp.isPresent()) {

            Page<Ticket> tmp = ticketRepository.findByUserCreator(userTemp.get(),pageable);
            return tmp;
        }
        return null;
    }

    public Integer assignTicketToManager(String username, Long id) {
        Optional<User> tmpUser = userRepository.findByEmail(username);
        if(tmpUser.isEmpty()){
            return 1;
        }

        Optional<Ticket> tmpTicket = ticketRepository.findById(id);
        if(tmpTicket.isEmpty()){
            return 2;
        }

        if(tmpUser.get().getRole() != MANAGER) {
            return 3;
        }

        Ticket ticketTmp = tmpTicket.get();
        ticketTmp.setStatus(TicketStatus.IN_PROGRESS);
        ticketTmp.setUserAssignee(tmpUser.get());
        ticketRepository.save(ticketTmp);
        return 0;
    }

    public Integer solveTicket(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userTemp = userRepository.findByEmail(userDetails.getUsername());
        if(userTemp.isEmpty()){
            return -1;
        }

        Optional<Ticket> ticketTmp = ticketRepository.findById(id);
        if(ticketTmp.isEmpty()){
            return 2;
        }

        Ticket ticket = ticketTmp.get();
        if(ticket.getUserAssignee() != null){
            if(!ticket.getUserAssignee().getUsername().equals(userTemp.get().getUsername())){
                return 1;
            }
        }

        ticket.setStatus(TicketStatus.DONE);
        ticketRepository.save(ticket);
        return 0;
    }

    
    public List<Ticket> getAsignedTickets() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userTemp = userRepository.findByEmail(userDetails.getUsername());
        if(userTemp.isPresent()) {
            return ticketRepository.findByUserAssignee(userTemp.get());
        }
        return null;
    }


    public Optional<Ticket> getTicket(Long id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        return optionalTicket;
    }


    public List<Ticket> getAllTickets(){
        return ticketRepository.findAll();
    }


}
