package br.com.xbinario.antifraude.config;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import br.com.xbinario.antifraude.entity.Customer;
import br.com.xbinario.antifraude.entity.Transaction;
import br.com.xbinario.antifraude.repository.CustomerRepository;
import br.com.xbinario.antifraude.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class Setup {

    public static List<String> CITIES = List.of(
        "São Paulo", 
        "Campinas", 
        "Guarulhos", 
        "São Bernardo do Campo", 
        "Santo André", 
        "São José dos Campos", 
        "Ribeirão Preto", 
        "Sorocaba", 
        "Santos", 
        "Osasco", 
        "Mogi das Cruzes", 
        "Jundiaí", 
        "São José do Rio Preto", 
        "Piracicaba", 
        "Bauru", 
        "Taubaté", 
        "Barueri", 
        "Diadema", 
        "Carapicuíba", 
        "Itaquaquecetuba");

    public static String getARandomCity() {
        return CITIES.get(new Random().nextInt(CITIES.size()));
    }

    @Transactional
    public void init(@Observes StartupEvent ev, CustomerRepository customers, TransactionRepository transactions) {
        Random random = new Random();
        if (customers.count() == 0) {
            var customer1 = new Customer();
            customer1.name = "Reinaldo Santana";
            customer1.email = "reinaldojsantana@mail.com";
            customers.persist(customer1);

            var customer2 = new Customer();
            customer2.name = "João da Silva";
            customer2.email = "joao.silva@mail.com";
            customers.persist(customer2);

            var customer3 = new Customer();
            customer3.name = "Maria das graças";
            customer3.email = "maria.gracas@mail.com";
            customers.persist(customer3);
        }

        transactions.deleteAll(); // Delete all transactions
        for (int i = 0; i < 100; i++) {
            var transaction = new Transaction();
            // Get a random customer
            var idx = random.nextInt((int) customers.count());
            transaction.customerId = customers.findAll().page(idx, 1).firstResult().id;
            transaction.amount = random.nextInt(1000) + 1;
            transaction.time = LocalDateTime.now().minusMinutes(random.nextInt(20));
            transaction.city = getARandomCity();
            transactions.persist(transaction);
        }

        for (Customer customer : customers.listAll()) {
            System.out.println("Customer: " + customer.name + " - " + customer.id);
        }
    }
}
