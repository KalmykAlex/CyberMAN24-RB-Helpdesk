package com.example.helpdesk.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketSecurityRepository extends JpaRepository<TicketSecurity, Long> {
}
