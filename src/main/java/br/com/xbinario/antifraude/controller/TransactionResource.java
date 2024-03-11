package br.com.xbinario.antifraude.controller;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import org.jboss.resteasy.reactive.RestQuery;

import br.com.xbinario.antifraude.entity.Transaction;
import br.com.xbinario.antifraude.repository.TransactionRepository;

@Path("/transactions")
public class TransactionResource {

    private final TransactionRepository transactions;

    public TransactionResource(TransactionRepository transactions) {
        this.transactions = transactions;
    }

    
    @GET
    public List<Transaction> list(@RestQuery long customerId) {
        return transactions.getTransactionsForCustomer(customerId);
    }

    @POST
    @Transactional
    public void create(Transaction transaction) {
        transaction.time = LocalDateTime.now();
        transactions.persist(transaction);
    }
}
