package org.sid.ebankbackend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor

public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String name;
    private String email;
    @OneToMany(mappedBy = "customer") //MappedBy pour relier cette relation avec ManyToOne dans BankAccount (dire a jpa qu'il s'agit d'une seul cle etranger)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<BankAccount> bankAccounts;


}
