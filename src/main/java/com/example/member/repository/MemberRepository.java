package com.example.member.repository;

import com.example.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

     Optional<Member> findByEmail(String email);

}
