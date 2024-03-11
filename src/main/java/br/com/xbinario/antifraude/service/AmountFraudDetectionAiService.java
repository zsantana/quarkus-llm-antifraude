package br.com.xbinario.antifraude.service;

import java.time.temporal.ChronoUnit;

import org.eclipse.microprofile.faulttolerance.Timeout;

import br.com.xbinario.antifraude.repository.CustomerRepository;
import br.com.xbinario.antifraude.repository.TransactionRepository;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(tools = { TransactionRepository.class, CustomerRepository.class })
public interface AmountFraudDetectionAiService {

    @SystemMessage("""
            You are a bank account fraud detection AI. You have to detect frauds in transactions.
            """)
    @UserMessage("""
             Your task is to detect whether a fraud was committed for the customer {{customerId}}.

             To detect a fraud, perform the following actions:
             1 - Retrieve the name of the customer {{customerId}}
             2 - Retrieve the transactions for the customer {{customerId}} for the last 15 minutes.
             3 - Sum the amount of the all these transactions. Make sure the sum is correct.
             4 - If the amount is greater than 10000, a fraud is detected.

             Answer with a **single** JSON document containing:
             - the customer name in the 'customer-name' key
             - the computed sum in the 'total' key
             - the 'fraud' key set to a boolean value indicating if a fraud was detected
             - the 'transactions' key containing the list of transaction amounts
             - the 'explanation' key containing a explanation of your answer, especially how the sum is computed.
             - if there is a fraud, the 'email' key containing an email to the customer {{customerId}} to warn him about the fraud. The text must be formal and polite. It must ask the customer to contact the bank ASAP.

            Your response must be just the raw JSON document, without ```json, ``` or anything else.
             """)
    @Timeout(value = 2, unit = ChronoUnit.MINUTES)
    String detectAmountFraudForCustomer(long customerId);

}
