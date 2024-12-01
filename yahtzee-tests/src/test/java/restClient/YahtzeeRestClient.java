package restClient;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class YahtzeeRestClient {

    public static RequestSpecification getRequestSpecWithAuth() {
        RequestSpecification requestSpec = RestAssured.given()
                .auth()
                .preemptive()
                .basic("admin", "snakeeyes")
                .baseUri("http://localhost:8094");
        return requestSpec;
    }

    public static RequestSpecification getRequestSpecWithoutAuth() {
        RequestSpecification requestSpec = RestAssured.given()
                .baseUri("http://localhost:8094");
        return requestSpec;
    }

    public Response doHttpGetRequest(String path) {
        return RestAssured.given(getRequestSpecWithAuth())
                .when().log().all()
                .get(path)
                .then()
                .log().all()
                .extract()
                .response();

    }

    public <T> Response doHttpPostRequest(String path, T requestBody) {
        return RestAssured.given(getRequestSpecWithAuth())
                .body(requestBody)
                .when().log().all()
                .post(path)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public <T> Response doHttpPutRequest(String path, T requestBody, Boolean isAuthNeeded) {
        RequestSpecification requestSpecification = null;
        if (isAuthNeeded) {
            requestSpecification = getRequestSpecWithAuth();
        } else {
            requestSpecification = getRequestSpecWithoutAuth();
        }

        return RestAssured.given(requestSpecification)
                .body(requestBody)
                .when().log().all()
                .put(path)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response doHttpDeleteRequest(String path) {
        return RestAssured.given(getRequestSpecWithAuth())
                .when().log().all()
                .delete(path)
                .then()
                .log().all()
                .extract()
                .response();
    }
}
