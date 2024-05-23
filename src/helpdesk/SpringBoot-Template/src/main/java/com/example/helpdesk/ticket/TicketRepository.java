package com.example.helpdesk.ticket;

import com.example.helpdesk.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Page<Ticket> findByUserCreator(User userCreator, Pageable pageable);

    List<Ticket> findByUserAssignee(User user);
}
