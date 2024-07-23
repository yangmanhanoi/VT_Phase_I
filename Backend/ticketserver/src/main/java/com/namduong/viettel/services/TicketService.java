package com.namduong.viettel.services;

import com.namduong.viettel.dtos.TicketPayload;
import com.namduong.viettel.models.Ticket;

public interface TicketService {
    Ticket saveData(TicketPayload payload);
}
