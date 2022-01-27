package test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static data.APIHelper.*;
import static data.DBHelper.deleteAllDB;
import static data.DBHelper.deleteCodes;
import static data.DataHelper.*;

public class MySQLDBTest {

    @AfterEach
    void setDown() {
        deleteCodes();
        authUser(getFirstUser(), 200);
    }

    @AfterAll
    static void setDownAll() {
        deleteAllDB();
    }

    @Test
    void shouldLoginFirstUser() {
        authUser(getFirstUser(), 200);
    }

    @Test
    void shouldLoginSecondUser() {
        authUser(getSecondUser(), 200);
    }

    @Test
    void shouldReturnFailWithInvalidUser() {
        authUser(getInvalidUser(), 400);
    }

    @Test
    void shouldTransferMoneyToUserCard() {
        authUser(getFirstUser(), 200); // С какой карты, на какую карту, сумма перевода, ожидаемый статус.
        doTransfer(verifyToken(getFirstUser()),
                "5559 0000 0000 0002",
                "5559 0000 0000 0001",
                "1000",
                200);
        equateBalance(getFirstUser(), 1, 2); // Возвращение равного баланса между картами.
    }

    @Test
    void shouldReturnFailTransferOverLimitToUserCard() {
        authUser(getFirstUser(), 200);
        doTransfer(verifyToken(getFirstUser()),
                "5559 0000 0000 0002",
                "5559 0000 0000 0001",
                "11000",
                400);
        equateBalance(getFirstUser(), 1, 2);
    }

    @Test
    void shouldTransferMoneyToAnotherCard() {
        authUser(getFirstUser(), 200);
        doTransfer(verifyToken(getFirstUser()),
                "5559 0000 0000 0002",
                "5559 0000 0000 0008",
                "1000",
                200);
        equateBalance(getFirstUser(), 1, 2);
    }
}