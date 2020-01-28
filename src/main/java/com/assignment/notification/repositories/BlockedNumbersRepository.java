package com.assignment.notification.repositories;

import com.assignment.notification.entities.BlockedNumbers;
import org.springframework.data.repository.CrudRepository;

public interface BlockedNumbersRepository extends CrudRepository<BlockedNumbers, String> {
}
