package com.assignment.notification.controller;

import com.assignment.notification.exceptions.PhoneNumberNotFoundException;
import com.assignment.notification.dto.BlacklistedNumberDto;
import com.assignment.notification.exceptions.RecordNotFoundException;
import com.assignment.notification.services.BlacklistedNumbersService;
import org.slf4j.Logger;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/v1")
public class BlacklistController {
    private  static  final Logger logger =  LoggerFactory.getLogger(BlacklistController.class);

    @Autowired
    BlacklistedNumbersService blacklistedNumbersService;

  /* ***************  for adding phone_Numbers to blacklist ************ */

    @RequestMapping(path = "/blacklist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HashMap<String, String>> addBlacklistNumber(@RequestBody BlacklistedNumberDto blacklistedNumberDto){

      try {
          return new ResponseEntity<HashMap<String, String>>(blacklistedNumbersService.addBlacklistNumbers(blacklistedNumberDto), HttpStatus.OK);
      }
      catch (Exception e){
          logger.error("---------------**** error in adding phone_numbers to blacklist ****-----------");
          return ResponseEntity.badRequest().build();
      }
    }


    /* *************** for getting blacklisted phone_Numbers ************ */

    @RequestMapping(path = "/blacklist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BlacklistedNumberDto> getBlacklistedNumbers() throws RecordNotFoundException {
        try {
            return blacklistedNumbersService.getBlacklistedNumbers();
        }
        catch (Exception e){
            throw new RecordNotFoundException ();
        }
    }

    /* *************** for deleting phone_Numbers from blacklist ************ */

    @RequestMapping(path = "/blacklist", method = RequestMethod.DELETE, produces =  MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteNumber(@RequestBody BlacklistedNumberDto blacklistedNumberDto) throws PhoneNumberNotFoundException {
        try {
            logger.info("trying to delete phone_number");
            blacklistedNumbersService.deletePhone_number(blacklistedNumberDto);
        }
        catch(Exception e){
            throw new PhoneNumberNotFoundException( );
        }
    }
}
