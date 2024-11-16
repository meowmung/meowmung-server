package com.example.member.repository;

import com.example.member.entity.RandomNumber;
import org.springframework.data.repository.CrudRepository;

public interface RandomNumberRepository extends CrudRepository<RandomNumber, String> {

}
