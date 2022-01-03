package com.codefactory.accountapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Data
@Entity
@With
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long amount;

    private Instant dateTime;

    @ManyToOne
    @JoinColumn(name = "transfer_from_id")
    private Account transferFrom;

    @ManyToOne
    @JoinColumn(name = "transfer_to_id")
    private Account transferTo;

}
