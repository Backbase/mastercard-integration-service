package com.backbase.mastercard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.backbase.buildingblocks.test.http.TestRestTemplateConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@ActiveProfiles("it")
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
public class BalanceControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getBalancesTest() throws Exception {
        String arrangementId = "aa:q648383844dhhfHhTV";
        MvcResult result =
            mvc.perform(get("/service-api/v2/balances")
                    .param("arrangementIds", arrangementId)
                    .header(AUTHORIZATION, TestRestTemplateConfiguration.TEST_SERVICE_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody).as("ID in response should match ID in request").contains(arrangementId);
    }
}
