package com.namduong.viettel.services.impl;

import com.namduong.viettel.models.Agent;
import com.namduong.viettel.repositories.AgentRepository;
import com.namduong.viettel.services.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AgentServieImpl implements AgentService {
    @Autowired
    private AgentRepository agentRepository;
    @Override
    public Agent findById(String id) {
        Optional<Agent> optional = agentRepository.findById(id);
        if(!optional.isEmpty()) return optional.get();
        return null;
    }
}
