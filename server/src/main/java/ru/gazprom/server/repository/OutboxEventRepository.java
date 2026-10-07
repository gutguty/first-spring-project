package ru.gazprom.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.gazprom.server.model.OutboxEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    @Query("SELECT e FROM OutboxEvent e" +
            " WHERE e.status IN :statuses" +
            " AND e.availableAt <= :currentTime" +
            " ORDER BY e.createdAt")
    List<OutboxEvent> findReadyToPublish(@Param("statuses") List<String> statuses, @Param("currentTime") LocalDateTime currentTime);}
