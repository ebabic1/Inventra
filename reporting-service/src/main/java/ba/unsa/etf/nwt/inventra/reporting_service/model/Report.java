package ba.unsa.etf.nwt.inventra.reporting_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate generatedAt;
    private Long userId;

    @OneToMany(mappedBy = "report")
    private List<OrderReport> orderReports;
}