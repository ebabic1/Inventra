package ba.unsa.etf.nwt.inventra.reporting_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @OneToMany(mappedBy = "report")
    private List<OrderReport> orderReports;
}