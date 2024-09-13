package samryong.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl extends ReportService{
    private final ReportService reportService;
}
