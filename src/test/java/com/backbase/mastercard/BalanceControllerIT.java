package com.backbase.mastercard;

import static com.backbase.mastercard.util.RequestUtils.CLAIM_ASPSP_ID;
import static com.backbase.mastercard.util.RequestUtils.CLAIM_CONSENT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.buildingblocks.common.HttpCommunicationConstants;
import com.backbase.buildingblocks.jwt.internal.token.InternalJwtClaimsSet;
import com.backbase.buildingblocks.test.http.TestRestTemplateConfiguration;
import com.backbase.buildingblocks.testutils.TestTokenUtil;
import com.backbase.buildingblocks.testutils.TestTokenUtil.TestTokenBuilder;
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
        InternalJwtClaimsSet jwt = new TestTokenBuilder()
            .withClaim(CLAIM_ASPSP_ID, "420e5cff-0e2a-4156-991a-f6eeef0478cf")
            .withClaim(CLAIM_CONSENT_ID, "GFiTpF3:EBy5xGqQMatk")
            .build();
        String internalJwt = TestTokenUtil.encode(jwt);
        String arrangementId = "aa:q648383844dhhfHhTV";

        MvcResult result =
            mvc.perform(get("/service-api/v2/balances")
                    .param("arrangementIds", arrangementId)
                    .header(HttpHeaders.AUTHORIZATION, TestRestTemplateConfiguration.TEST_SERVICE_TOKEN)
                    .header(HttpCommunicationConstants.X_CXT_USER_TOKEN, "Bearer " + internalJwt))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody).as("ID in response should match ID in request").contains(arrangementId);
    }
}
