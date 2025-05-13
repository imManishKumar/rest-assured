package tests;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class RestfulBookerTest {

    private record userData(String firstname, String lastname, int totalprice, boolean depositpaid,
                            bookingDates bookingdates, String additionalneeds) {
    }

    private record bookingDates(String checkin, String checkout) {
    }

    private record authData(String username, String password){
    }

    private record partialData(String additionalneeds){
    }

    @Test
    public void createBookingTest() {
        String firstName = "John";
        final userData userData = new userData(firstName, "Doe", 150, true,
                new bookingDates("2023-10-01", "2023-10-10"), "Breakfast");

        given()
                .body(userData)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .when()
                .log()
                .all()
                .post("https://restful-booker.herokuapp.com/booking")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("bookingid", is(notNullValue()))
                .body("booking.firstname", equalTo(firstName))
                .body("booking.bookingdates.checkin", equalTo("2023-10-01"));
    }

    @Test
    public void getBookingTest() {
        String id = "467";
        given()
                .header("Accept", "application/json")
                .when()
                .log()
                .all()
                .get("https://restful-booker.herokuapp.com/booking/" + id)
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("firstname", equalTo("Josh"));
    }

    public String createToken(){
        authData authData = new authData("admin", "password123");
        return given()
                .body(authData)
                .header("Content-Type"," application/json")
                .when()
                .log()
                .all()
                .post("https://restful-booker.herokuapp.com/auth")
                .then()
                .statusCode(200)
                .body("token", is(notNullValue()))
                .extract().path("token").toString();
    }

    @Test
    public void updateBookingTest(){
        userData userData = new userData("Jane", "Doe", 200, true,
                new bookingDates("2023-10-01", "2023-10-10"), "Dinner");
        given()
                .body(userData)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie","token="+createToken())
                .when()
                .log()
                .all()
                .put("https://restful-booker.herokuapp.com/booking/467")
                .then()
                .statusCode(200)
                .body("additionalneeds", equalTo("Dinner"));
    }

    @Test
    public void partialUpdateTest(){
        partialData partialData = new partialData("Brunch");

        given()
                .body(partialData)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie","token="+createToken())
                .when()
                .log()
                .all()
                .patch("https://restful-booker.herokuapp.com/booking/467")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("additionalneeds", equalTo("Brunch"));
    }

    @Test
    public void deleteBookingTest(){
        given()
                .header("Content-Type", "application/json")
                .header("Cookie","token="+createToken())
                .when()
                .log()
                .all()
                .delete("https://restful-booker.herokuapp.com/booking/467")
                .then()
                .log()
                .all()
                .statusCode(201);
    }
}



