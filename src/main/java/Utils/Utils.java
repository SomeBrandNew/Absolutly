package Utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.stream.Collectors;

public class Utils {

    public static String generateString() {
        String symbols = "abcdefghijklmnopqrstuvwxyz";
        String random = new Random().ints(7, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());


        return random;
    }


    public static String generateEmail(String domain, int length) {
        return RandomStringUtils.random(length, "abcdefghijklmnopqrstuvwxyz") + "@" + domain;
    }

    public static JSONObject generateUserCreationRequestBody() {

        String name = generateString();
        String email = generateEmail("gmail.com", 4);

        JSONObject requestBody = new JSONObject();
        requestBody.put("username", name);
        requestBody.put("email", email);
        requestBody.put("password", "somePass");
        return requestBody;
    }


}
