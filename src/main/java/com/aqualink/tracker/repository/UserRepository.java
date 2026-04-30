package com.aqualink.tracker.repository;

import com.aqualink.tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhone(String phone);

    Optional<User> findByPhoneAndVendorId(String phone, Long vendorId);
}