package com.assignment.notification.services;

import com.assignment.notification.exceptions.PhoneNumberNotFoundException;
import com.assignment.notification.dto.BlacklistedNumberDto;
import com.assignment.notification.entities.BlockedNumbers;
import com.assignment.notification.repositories.BlockedNumbersRepository;
import org.slf4j.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Set;

@Service
public class BlacklistedNumbersService {

    private static final Logger logger = LoggerFactory.getLogger(BlacklistedNumbersService.class);

    private Jedis jedis = new Jedis();

    @Autowired
    BlockedNumbersRepository blockedNumbersRepository;


    public HashMap<String, String> addBlacklistNumbers(BlacklistedNumberDto blacklistedNumberDto){

        logger.info(String.format("-------- > addBlacklistNumbers executing....."));

        for(String phone_number : blacklistedNumberDto.getPhone_numbers()){
            jedis.sadd("blacklist", phone_number  );
            BlockedNumbers blockedNumber = new BlockedNumbers(phone_number);
            blockedNumbersRepository.save(blockedNumber);
        }
        HashMap<String, String>  map = new HashMap<>();
        map.put("data", "Successfully blacklisted");
        return map;
    }

    public ResponseEntity<BlacklistedNumberDto> getBlacklistedNumbers(){
        logger.info(String.format("-------- > getBlacklistedNumbers executing....."));
        Set<String> phone_numbers = jedis.smembers("blacklist");
        BlacklistedNumberDto blacklistedNumberDto = new BlacklistedNumberDto(phone_numbers);
        return new ResponseEntity<BlacklistedNumberDto>(blacklistedNumberDto, HttpStatus.OK);
    }


    public void deletePhone_number(BlacklistedNumberDto blacklistedNumberDto) throws PhoneNumberNotFoundException {
        logger.info(String.format("-------- > deletePhone_number executing....."));
        Set<String> blockedNumbers = blacklistedNumberDto.getPhone_numbers();
        for(String number : blockedNumbers){
            //logger.info(blockedNumbersRepository.findById(number).get().toString());
            try {
                blockedNumbersRepository.deleteById(number);
                jedis.srem("blacklist", number);
               // logger.info("phone_number was present");

            }
            catch (Exception ex){
                logger.info("exception occurred !");
                throw new PhoneNumberNotFoundException("phone_number not present", 404);
            }

        }
    }


}
