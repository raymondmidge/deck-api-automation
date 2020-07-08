package com.example;

import com.example.models.CreateDeckResponse;
import com.example.utils.HttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.example.Constants.CREATE_DECK_API_ENDPOINT;
import static org.junit.jupiter.api.Assertions.*;

class CreateDeckTests extends BaseRunner {

    @Test
    @DisplayName("create a new deck (happy path)")
    void createNewDeck(){
        CreateDeckResponse response = callGetApi();
        assertTrue(response.getSuccess());
        assertNotNull(response.getDeck_id());
        assertNotNull(response.getRemaining());
        assertNotNull(response.getShuffled());
    }

    @Test
    @DisplayName("verify new deck has 52 cards remaining")
    void verifyFiftyTwoCardsRemain(){
        CreateDeckResponse response = callGetApi();
        assertEquals(52, response.getRemaining(), () -> String.format("expected 52 cards to be returned from fresh deck, but found %s remaining", response.getRemaining()));
    }

    @Test
    @DisplayName("verify that jokers can be included")
    void verifyJokersCanBeIncluded(){
        CreateDeckResponse response = callPostApi(getQueryParamsForIncludingJoker());
        assertTrue(response.getSuccess());
        assertEquals(54, response.getRemaining(), () -> String.format("if jokers are provided in the deck, then the card count should be 54, but found %s instead", response.getRemaining()));
    }


    private CreateDeckResponse callGetApi(){
        HttpResponse response = httpUtil.get(CREATE_DECK_API_ENDPOINT);
        return gson.fromJson(response.getContent(), CreateDeckResponse.class);
    }

    private CreateDeckResponse callPostApi(Map<String,String> queryParams){
        HttpResponse response = httpUtil.get(CREATE_DECK_API_ENDPOINT, queryParams);
        return gson.fromJson(response.getContent(), CreateDeckResponse.class);
    }

    private Map<String,String> getQueryParamsForIncludingJoker(){
        Map<String,String> params = new HashMap<>();
        params.put("jokers_enabled","true");
        return params;
    }
}
