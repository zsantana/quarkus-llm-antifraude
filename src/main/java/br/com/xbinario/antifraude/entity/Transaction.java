package br.com.xbinario.antifraude.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    public Long id;

    public double amount;

    public long customerId;

    public String city;

    public LocalDateTime time;

}
