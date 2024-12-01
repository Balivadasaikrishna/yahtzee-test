package tests;

import restClient.YahtzeeRestClient;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojo.AllDices;
import pojo.Data;
import pojo.DieInvalidResponse;
import pojo.DieValidResponse;

public class YahtzeeTests {

    private YahtzeeRestClient restClient = new YahtzeeRestClient();
    private static String playerName = "sai";

    @Test(dataProvider = "dieValidData")
    public void getIndividualValidDieTest(int number) {
        Response response = restClient.doHttpGetRequest(String.format("/die/%s", number));
        DieValidResponse dieResponse = response.as(DieValidResponse.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "status code is not 200");
        Assert.assertTrue(dieResponse.getStatus().equals("success"), "Die response is not success");
        Assert.assertTrue(dieResponse.getData().getId() >= 1 && dieResponse.getData().getId() <= 5,
                "id is not within the range [" + 1 + ", " + 5 + "]");
        Assert.assertTrue(dieResponse.getData().getValue() >= 1 && dieResponse.getData().getValue() <= 6,
                "value is not within the range [" + 1 + ", " + 6 + "]");
    }

    @Test(dataProvider = "dieInvalidData")
    public void getIndividualInValidDieTest(int number) {
        Response response = restClient.doHttpGetRequest(String.format("/die/%s", number));
        DieInvalidResponse dieResponse = response.as(DieInvalidResponse.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "status code is not 400");
        Assert.assertTrue(dieResponse.getStatus().equals("failed"), "Die response is not failed");
        Assert.assertTrue(dieResponse.getData().equals("Die ID must be an integer between 1 and 5"), "Die ID is not between 1 and 5");
    }

