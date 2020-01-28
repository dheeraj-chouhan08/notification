package com.assignment.notification.repositories;

import com.assignment.notification.entities.SmsRequests;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SmsRequestsRepository extends CrudRepository<SmsRequests, Integer> {

}
