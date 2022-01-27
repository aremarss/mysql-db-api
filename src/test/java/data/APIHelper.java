package data;

import io.restassured.http.ContentType;

import static data.DBHelper.getVerifyCode;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APIHelper {
    private APIHelper() {
    }

    private final static String baseUri = "http://localhost:9999/api";
    public static Integer equateValue;

    public static void authUser(DataHelper.AuthInfo info, Integer statusCode) {
        given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .body("{'login': " + info.getLogin() + ", 'password': " + info.getPassword() + "}")
                .when()
                .post("/auth")
                .then()
                .statusCode(statusCode);
    }

    public static String verifyToken(DataHelper.AuthInfo info) {
        return given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .body("{'login': " + info.getLogin() + ", 'code': " + getVerifyCode() + "}")
                .when()
                .post("/auth/verification")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    public static void doTransfer(String token, String fromCard, String toCard, String transferSum, Integer statusCode) {
        given()
                .baseUri(baseUri)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"from\": " + "\"" + fromCard + "\"" + ",\n" +
                        "  \"to\": " + "\"" + toCard + "\"" + ",\n" +
                        "  \"amount\": " + transferSum + "\n" +
                        "}")
                .when()
                .post("/transfer")
                .then()
                .statusCode(statusCode);
    }

    public static String getBalanceCard(String token, Integer numberCard) {
        String number = String.valueOf(numberCard - 1);
        return given()
                .baseUri(baseUri)
                .header(
                        "Authorization", "Bearer " + token)
                .when()
                .get("/cards")
                .then()
                .statusCode(200)
                .extract()
                .path("balance" + "[" + number + "]")
                .toString();
    }

    public static void equateBalance(DataHelper.AuthInfo info, Integer cardOne, Integer cardTwo) {
        int oneCard = Integer.parseInt(getBalanceCard(verifyToken(info), cardOne));
        int twoCard = Integer.parseInt(getBalanceCard(verifyToken(info), cardTwo));
        int inequality = oneCard - twoCard;
        equateValue = inequality / 2;
        if (inequality > 0) {
            doTransfer(verifyToken(info),
                    info.firstCard,
                    info.secondCard,
                    String.valueOf(equateValue),
                    200);
        }
        if (inequality < 0) {
            doTransfer(verifyToken(info),
                    info.secondCard,
                    info.firstCard,
                    String.valueOf(equateValue),
                    200);
        }
        assertEquals(getBalanceCard(verifyToken(info), cardOne), getBalanceCard(verifyToken(info), cardTwo));
    }

//    private static String getCard(String token, Integer numberCard) {
//        String number = String.valueOf(numberCard - 1);
//        card = given()
//                .baseUri(baseUri)
//                .header(
//                        "Authorization", "Bearer " + token)
//                .when()
//                .get("/cards")
//                .then()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
//                .extract()
//                .path("number" + "[" + number + "]");
//        if (card == null) {
//            System.out.println("\nFail: User doesn't have this card!");
//            return fail();
//        } else
//            return card;
//    }
}