    @Test(dataProvider = "dieValidData")
    public void setValidDieTest(int number) {
        Data data = Data.builder().id(number).value(number).build();
        Response response = restClient.doHttpPutRequest("die", data, true);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_NO_CONTENT,
                "status code is not 204");
    }

    @Test(dataProvider = "dieInvalidData")
    public void setInvalidDieTest(int number) {
        Data data = Data.builder().id(number).value(number).build();
        Response response = restClient.doHttpPutRequest("die", data, true);
        DieInvalidResponse dieResponse = response.as(DieInvalidResponse.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "status code is not 400");
        Assert.assertTrue(dieResponse.getStatus().equals("failed"), "Die response is not failed");
        Assert.assertTrue(dieResponse.getData().equals("Die ID must be between 1 and 5"), "Die ID is not between 1 and 5");
    }

    @Test(dataProvider = "dieValidData")
    public void setDieWithoutAuthorizationTest(int number) {
        Data data = Data.builder().id(number).value(number).build();
        Response response = restClient.doHttpPutRequest("die", data, false);
        DieInvalidResponse dieResponse = response.as(DieInvalidResponse.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_UNAUTHORIZED, "status code is not 401");
        Assert.assertTrue(dieResponse.getStatus().equals("failed"), "Die response is not failed");
        Assert.assertTrue(dieResponse.getData().equals("Basic auth missing or could not be parsed"), "Api is not failed as auth is not present");
    }

    @Test(dataProvider = "dieValidData")
    public void rollAValidDieTest(int number) {
        Response response = restClient.doHttpPostRequest(String.format("/rollDie/%s", number), "{}");
        DieValidResponse dieResponse = response.as(DieValidResponse.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "status code is not 200");
        Assert.assertTrue(dieResponse.getStatus().equals("success"), "Die response is not success");
        Assert.assertTrue(dieResponse.getData().getId() >= 1 && dieResponse.getData().getId() <= 5,
                "id is not within the range [" + 1 + ", " + 5 + "]");
        Assert.assertTrue(dieResponse.getData().getValue() >= 1 && dieResponse.getData().getValue() <= 6,
                "value is not within the range [" + 1 + ", " + 6 + "]");
    }

    @Test(dataProvider = "dieInvalidData")
    public void rollInvalidDieTest(int number) {
        Response response = restClient.doHttpPostRequest(String.format("/rollDie/%s", number), "{}");
        DieInvalidResponse dieResponse = response.as(DieInvalidResponse.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "status code is not 400");
        Assert.assertTrue(dieResponse.getStatus().equals("failed"), "Die response is not failed");
        Assert.assertTrue(dieResponse.getData().equals("Die ID must be an integer between 1 and 5"), "Die ID is not between 1 and 5");
    }

    @Test
    public void getAllDieValuesTest() {
        Response response = restClient.doHttpGetRequest("dice");
        AllDices allDices = response.as(AllDices.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "status code is not 200");
        Assert.assertTrue(allDices.getStatus().equals("success"), "dice response is not success");
        Assert.assertTrue(allDices.getData().size() == 5, "dice response size is not 5");
        allDices.getData().stream().forEach(d -> {
            Assert.assertTrue(d.getId() >= 1 && d.getId() <= 5,
                    "id is not within the range [" + 1 + ", " + 5 + "]");
            Assert.assertTrue(d.getValue() >= 1 && d.getValue() <= 6,
                    "value is not within the range [" + 1 + ", " + 6 + "]");
        });
    }

    @Test
    public void rollAll5DiceTest() {
        Response response = restClient.doHttpPostRequest("rollDice", "{}");
        AllDices allDices = response.as(AllDices.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "status code is not 200");
        Assert.assertTrue(allDices.getStatus().equals("success"), "dice response is not success");
        Assert.assertTrue(allDices.getData().size() == 5, "dice response size is not 5");
        allDices.getData().stream().forEach(d -> {
            Assert.assertTrue(d.getId() >= 1 && d.getId() <= 5,
                    "id is not within the range [" + 1 + ", " + 5 + "]");
            Assert.assertTrue(d.getValue() >= 1 && d.getValue() <= 6,
                    "value is not within the range [" + 1 + ", " + 6 + "]");
        });
    }

    @Test
    public void checkIsYahtzeeTest() {
        Response response = restClient.doHttpGetRequest("isYahtzee");
        DieInvalidResponse dieResponse = response.as(DieInvalidResponse.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "status code is not 200");
        Assert.assertTrue(dieResponse.getStatus().equals("success"), "isYahtzee response is not success");
        Assert.assertFalse(Boolean.parseBoolean(dieResponse.getData()), "dice response is not success");
    }

    @Test
    public void setValidPlayerNameTest() {
        Response response = restClient.doHttpPutRequest("playerName", "{\"name\":\"" + playerName + "\"}", true);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_NO_CONTENT, "status code is not 204");
    }

    @Test
    public void setInvalidPlayerNameTest() {
        Response response = restClient.doHttpPutRequest("playerName", "{\"name\":1}", true);
        DieInvalidResponse res = response.as(DieInvalidResponse.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "status code is not 204");
        Assert.assertTrue(res.getStatus().equals("failed"), "Die response is not failed");
        Assert.assertTrue(res.getData().equals("Invalid request body format"), "error message is not matching");
    }

    @Test
    public void setPlayerNameWithoutAuthenticationTest() {
        Response response = restClient.doHttpPutRequest("playerName", "{\"name\":\"" + playerName + "\"}", false);
        DieInvalidResponse dieResponse = response.as(DieInvalidResponse.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_UNAUTHORIZED, "status code is not 401");
        Assert.assertTrue(dieResponse.getStatus().equals("failed"), "Die response is not failed");
        Assert.assertTrue(dieResponse.getData().equals("Basic auth missing or could not be parsed"), "Api is not failed as auth is not present");
    }

    @Test
    public void getPlayerNameTest() {
        this.setPlayerNameWithoutAuthenticationTest(); // setting player name first before fetching
        Response response = restClient.doHttpGetRequest("playerName");
        DieInvalidResponse dieResponse = response.as(DieInvalidResponse.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "status code is 200");
        Assert.assertTrue(dieResponse.getStatus().equals("success"), "Die response is not success");
        Assert.assertTrue(dieResponse.getData().equals(playerName), "player name is not matching");
    }


    @DataProvider(name = "dieValidData")
    public Object[][] dieValidData() {
        return new Object[][]{
                {1},
                {2},
                {3},
                {4},
                {5}
        };
    }

    @DataProvider(name = "dieInvalidData")
    public Object[][] dieInvalidData() {
        return new Object[][]{
                {-1},
                {0},
                {6}
        };
    }
}
