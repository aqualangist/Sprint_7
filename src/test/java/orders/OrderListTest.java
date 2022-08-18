package orders;

import com.ya.orders.Order;
import com.ya.orders.OrderData;
import com.ya.orders.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.ya.DataForTests.clearOrderTestData;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Story("Making list of orders")
public class OrderListTest {

    private int track;
    OrderClient ordersClient = new OrderClient();

    @Before
    public void setUp() {
        OrderData orderData = OrderClient.getRandomOrderData();
        Order order = Order.builder()
                .firstName(orderData.getFirstName())
                .lastName(orderData.getLastName())
                .address(orderData.getAddress())
                .metroStation(orderData.getMetroStation())
                .phone(orderData.getPhone())
                .rentTime(orderData.getRentTime())
                .deliveryDate(orderData.getDeliveryDate())
                .comment(orderData.getComment())
                .color(orderData.getColor())
                .build();

        ValidatableResponse createOrderResponse = ordersClient.createOrder(order);
        track = createOrderResponse.extract().path("track");
    }

    @After
    public void tearDown() {
        clearOrderTestData(track);
    }

    @Test
    @DisplayName("Get list of orders")
    @Description("Get the list of all the orders in the system. Precondition: create an order. Postcondition: delete created order.")
    public void getOrderList() {
        ValidatableResponse getOrderListResponse = ordersClient.getOrderList();
        int statusCode = getOrderListResponse.extract().statusCode();
        ArrayList<String> ordersList = getOrderListResponse.extract().path("orders");

        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_OK));
        assertThat("Some data were wrong", ordersList, notNullValue());
    }
}