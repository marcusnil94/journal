package com.journal.journal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface JournalRepository extends CrudRepository<Journal, Integer> {
    List<Journal> findAllByOrderByDateDesc();
    
}
