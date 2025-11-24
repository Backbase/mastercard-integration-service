package com.backbase.mastercard.rest;

import com.backbase.buildingblocks.backend.security.accesscontrol.accesscontrol.AccessControlValidatorImpl;
import com.backbase.buildingblocks.testutils.TestTokenUtil;
import com.backbase.cards.client.models.CardRequestPost;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("it")
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
class CardsControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccessControlValidatorImpl accessControlValidator;

    @Test
    void postCardAuthenticatedTest() throws Exception {
        when(accessControlValidator.checkPermissions("Personal Finance Management", "Manage Cards", "create")).thenReturn(true);

        var internalJwt = TestTokenUtil.encode(new TestTokenUtil.TestTokenBuilder().user().build());

        // Create the CardRequestPost object using builder pattern
        CardRequestPost cardRequestPost = new CardRequestPost("card-product-token-xyz")
                .arrangementId("12345678-1234-1234-1234-123456789012");

        // Serialize to JSON using ObjectMapper
        String payload = objectMapper.writeValueAsString(cardRequestPost);

        mvc.perform(post("/client-api/v2/cards")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + internalJwt))
                .andExpect(status().isOk());
    }

    @Test
    void postCardWithoutAuthenticationTest() throws Exception {
        // Create the CardRequestPost object using builder pattern
        CardRequestPost cardRequestPost = new CardRequestPost("card-product-token-xyz")
                .arrangementId("12345678-1234-1234-1234-123456789012");

        // Serialize to JSON using ObjectMapper
        String payload = objectMapper.writeValueAsString(cardRequestPost);

        mvc.perform(post("/client-api/v2/cards")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
