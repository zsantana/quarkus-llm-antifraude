package br.com.xbinario.antifraude.repository;

import java.time.LocalDateTime;
import java.util.List;

import br.com.xbinario.antifraude.entity.Transaction;
import jakarta.enterprise.context.ApplicationScoped;

import dev.langchain4j.agent.tool.Tool;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class TransactionRepository implements PanacheRepository<Transaction> {

    @Tool("Obtenha a transação de um determinado cliente nos últimos 15 minutos")
    public List<Transaction> getTransactionsForCustomer(long customerId) {
        return find("customerId = ?1 and time > ?2 order by time desc", customerId, LocalDateTime.now().minusMinutes(15))
            .page(0, 2)    
            .list();
    }

    public double getAmountForCustomer(long customerId) {
        return find("customerId = ?1 and time > ?2", customerId, LocalDateTime.now().minusMinutes(15))
                .stream().mapToDouble(t -> t.amount).sum();
    }

    // @Tool("Obtenha a cidade para um determinado ID de transação")
    // public String getCityForTransaction(long transactionId) {
    //     return find("id = ?1", transactionId).firstResult().city;
    // }

}
