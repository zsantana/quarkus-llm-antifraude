package br.com.xbinario.antifraude.service;

import java.time.temporal.ChronoUnit;

import org.eclipse.microprofile.faulttolerance.Timeout;

import br.com.xbinario.antifraude.repository.CustomerRepository;
import br.com.xbinario.antifraude.repository.TransactionRepository;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(tools = { TransactionRepository.class, CustomerRepository.class, EmailService.class })
public interface DistanceFraudDetectionAiService {

    // 
    
    @SystemMessage("""
                Você é uma IA de detecção de fraude em contas bancárias. Você tem que detectar fraudes nas transações.
            """)
    @UserMessage("""
        Detecte fraudes com base na distância entre duas transações do cliente: {{customerId}}.

        Para detectar uma fraude, execute as seguintes ações:
        1 - Primeiro, recupere o nome do cliente {{customerId}}
        2 - Recupere as transações do cliente {{customerId}} dos últimos 15 minutos.
        3 - Recupere a cidade de cada transação pelo campo 'city'.
        4 - Verifique se a distância entre as duas cidades é **SUPERIOR** a 200 km por via terrestre, caso positivo é detectada uma fraude.
        5 - Caso seja detectada alguma fraude, encontre as duas transações associadas a essas cidades.

        Responda com um **único** documento JSON contendo:
        - o nome do cliente na chave 'customer-name'
        - o valor da primeira transação na chave 'first-amount'
        - o valor da segunda transação na chave 'second-amount'
        - a cidade da primeira transação na chave 'first-city'
        - a cidade da segunda transação na chave 'second-city'
        - a chave 'fraud' definida para um valor booleano que indica se uma fraude foi detectada (portanto, a distância é superior a 200 km)
        - a chave 'distance' definida para a distância entre as duas cidades
        - a chave 'explanation' contendo uma explicação da sua resposta.
        - a chave 'cities' contendo todas as cidades para as transações do cliente {{customerId}} nos últimos 15 minutos.
        - se houver fraude, a chave 'email' contendo um email para o cliente {{customerId}} para avisá-lo sobre a fraude. 
        - se houver fraude, a chave 'texto_email' O texto deve ser formal e educado. Deve pedir ao cliente que entre em contato com o banco o mais rápido possível.

        Sua resposta deve ser apenas o documento JSON bruto, sem ```json, ``` ou qualquer outra coisa.

            """)
    @Timeout(value = 1, unit = ChronoUnit.MINUTES)
    String detectDistanceFraudForCustomer(long customerId);

}
