package samryong.report.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import samryong.report.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findById(Long id);
}
