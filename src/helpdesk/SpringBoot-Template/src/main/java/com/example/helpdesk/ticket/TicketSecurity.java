package com.example.helpdesk.ticket;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@DiscriminatorValue("TicketSecurity")
@Data
public class TicketSecurity extends Ticket{
    private String threatLevel;
}
