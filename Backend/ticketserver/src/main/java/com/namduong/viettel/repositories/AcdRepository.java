package com.namduong.viettel.repositories;

import com.namduong.viettel.models.Acd;
import com.namduong.viettel.models.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AcdRepository extends JpaRepository<Acd, String> {
    @Query(nativeQuery = true,value = "select * from ticket.acd where current_convs = (select min(current_convs) from ticket.acd) limit 1")
    Acd findFirstOrderByCurrentConversationsDesc();

    Acd findByAgent(Agent agent);
}
