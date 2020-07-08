package com.example;

import com.example.models.CreateDeckResponse;
import com.example.models.DrawCardResponse;
import com.example.utils.HttpResponse;
import com.google.gson.Gson;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static com.example.Constants.CREATE_DECK_API_ENDPOINT;
import static com.example.Constants.DRAW_CARD_API_ENDPOINT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DrawCardTests extends BaseRunner {


	@ParameterizedTest(name = "verify that drawing {0} will leave {1} remaining")
    @CsvSource({
            "1,    51",
            "7,    45",
            "52,   0"
    })
	void drawAndVerifyRemainingCardCount(int count, int expectedToRemain) {
        DrawCardResponse response = callDrawCardsApi(count);
        assertEquals(expectedToRemain, response.getRemaining(),
				() -> String.format("expected %s cards to be returned after drawing %s, but found %s", expectedToRemain, count, response.getRemaining()));
	}


	private DrawCardResponse callDrawCardsApi(Integer count){
        String deckId = createNewDeck();
        String endpoint = DRAW_CARD_API_ENDPOINT.replace("{deck_id}", deckId);
        HttpResponse httpResponse;
        if(count != null){
            httpResponse = httpUtil.get(endpoint, getQueryParamsForCount(count));
        }else{
            httpResponse = httpUtil.get(endpoint);
        }
        return gson.fromJson(httpResponse.getContent(), DrawCardResponse.class);
    }

    private Map<String,String> getQueryParamsForCount(Integer count){
        Map<String,String> params = new HashMap<>();
        params.put("count",Integer.toString(count));
        return params;
    }


	private String createNewDeck(){
        HttpResponse response = httpUtil.get(CREATE_DECK_API_ENDPOINT);
        CreateDeckResponse unmarshalledResponse = new Gson().fromJson(response.getContent(), CreateDeckResponse.class);
        return unmarshalledResponse.getDeck_id();
    }
}
