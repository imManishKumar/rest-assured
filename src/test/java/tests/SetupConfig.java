package tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class SetupConfig {

    public void setup(){
        RequestSpecification requesSpec = new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com/")
                .setContentType("application/json")
                .setAccept("application/json")
                .log(LogDetail.ALL)
                .build();

        ResponseSpecification responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.ALL)
                .build();
    }
}
