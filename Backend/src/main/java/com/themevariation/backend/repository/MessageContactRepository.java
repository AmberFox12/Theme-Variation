package com.themevariation.backend.repository;

import com.themevariation.backend.model.MessageContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageContactRepository extends JpaRepository<MessageContact, Long> {
    List<MessageContact> findAllByOrderByEnvoyeLeDesc();
}
