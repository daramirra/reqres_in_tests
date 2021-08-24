import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class SimpleRestTests {
    @Test
    void deleteUserTest() {
        given()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .assertThat().statusCode(204);
    }

    @Test
    void resourceNotFoundTest() {
        given()
                .when()
                .get("https://reqres.in/api/unknown/23")
                .then()
                .assertThat().statusCode(404)
                .body(equalTo("{}"));
    }

    @Test
    void createUserTest() {
        CreateUserResponseBody responseBody = given()
                .contentType(JSON)
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                       "    \"job\": \"leader\"\n" +
                        "}")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .assertThat().statusCode(201)
                .extract().body().as(CreateUserResponseBody.class);

        assertThat(responseBody.getId()).isNotEmpty();
        assertThat(responseBody.getName()).isEqualTo("morpheus");
        assertThat(responseBody.getCreatedAt()).isNotEmpty();
        assertThat(responseBody.getJob()).isEqualTo("leader");
    }

    @Test
    void negativeCreateUserTest() {
        given()
                .contentType(JSON)
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "}")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .assertThat().statusCode(400);
    }

    @Test
    void unsuccessfulLoginTest() {
        given()
                .contentType(JSON)
                .body("{\n" +
                        "    \"email\": \"peter@klaven\"\n" +
                        "}")
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .assertThat().body(equalTo("{\"error\":\"Missing password\"}"))
                .assertThat().statusCode(400);
    }
}
