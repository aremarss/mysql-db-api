package data;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class DataHelper {
    static Faker faker = new Faker();

    @Data
    @AllArgsConstructor
    public static class AuthInfo {
        String login;
        String password;
        String firstCard;
        String secondCard;

        public AuthInfo(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }

    public static AuthInfo getFirstUser() {
        return new AuthInfo("vasya",
                "qwerty123",
                "5559 0000 0000 0001",
                "5559 0000 0000 0002");
    }

    public static AuthInfo getSecondUser() {
        return new AuthInfo("petya",
                "123qwerty");
    }

    public static AuthInfo getInvalidUser() {
        return new AuthInfo(faker.name().username(),
                faker.internet().password());
    }
}