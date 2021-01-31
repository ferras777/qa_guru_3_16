import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class ReqresTests {

    @BeforeAll
    static void beforeAll() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    @DisplayName("Update user info test")
    void updateUserInfo() {
        String data = "{" +
                "    \"name\": \"morpheus\"," +
                "    \"job\": \"zion resident\"" +
                "}";
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        given()
                .contentType(JSON)
                .body(data)
        .when()
                .put("/users/2")
        .then()
                .statusCode(200)
                .log().body()
                .body("name", is("morpheus"))
                .body("job", is("zion resident"))
                .body("updatedAt", containsString(date));
    }
    
    @Test
    @DisplayName("Unsuccessful login test")
    void loginUnsuccessful() {
        String data = "{" +
                "\"email\": \"peter@klaven\"" +
                "}";

        given()
                .contentType(JSON)
                .body(data)
        .when()
                .post("/login")
        .then()
                .statusCode(400)
                .log().body()
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Successful login test")
    void loginSuccessful() {
        String data = "{" +
                "\"email\": \"eve.holt@reqres.in\"," +
                "\"password\": \"cityslicka\"" +
                "}";

        given()
                .contentType(JSON)
                .body(data)
        .when()
                .post("/login")
        .then()
                .statusCode(200)
                .log().body()
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("Single user body test")
    void singleUserInfo() {
        given()
                .contentType(JSON)
        .when()
                .get("/unknown/2")
        .then()
                .statusCode(200)
                .log().body()
                .body("data.id", is(2))
                .body("data.name", is("fuchsia rose"))
                .body("data.year", is(2001))
                .body("data.color", is("#C74375"))
                .body("data.pantone_value", is("17-2031"));
    }

    @Test
    @DisplayName("Create user test")
    void createUser() {
        String data = "{" +
                "    \"name\": \"morpheus\"," +
                "    \"job\": \"zion resident\"" +
                "}";
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        given()
                .contentType(JSON)
                .body(data)
        .when()
                .post("/users")
        .then()
                .statusCode(201)
                .log().body()
                .body("name", is("morpheus"))
                .body("job", is("zion resident"))
                .body("id", hasLength(3))
                .body("createdAt", containsString(date));
    }
}
