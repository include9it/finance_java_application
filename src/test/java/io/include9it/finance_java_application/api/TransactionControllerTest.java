package io.include9it.finance_java_application.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.include9it.finance_java_application.dto.Transaction;
import io.include9it.finance_java_application.dto.TransactionRequest;
import io.include9it.finance_java_application.service.TransactionService;
import io.include9it.finance_java_application.util.Constants;
import io.include9it.finance_java_application.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {
    private static final String TRANSACTIONS_PATH = "/api/transactions";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TransactionService service;

    @Test
    public void testGetTransactionHistory() throws Exception {
        var responseJson = TestUtil.readResourceFile("response/transaction_history_response.json");
        List<Transaction> response = objectMapper.readValue(responseJson, new TypeReference<List<Transaction>>() {
        });

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate"));

        when(service.getTransactionHistory(Constants.ACCOUNT_ID, pageable)).thenReturn(response);

        var request = get(TRANSACTIONS_PATH + "/{accountId}", Constants.ACCOUNT_ID)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    void testReturnBadRequestToEmptyBody() throws Exception {
        var request = post("/api/transactions/process")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testProcessTransactionSuccess() throws Exception {
        var requestJson = TestUtil.readResourceFile("request/process_transaction_request.json");
        TransactionRequest requestModel = objectMapper.readValue(requestJson, TransactionRequest.class);
        ResponseEntity<Void> response = ResponseEntity.ok().build();

        when(service.processTransaction(requestModel)).thenReturn(response);

        var request = post(TRANSACTIONS_PATH + "/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson);

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }
}
