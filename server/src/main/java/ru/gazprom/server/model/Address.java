package ru.gazprom.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gazprom.server.enums.CityZone;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses")
public class Address extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CityZone cityZone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
