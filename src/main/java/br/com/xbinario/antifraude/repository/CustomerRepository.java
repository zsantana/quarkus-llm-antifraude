package br.com.xbinario.antifraude.repository;

import jakarta.enterprise.context.ApplicationScoped;
import br.com.xbinario.antifraude.entity.Customer;
import dev.langchain4j.agent.tool.Tool;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {

    @Tool("obtenha o nome do cliente para o customerId fornecido")
    public String getCustomerName(long id) {
        return find("id", id).firstResult().name;
    }

}
