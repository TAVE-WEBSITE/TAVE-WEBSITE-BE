package com.tave.tavewebsite.domain.apply.dashboard.repository;

import com.tave.tavewebsite.domain.apply.dashboard.entity.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
}
