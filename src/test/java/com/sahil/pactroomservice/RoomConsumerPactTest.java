
package com.sahil.pactroomservice;

import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "RoomService", port = "0")
class RoomConsumerPactTest {

   
@Pact(consumer = "RoomWebApp", provider = "RoomService")
public V4Pact getRoom(PactBuilder builder) {
    return builder
            .usingLegacyDsl()
            .given("room room-123 exists")
            .uponReceiving("a request to get room room-123")
            .path("/rooms/room-123")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(java.util.Map.of(
                    "Content-Type", "application/json"))
            .body("""
                    {
                      "id": "room-123",
                      "name": "Engineering Standup",
                      "status": "ACTIVE"
                    }
                    """)
            .toPact(V4Pact.class);
}


    @Test
    @PactTestFor(pactMethod = "getRoom")
    void consumerCanGetRoom(MockServer mockServer) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        mockServer.getUrl() + "/rooms/room-123"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("room-123",
                response.body().contains("\"id\": \"room-123\"")
                        ? "room-123" : "unexpected");
    }
}
