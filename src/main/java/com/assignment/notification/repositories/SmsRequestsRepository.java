package com.assignment.notification.repositories;

import com.assignment.notification.models.entities.SmsRequests;
import org.springframework.data.repository.CrudRepository;

public interface SmsRequestsRepository extends CrudRepository<SmsRequests, Integer> {

}
