package org.tkit.app.rs.v1.controllers;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tkit.app.rs.v1.models.AuthorDTO;
import org.tkit.app.rs.v1.rfc.RFCProblemDTO;
import org.tkit.quarkus.rs.models.PageResultDTO;
import org.tkit.quarkus.test.WithDBData;
import io.restassured.common.mapper.TypeRef;


import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.jboss.resteasy.util.HttpHeaderNames.ACCEPT;
import static org.jboss.resteasy.util.HttpHeaderNames.CONTENT_TYPE;

@QuarkusTest
@WithDBData(value = {"data/controllers-test-data.xls"},
        deleteBeforeInsert = true,
        rinseAndRepeat = true)
class AuthorRestControllerTest {

    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final int DEFAULT_TOTAL_PAGES = 1;

    private static final int REAL_AUTHORS_AMOUNT = 3;

    private static final String REAL_AUTHOR_ID = "1";
    private static final String REAL_AUTHOR_NAME = "John";
    private static final String REAL_AUTHOR_SURNAME = "Wayne";
    private static final Integer REAL_AUTHOR_AGE = 29;

    private static final String FAKE_AUTHOR_ID = "100";

    private static final String NEW_AUTHOR_ID = "10";
    private static final String NEW_AUTHOR_NAME = "Joanna";
    private static final String NEW_AUTHOR_SURNAME = "Cassidy";
    private static final Integer NEW_AUTHOR_AGE = 39;

    private static final String RFC_PROBLEM_DETAIL_ID_NOT_FOUND = "Author not found.";
    private static final String RFC_PROBLEM_TITLE_TECHNICAL_ERROR = "TECHNICAL ERROR";
    private static final String RFC_PROBLEM_TYPE_REST_EXCEPTION = "REST_EXCEPTION";
    private static final String RFC_PROBLEM_TYPE_VALIDATION_EXCEPTION = "VALIDATION_EXCEPTION";


    /* Search Tests */

    @Test
    @DisplayName("Get author by id")
    void testSuccessGetAuthorById() {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/authors/" + REAL_AUTHOR_ID);
        response.then()
                .statusCode(200);
        AuthorDTO author = response.body().as(AuthorDTO.class);
        assertThat(author.getId()).isEqualTo(REAL_AUTHOR_ID);
        assertThat(author.getAuthorName()).isEqualTo(REAL_AUTHOR_NAME);
        assertThat(author.getAuthorSurname()).isEqualTo(REAL_AUTHOR_SURNAME);
        assertThat(author.getAuthorAge()).isEqualTo(REAL_AUTHOR_AGE);
    }

    @Test
    @DisplayName("Doesn't get author by ID")
    void testFailGetAuthorByFakeID() {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/authors/" + FAKE_AUTHOR_ID);
        response.then()
                .statusCode(404);
        RFCProblemDTO rfcProblemDTO = response.as(RFCProblemDTO.class);
        assertThat(rfcProblemDTO.getStatus().toString()).hasToString("404");
        assertThat(rfcProblemDTO.getDetail()).isEqualTo(RFC_PROBLEM_DETAIL_ID_NOT_FOUND);
        assertThat(rfcProblemDTO.getInstance()).isNull();
        assertThat(rfcProblemDTO.getTitle()).isEqualTo(RFC_PROBLEM_TITLE_TECHNICAL_ERROR);
        assertThat(rfcProblemDTO.getType()).isEqualTo("REST_EXCEPTION");
    }

