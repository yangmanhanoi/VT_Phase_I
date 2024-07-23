package com.namduong.viettel.services.impl;

import com.namduong.viettel.models.Acd;
import com.namduong.viettel.models.Agent;
import com.namduong.viettel.repositories.AcdRepository;
import com.namduong.viettel.services.AcdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AcdServiceImpl extends BaseRedisServiceImpl implements AcdService {
    public AcdServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }
    @Autowired
    private AcdRepository acdRepository;
    @Override
    public Agent findAgent() {
        Optional<Acd> optional = Optional.ofNullable(acdRepository.findFirstOrderByCurrentConversationsDesc());
        if(!optional.isEmpty())
        {
            Acd acd = optional.get();

            return acd.getAgent();
        }
        return null;
    }

    @Override
    public Acd addConvs(Agent agent) {
        Optional<Acd> optional = Optional.ofNullable(acdRepository.findByAgent(agent));
        if(!optional.isEmpty())
        {
            Acd acd = optional.get();
            acd.addConv();
            return acdRepository.save(acd);
        }

        return null;
    }

    @Override
    public Acd decreaseCurrentConv(Agent agent) {
        Optional<Acd> optional = Optional.ofNullable(acdRepository.findByAgent(agent));
        if(!optional.isEmpty())
        {
            Acd acd = optional.get();
            acd.removeConv();
            return acdRepository.save(acd);
        }
        return null;
    }

}
