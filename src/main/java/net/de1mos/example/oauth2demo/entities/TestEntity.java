package net.de1mos.example.oauth2demo.entities;

import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "strange")
@Data
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(name="some_uuid")
    private UUID someUuid;

    @Column(name = "some_cidr")
    @ColumnTransformer(read="some_cidr", write="CAST(? AS cidr)")
    private String someCidr;

    @Column(name = "some_json")
    @ColumnTransformer(read="some_json", write="CAST(? AS json)")
    private String someJson;
}
