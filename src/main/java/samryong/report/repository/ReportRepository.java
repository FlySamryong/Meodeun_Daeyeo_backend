package samryong.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samryong.report.domain.Report;

import java.util.Optional;

public interface ReportRepository extends JpaRepository <Report, Long> {
    Optional<Report> findById(Long id);
}
