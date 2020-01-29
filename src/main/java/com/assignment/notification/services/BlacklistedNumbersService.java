package com.assignment.notification.services;

import com.assignment.notification.exceptions.PhoneNumberNotFoundException;
import com.assignment.notification.models.dto.AddBlacklistResponseDto;
import com.assignment.notification.models.dto.BlacklistedNumberDto;
import com.assignment.notification.models.entities.BlockedNumbers;
import com.assignment.notification.repositories.BlockedNumbersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import static com.assignment.notification.constants.NotificationConstants.RedisConstants.BLACKLISTED_KEY;

@Service
@Slf4j
public class BlacklistedNumbersService {


    @Autowired
    private  Jedis jedis ;

    @Autowired
    BlockedNumbersRepository blockedNumbersRepository;

    public AddBlacklistResponseDto addBlacklistNumbers(BlacklistedNumberDto blacklistedNumberDto) {

        ArrayList<String> blacklistedNumbers = new ArrayList<>(blacklistedNumberDto.getPhoneNumbers());
        long flag = jedis.sadd(BLACKLISTED_KEY, blacklistedNumbers.stream().toArray(String[]::new));
        if (flag > 0) {
            blockedNumbersRepository.saveAll(blacklistedNumberDto.getPhoneNumbers().stream().map( t -> new BlockedNumbers(t)).collect(Collectors.toSet()));
            return new AddBlacklistResponseDto("successfully blacklisted");
        } else {
            throw new RuntimeException();
        }

    }

    public BlacklistedNumberDto getBlacklistedNumbers() {
        log.debug(String.format("-------- > getBlacklistedNumbers executing....."));
        Set<String> phone_numbers = jedis.smembers("blacklist");
        BlacklistedNumberDto blacklistedNumberDto = new BlacklistedNumberDto(phone_numbers);
        return blacklistedNumberDto;
    }


    public void deletePhone_number(BlacklistedNumberDto blacklistedNumberDto) throws PhoneNumberNotFoundException {
        log.debug(String.format("-------- > deletePhone_number executing....."));
        Set<String> blockedNumbers = blacklistedNumberDto.getPhoneNumbers();
        for (String number : blockedNumbers) {
            try {
                blockedNumbersRepository.deleteById(number);
                jedis.srem(BLACKLISTED_KEY, number);
            } catch (Exception ex) {
                log.error(" ******-------  exception occurred ! --------******");
                throw new PhoneNumberNotFoundException("phone_number not present", 404);
            }

        }
    }


}
