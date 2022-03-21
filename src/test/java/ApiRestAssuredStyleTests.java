import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.junit.Test;

import static Utils.Utils.generateEmail;
import static Utils.Utils.generateString;
import static io.restassured.RestAssured.given;

public class ApiRestAssuredStyleTests {

    static final String NAME = generateString();
    static final String EMAIL = generateEmail("gmail.com", 4);
    static final String PASSWORD = "somepass";


    //Тест на получение кода ответа базы клиентов
    @Test
    public void getRequesUsersData() throws JSONException {
        given()
                .baseUri(ApiEndpointUrl.GET_USERS_INFO_API_URL)
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void checkUserCreation() throws JSONException {
        RestAssured.given()
                .when()
                .contentType(ContentType.JSON)
                .body("{"
                        + "\"username\": yabloko,"
                        + "\"email\": 823823@asdfg.ru,"
                        + "\"password\": \"Title\""
                        + "}"
                )
                .post(ApiEndpointUrl.CREATE_USER_API_URL)
                .then()
                .assertThat()
                .statusCode(200);


    }
}
