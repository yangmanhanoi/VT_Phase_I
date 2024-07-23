package com.namduong.viettel.repositories;

import com.namduong.viettel.models.Ticket;
import com.namduong.viettel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Ticket findByUser(User user);
}
