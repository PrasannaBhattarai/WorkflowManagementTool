package com.project.workflow.repository;
import com.project.workflow.models.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
}