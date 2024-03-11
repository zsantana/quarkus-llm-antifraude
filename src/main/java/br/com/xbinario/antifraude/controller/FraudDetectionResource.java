package br.com.xbinario.antifraude.controller;

import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.jboss.resteasy.reactive.RestQuery;

import br.com.xbinario.antifraude.entity.Transaction;
import br.com.xbinario.antifraude.repository.TransactionRepository;
import br.com.xbinario.antifraude.service.AmountFraudDetectionAiService;
import br.com.xbinario.antifraude.service.DistanceFraudDetectionAiService;

@Path("/fraud")
public class FraudDetectionResource {

    private final DistanceFraudDetectionAiService distanceFraudDetectionAiService;
    private final AmountFraudDetectionAiService amountFraudDetectionAiService;
    private final TransactionRepository transactions;

    public FraudDetectionResource(DistanceFraudDetectionAiService distanceFraudDetectionAiService,
                                  AmountFraudDetectionAiService amountFraudDetectionAiService,
                                  TransactionRepository transactions) {

        this.distanceFraudDetectionAiService = distanceFraudDetectionAiService;
        this.amountFraudDetectionAiService = amountFraudDetectionAiService;
        this.transactions = transactions;
    }

    @GET
    @Path("/distance")
    public String detectBasedOnDistance(@RestQuery long customerId) {
        return distanceFraudDetectionAiService.detectDistanceFraudForCustomer(customerId);
    }

    @GET
    @Path("/amount")
    public String detectBaseOnAmount(@RestQuery long customerId) {
        return amountFraudDetectionAiService.detectAmountFraudForCustomer(customerId);
    }

    @GET
    @Path("/transactions")
    public List<Transaction> list(@RestQuery long customerId) {
        return transactions.getTransactionsForCustomer(customerId);
    }

    @GET
    @Path("/verification")
    public double verify(@RestQuery long customerId) {
        return transactions.getAmountForCustomer(customerId);
    }
}