    @Test
    @DisplayName("Finds all authors by no criteria")
    void testSuccessFindAuthorsByNoCriteria() {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/authors");
        response.then().statusCode(200);
        PageResultDTO<?> pageResultDTO = response.as(PageResultDTO.class);
        assertThat(pageResultDTO.getSize()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(REAL_AUTHORS_AMOUNT);
        assertThat(pageResultDTO.getStream().size()).isEqualTo(REAL_AUTHORS_AMOUNT);
        assertThat(pageResultDTO.getTotalPages()).isEqualTo(DEFAULT_TOTAL_PAGES);
    }

    @Test
    @DisplayName("Finds all authors by fake criteria")
    void testSuccessFindAuthorsByFakeCriteria() {
        Response response = given()
                .when()
                .queryParam("fakeParam", "fakeValue")
                .get("/authors");
        response.then().statusCode(200);
        PageResultDTO<?> pageResultDTO = response.as(PageResultDTO.class);
        assertThat(pageResultDTO.getSize()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(REAL_AUTHORS_AMOUNT);
        assertThat(pageResultDTO.getTotalPages()).isEqualTo(DEFAULT_TOTAL_PAGES);
        assertThat(pageResultDTO.getStream().size()).isEqualTo(REAL_AUTHORS_AMOUNT);
    }

    @Test
    @DisplayName("Finds author by name")
    void testSuccessFindAuthorByName() {
        Response response = given()
                .when()
                .queryParam("authorName", REAL_AUTHOR_NAME)
                .get("/authors/");
        response.then().statusCode(200);

        PageResultDTO<AuthorDTO> pageResultDTO = response.as(getAuthorDtoTypeRef());
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(1);

        AuthorDTO author = pageResultDTO.getStream().get(0);
        assertThat(author.getId()).isEqualTo(REAL_AUTHOR_ID);
        assertThat(author.getAuthorName()).isEqualTo(REAL_AUTHOR_NAME);
        assertThat(author.getAuthorSurname()).isEqualTo(REAL_AUTHOR_SURNAME);
        assertThat(author.getAuthorAge()).isEqualTo(REAL_AUTHOR_AGE);
    }

    @Test
    @DisplayName("Finds author by first two letters of surname")
    void testSuccessFindAuthorByFirstTwoLettersOfSurname() {
        Response response = given()
                .when()
                .queryParam("authorSurname", REAL_AUTHOR_SURNAME.substring(0, 2))
                .get("/authors/");
        response.then().statusCode(200);

        PageResultDTO<AuthorDTO> pageResultDTO = response.as(getAuthorDtoTypeRef());
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(1);

        AuthorDTO author = pageResultDTO.getStream().get(0);
        assertThat(author.getId()).isEqualTo(REAL_AUTHOR_ID);
        assertThat(author.getAuthorName()).isEqualTo(REAL_AUTHOR_NAME);
        assertThat(author.getAuthorSurname()).isEqualTo(REAL_AUTHOR_SURNAME);
        assertThat(author.getAuthorAge()).isEqualTo(REAL_AUTHOR_AGE);
    }

    @Test
    @DisplayName("Finds author by all criteria")
    void testSuccessFindAuthorByAllCriteria() {
        Response response = given()
                .when()
                .queryParam("authorName", REAL_AUTHOR_NAME)
                .queryParam("authorSurname", REAL_AUTHOR_SURNAME)
                .queryParam("authorAge", REAL_AUTHOR_AGE)
                .get("/authors/");
        response.then().statusCode(200);

        PageResultDTO<AuthorDTO> pageResultDTO = response.as(getAuthorDtoTypeRef());
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(1);

        AuthorDTO author = pageResultDTO.getStream().get(0);
        assertThat(author.getId()).isEqualTo(REAL_AUTHOR_ID);
        assertThat(author.getAuthorName()).isEqualTo(REAL_AUTHOR_NAME);
        assertThat(author.getAuthorSurname()).isEqualTo(REAL_AUTHOR_SURNAME);
        assertThat(author.getAuthorAge()).isEqualTo(REAL_AUTHOR_AGE);
    }

    /* Save Tests {POST} */

    @Test
    @DisplayName("Saves author")
    void testSuccessSaveAuthor() {

        AuthorDTO newAuthor = setNewAuthorData();

        Response postResponse = given()
                .body(newAuthor)
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(ACCEPT, MediaType.APPLICATION_JSON)
                .when()
                .post("/authors");
        postResponse.then()
                .statusCode(201);

        AuthorDTO savedAuthor = postResponse.as(AuthorDTO.class);
        assertThat(savedAuthor.getId()).isEqualTo(NEW_AUTHOR_ID);
        assertThat(savedAuthor.getAuthorName()).isEqualTo(NEW_AUTHOR_NAME);
        assertThat(savedAuthor.getAuthorSurname()).isEqualTo(NEW_AUTHOR_SURNAME);
        assertThat(savedAuthor.getAuthorAge()).isEqualTo(NEW_AUTHOR_AGE);

        Response getResponse = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/authors");
        PageResultDTO<AuthorDTO> pageResultDTO = getResponse.as(getAuthorDtoTypeRef());
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(REAL_AUTHORS_AMOUNT + 1);
    }

    @Test
    @DisplayName("Doesn't save author without surname")
    void testFailSaveAuthorWithoutSurname() {

        AuthorDTO newAuthor = setNewAuthorData();

        newAuthor.setAuthorSurname(null);

        Response postResponse = given()
                .body(newAuthor)
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(ACCEPT, MediaType.APPLICATION_JSON)
                .when()
                .post("/authors");
        postResponse.then().statusCode(400);

        RFCProblemDTO rfcProblemDTO = postResponse.as(RFCProblemDTO.class);

        assertThat(rfcProblemDTO.getStatus().toString()).hasToString("400");
        assertThat(rfcProblemDTO.getInstance()).isNull();
        assertThat(rfcProblemDTO.getTitle()).isEqualTo(RFC_PROBLEM_TITLE_TECHNICAL_ERROR);
        assertThat(rfcProblemDTO.getType()).isEqualTo(RFC_PROBLEM_TYPE_VALIDATION_EXCEPTION);
    }

    /* Delete Tests {DELETE} */

    @Test
    @DisplayName("Delete author")
    void testSuccessDeleteAuthor() {
        Response response = given().
                when().delete("/authors/" + REAL_AUTHOR_ID);
        response.then().
                statusCode(204);

        Response getResponse = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when().get("/authors");
        getResponse.then()
                .statusCode(200);

        PageResultDTO<AuthorDTO> pageResultDTO = getResponse.as(getAuthorDtoTypeRef());
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(REAL_AUTHORS_AMOUNT - 1);
    }

    @Test
    @DisplayName("Doesn't delete author with fake ID")
    void testFailDeleteAuthorWithFakeId() {
        Response response = given()
                .when().delete("/authors/" + FAKE_AUTHOR_ID);
        RFCProblemDTO rfcProblemDTO = response.as(RFCProblemDTO.class);
        response.then()
                .statusCode(404);
        assertThat(rfcProblemDTO.getDetail()).isEqualTo(RFC_PROBLEM_DETAIL_ID_NOT_FOUND);
        assertThat(rfcProblemDTO.getTitle()).isEqualTo(RFC_PROBLEM_TITLE_TECHNICAL_ERROR);
        assertThat(rfcProblemDTO.getType()).isEqualTo(RFC_PROBLEM_TYPE_REST_EXCEPTION);
    }

    /* Update Tests {PUT} */

    @Test
    @DisplayName("Updates author")
    void testSuccessUpdateAuthor() {

        AuthorDTO newAuthor = setNewAuthorData();

        Response putResponse = given()
                .body(newAuthor)
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(ACCEPT, MediaType.APPLICATION_JSON)
                .when()
                .put("/authors/" + REAL_AUTHOR_ID);

        AuthorDTO updatedAuthor = putResponse.body().as(AuthorDTO.class);

        Assertions.assertThat(updatedAuthor.getAuthorName()).isEqualTo(NEW_AUTHOR_NAME);
        Assertions.assertThat(updatedAuthor.getAuthorSurname()).isEqualTo(NEW_AUTHOR_SURNAME);
        Assertions.assertThat(updatedAuthor.getAuthorAge()).isEqualTo(NEW_AUTHOR_AGE);

        Response getResponse = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/authors");
        PageResultDTO<AuthorDTO> pageResultDTO = getResponse.as(getAuthorDtoTypeRef());
        Assertions.assertThat(pageResultDTO.getTotalElements()).isEqualTo(REAL_AUTHORS_AMOUNT);
    }

    @Test
    @DisplayName("Doesn't update author by fake ID")
    void testFailUpdateAuthorByFakeId() {

        AuthorDTO newAuthorData = setNewAuthorData();

        Response putResponse = given()
                .body(newAuthorData)
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(ACCEPT, MediaType.APPLICATION_JSON)
                .when()
                .put("/authors/" + FAKE_AUTHOR_ID);
        putResponse.then().statusCode(404);

        RFCProblemDTO rfcProblemDTO = putResponse.as(RFCProblemDTO.class);

        Assertions.assertThat(rfcProblemDTO.getStatus().toString()).hasToString("404");
        Assertions.assertThat(rfcProblemDTO.getDetail()).isEqualTo(RFC_PROBLEM_DETAIL_ID_NOT_FOUND);
        Assertions.assertThat(rfcProblemDTO.getInstance()).isNull();
        Assertions.assertThat(rfcProblemDTO.getTitle()).isEqualTo(RFC_PROBLEM_TITLE_TECHNICAL_ERROR);
        Assertions.assertThat(rfcProblemDTO.getType()).isEqualTo("REST_EXCEPTION");
    }

    @Test
    @DisplayName("Doesn't update author without surname")
    void testFailUpdateAuthorWithoutSurname() {

        AuthorDTO newAuthorData = setNewAuthorData();

        newAuthorData.setAuthorSurname(null);

        Response putResponse = given()
                .body(newAuthorData)
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(ACCEPT, MediaType.APPLICATION_JSON)
                .when()
                .put("/authors/" + REAL_AUTHOR_ID);

        putResponse.then().statusCode(400);

        RFCProblemDTO rfcProblemDTO = putResponse.as(RFCProblemDTO.class);
        Assertions.assertThat(rfcProblemDTO.getStatus().toString()).hasToString("400");
        Assertions.assertThat(rfcProblemDTO.getInstance()).isNull();
        Assertions.assertThat(rfcProblemDTO.getTitle()).isEqualTo(RFC_PROBLEM_TITLE_TECHNICAL_ERROR);
        Assertions.assertThat(rfcProblemDTO.getType()).isEqualTo(RFC_PROBLEM_TYPE_VALIDATION_EXCEPTION);
    }

    private AuthorDTO setNewAuthorData() {
        AuthorDTO newAuthor = new AuthorDTO();
        newAuthor.setId(NEW_AUTHOR_ID);
        newAuthor.setAuthorName(NEW_AUTHOR_NAME);
        newAuthor.setAuthorSurname(NEW_AUTHOR_SURNAME);
        newAuthor.setAuthorAge(NEW_AUTHOR_AGE);

        return newAuthor;
    }

    private TypeRef<PageResultDTO<AuthorDTO>> getAuthorDtoTypeRef() {
        return new TypeRef<>() {
        };
    }
}
