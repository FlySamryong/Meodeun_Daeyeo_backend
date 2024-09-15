package samryong.domain.report.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import samryong.domain.report.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findById(Long id);
}
