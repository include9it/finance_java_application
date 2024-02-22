package io.include9it.finance_java_application.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.include9it.finance_java_application.dto.Account;
import io.include9it.finance_java_application.service.AccountService;
import io.include9it.finance_java_application.util.Constants;
import io.include9it.finance_java_application.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {
    private static final String ACCOUNTS_PATH = "/api/accounts/{clientId}";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountService service;

    @Test
    void testReturnAccountList() throws Exception {
        var responseJson = TestUtil.readResourceFile("response/account_list_response.json");
        List<Account> response = objectMapper.readValue(responseJson, new TypeReference<List<Account>>() {
        });

        when(service.getAccountList(Constants.CLIENT_ID)).thenReturn(response);

        var request = get(ACCOUNTS_PATH, Constants.CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].currency", notNullValue()))
                .andExpect(jsonPath("$[0].balance", notNullValue()))
                .andExpect(jsonPath("$[1].id", notNullValue()))
                .andExpect(jsonPath("$[1].currency", notNullValue()))
                .andExpect(jsonPath("$[1].balance", notNullValue()));

    }
}
