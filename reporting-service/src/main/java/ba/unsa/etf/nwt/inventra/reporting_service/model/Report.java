package ba.unsa.etf.nwt.inventra.reporting_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    @JsonIgnore
    @OneToMany(mappedBy = "report", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderReport> orderReports;
}