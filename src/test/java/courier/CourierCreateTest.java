package courier;

import com.ya.courier.Courier;
import com.ya.courier.CourierClient;
import com.ya.courier.CourierCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.ya.DataForTests.clearCourierTestData;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Story("Creating new courier")
public class CourierCreateTest extends FeatureCreatingCourierTest {

    private final CourierClient courierClient = new CourierClient();
    private Courier courier;
    private int statusCode;
    private boolean ok;
    private String message;

    @After
    public void tearDown() {
        clearCourierTestData(courier);
    }

    /* курьера можно создать,
     * запрос возвращает правильный код ответа,
     * успешный запрос возвращает ok: true*/
    @Test
    @DisplayName("Courier can be created")
    @Description("Request returns status code 201 and message: \"true\". \"" +
            "Postcondition: Login with valid credentials. Get courier's ID and delete the courier.")
    public void createNewCourierReturnsCreated() {
        Courier courierCredentials = CourierClient.getRandomCredentials();
        courier = Courier.builder()
                .login(courierCredentials.getLogin())
                .password(courierCredentials.getPassword())
                .firstName(courierCredentials.getFirstName())
                .build();

        ValidatableResponse registrationResponse = courierClient.create(courier);
        statusCode = registrationResponse.extract().statusCode();
        ok = registrationResponse.extract().path("ok");

        assertThat("The response is: " + ok, ok, equalTo(true));
        assertEquals("Status core is incorrect" + " - " + statusCode, SC_CREATED, statusCode);
    }

    // возвращается ошибка в случае отсуствия одного из обязательных полей
    @Test
    @DisplayName("Request returns an error if password field doesn't exist")
    @Description("Register a courier without password field. The system should return an error message.")
    public void createNewCourierWithoutPasswordFieldReturnsBadRequest() {
        Courier courierCredentials = CourierClient.getRandomCredentials();
        courier = Courier.builder()
                .login(courierCredentials.getLogin())
                .firstName(courierCredentials.getFirstName())
                .build();

        ValidatableResponse registrationResponse = courierClient.create(courier);
        statusCode = registrationResponse.extract().statusCode();
        message = registrationResponse.extract().path("message");

        assertThat("The message is: " + message, message, equalTo("Недостаточно данных для создания учетной записи"));
        assertEquals("Status core is incorrect" + " - " + statusCode, SC_BAD_REQUEST, statusCode);

    }

    @Test
    @DisplayName("Request returns an error if login field doesn't exist")
    @Description("Register a courier without login field. The system should return an error message.")
    public void createNewCourierWithoutLoginFieldReturnsBadRequest() {
        Courier courierCredentials = CourierClient.getRandomCredentials();
        courier = Courier.builder()
                .password(courierCredentials.getPassword())
                .firstName(courierCredentials.getFirstName())
                .build();

        ValidatableResponse registrationResponse = courierClient.create(courier);
        statusCode = registrationResponse.extract().statusCode();
        message = registrationResponse.extract().path("message");

        assertThat("The message is: " + message, message, equalTo("Недостаточно данных для создания учетной записи"));
        assertEquals("Status core is incorrect" + " - " + statusCode, SC_BAD_REQUEST, statusCode);
    }

    // чтобы создать курьера, нужно передать в ручку все обязательные поля
    @Test
    @DisplayName("Create a new courier with required fields - login and password")
    @Description("Register a courier with only login and password fields. " +
            "Postcondition: Login with valid credentials. Get courier's ID and delete the courier.")
    public void createNewCourierWithOnlyRequiredFieldsReturnsCreated() {
        Courier courierCredentials = CourierClient.getRandomCredentials();

        courier = Courier.builder()
                .login(courierCredentials.getLogin())
                .password(courierCredentials.getPassword())
                .firstName(courierCredentials.getFirstName())
                .build();

        ValidatableResponse registrationResponse = courierClient.create(courier);
        statusCode = registrationResponse.extract().statusCode();
        ok = registrationResponse.extract().path("ok");

        assertThat("The response is: " + ok, ok, equalTo(true));
        assertEquals("Status core is incorrect" + " - " + statusCode, SC_CREATED, statusCode);
    }
}