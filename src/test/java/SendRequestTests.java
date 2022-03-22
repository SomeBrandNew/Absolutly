import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.get;
import static utils.PropertyLoader.loadProperty;
import static utils.Utils.generateEmail;
import static utils.Utils.generateString;


public class SendRequestTests {

    //проверка создания пользователя и корректности его отображения в БД
    @Test
    public void checkUserCreation() throws JSONException {
        String name = generateString();
        String email = generateEmail("gmail.com", 4);

        Response getResponseOfUserAmountBeforeCreation = get(loadProperty("getUsersInfoApiUrl"));
        int amountOfUsersBaseBefore = getResponseOfUserAmountBeforeCreation.jsonPath().getList("created_at").size();

        JSONObject requestBody = new JSONObject();
        requestBody.put("username", name);
        requestBody.put("email", email);
        requestBody.put("password", "somePass");

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());
        Response response = request.post(loadProperty("createUserApiUrl"));

        Response getDataAfterUserCreation = get(loadProperty("getUsersInfoApiUrl"));

        List<String> listOfNames = getDataAfterUserCreation.jsonPath().getList("username");
        String newUserName = listOfNames.get(listOfNames.size() - 1);

        List<String> listOfEmails = getDataAfterUserCreation.jsonPath().getList("email");
        String newUserEmail = listOfEmails.get(listOfEmails.size() - 1);

        int statusCode = response.getStatusCode();
        int amountUsersAfter = getDataAfterUserCreation.jsonPath().getList("created_at").size();

        //Отображается 200 код, но по идее должен быть 201\Created
        Assert.assertEquals("Status code does not match \"200\"", 200, statusCode);
        Assert.assertEquals("Name does not match name of created user.", name, newUserName);
        Assert.assertEquals("Email does not match email of created user.", email, newUserEmail);
        Assert.assertEquals("Amount of users did not changed", amountOfUsersBaseBefore + 1, amountUsersAfter);
    }

    //проверка создания пользователя с именем уже зарегистрированного клиента
    @Test
    public void checkExistingUserCreation() throws JSONException {

        JSONObject requestBody = new JSONObject();
        requestBody.put("username", "kvakva2");
        requestBody.put("email", "to@believein.ru");
        requestBody.put("password", "somePass");

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = request.post(loadProperty("createUserApiUrl"));
        int statusCode = response.getStatusCode();

        ResponseBody responseBody = response.getBody();
        String responseBodyAsString = responseBody.asString();

        String statusLine = response.getStatusLine();

        Assert.assertEquals(400, statusCode);
        Assert.assertEquals("Response body does not contains \"This username is taken. Try another.\"" /*Expected value*/, responseBodyAsString.contains("This username is taken. Try another.") /*Actual Value*/, true);
        Assert.assertEquals("Status line does not contains \"HTTP/1.1 400 Bad Request\"", "HTTP/1.1 400 Bad Request", statusLine);

    }

    //проверка создания пользователя с email, которое есть у ранее зарегистрированного клиента
    @Test
    public void checkCreationNewUserWithNotUniquePassword() throws JSONException {

        JSONObject requestBody = new JSONObject();
        requestBody.put("username", "kvakva333333");
        requestBody.put("email", "to@believein.ru");
        requestBody.put("password", "somePass");

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = request.post(loadProperty("createUserApiUrl"));
        int statusCode = response.getStatusCode();
        Assert.assertEquals(400, statusCode);

        ResponseBody responseBody = response.getBody();
        String responseBodyAsString = responseBody.asString();
        Assert.assertEquals("Response body does not contains \"Email already exists\"" /*Expected value*/, responseBodyAsString.contains("Email already exists") /*Actual Value*/, true);

        String statusLine = response.getStatusLine();
        Assert.assertEquals("Status line does not contains \"HTTP/1.1 400 Bad Request\"", "HTTP/1.1 400 Bad Request", statusLine);

    }
}


