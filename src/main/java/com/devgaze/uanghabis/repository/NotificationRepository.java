package com.devgaze.uanghabis.repository;

import com.devgaze.uanghabis.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
