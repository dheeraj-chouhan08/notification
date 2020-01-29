package com.assignment.notification.controller;

import com.assignment.notification.exceptions.PhoneNumberNotFoundException;
import com.assignment.notification.exceptions.RecordNotFoundException;
import com.assignment.notification.models.dto.AddBlacklistResponseDto;
import com.assignment.notification.models.dto.BlacklistedNumberDto;
import com.assignment.notification.services.BlacklistedNumbersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/v1")
public class BlacklistController {

    @Autowired
    BlacklistedNumbersService blacklistedNumbersService;

    /* ***************  for adding phoneNumbers to blacklist ************ */

    @RequestMapping(path = "/blacklist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddBlacklistResponseDto> addBlacklistNumber(@RequestBody BlacklistedNumberDto blacklistedNumberDto) {

        try {
            AddBlacklistResponseDto addBlacklistResponseDto = blacklistedNumbersService.addBlacklistNumbers(blacklistedNumberDto);
            return new ResponseEntity<AddBlacklistResponseDto>(addBlacklistResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("---------------**** error in adding phone_numbers to blacklist ****-----------");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /* *************** for getting blacklisted phoneNumbers ************ */

    @RequestMapping(path = "/blacklist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BlacklistedNumberDto> getBlacklistedNumbers() throws RecordNotFoundException {
        try {
            BlacklistedNumberDto blacklistedNumberDto = blacklistedNumbersService.getBlacklistedNumbers();
            return new ResponseEntity<BlacklistedNumberDto>(blacklistedNumberDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("---------------**** error in retrieving  blacklisted numbers ****-----------");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /* *************** for deleting phone_Numbers from blacklist ************ */

    @RequestMapping(path = "/blacklist", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteNumber(@RequestBody BlacklistedNumberDto blacklistedNumberDto) throws PhoneNumberNotFoundException {
        try {
            blacklistedNumbersService.deletePhone_number(blacklistedNumberDto);
        } catch (Exception e) {
            log.error("--------------*** error in deleting phoneNumbers from blacklist ***------------");
            throw new PhoneNumberNotFoundException();
        }
    }
}
