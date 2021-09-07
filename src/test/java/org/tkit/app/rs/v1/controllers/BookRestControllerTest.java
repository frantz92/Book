package org.tkit.app.rs.v1.controllers;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tkit.app.AbstractTest;
import org.tkit.app.domain.models.enums.BookCategory;
import org.tkit.app.rs.v1.models.AuthorDTO;
import org.tkit.app.rs.v1.models.BookDTO;
import org.tkit.app.rs.v1.rfc.RFCProblemDTO;
import org.tkit.quarkus.rs.models.PageResultDTO;
import org.tkit.quarkus.test.WithDBData;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@QuarkusTest
@WithDBData(value = {"data/book-controller-test-data.xls"},
        deleteBeforeInsert = true,
        rinseAndRepeat = true)
class BookRestControllerTest extends AbstractTest {

    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final int DEFAULT_TOTAL_PAGES = 1;

    private static final int REAL_BOOKS_AMOUNT = 3;
    private static final String REAL_BOOK_ID = "1";
    private static final String REAL_BOOK_ISBN = "73-110";
    private static final String REAL_BOOK_TITLE = "Long time";
    private static final BookCategory REAL_BOOK_CATEGORY = BookCategory.FANTASY;
    private static final Integer REAL_BOOK_PAGES = 105;

    private static final String REAL_AUTHOR_ID = "1";
    private static final String REAL_AUTHOR_NAME = "JOHN";
    private static final String REAL_AUTHOR_SURNAME = "WAYNE";
    private static final Integer REAL_AUTHOR_AGE = 29;

    private static final String NEW_BOOK_ID = "10";
    private static final String NEW_BOOK_ISBN = "80-228";
    private static final String NEW_BOOK_TITLE = "Say Hello World";
    private static final BookCategory NEW_BOOK_CATEGORY = BookCategory.MYSTERY;
    private static final Integer NEW_BOOK_PAGES = 409;

    private static final String FAKE_BOOK_ID = "100";

    private static final String INCORRECT_BOOK_ID = "-2";

    private static final String RFC_PROBLEM_DETAIL_ID_NOT_FOUND = "Book not found.";
    private static final String RFC_PROBLEM_TITLE_TECHNICAL_ERROR = "TECHNICAL ERROR";
    private static final String RFC_PROBLEM_TYPE_REST_EXCEPTION = "REST_EXCEPTION";
    private static final String RFC_PROBLEM_TYPE_VALIDATION_EXCEPTION = "VALIDATION_EXCEPTION";

    /* Search Tests */
    @Test
    @DisplayName("Gets book by ID")
    void testSuccessGetBookById() {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/books/" + REAL_BOOK_ID);
        response.then()
                .statusCode(200);
        BookDTO book = response.body().as(BookDTO.class);
        assertThat(book.getId()).isEqualTo(REAL_BOOK_ID);
        assertThat(book.getBookIsbn()).isEqualTo(REAL_BOOK_ISBN);
        assertThat(book.getBookTitle()).isEqualTo(REAL_BOOK_TITLE);
        assertThat(book.getBookPages()).isEqualTo(REAL_BOOK_PAGES);
        assertThat(book.getBookCategory()).isEqualTo(REAL_BOOK_CATEGORY);
        assertThat(book.getBookAuthor().getAuthorName()).isEqualTo(REAL_AUTHOR_NAME);
        assertThat(book.getBookAuthor().getAuthorSurname()).isEqualTo(REAL_AUTHOR_SURNAME);
        assertThat(book.getBookAuthor().getAuthorAge()).isEqualTo(REAL_AUTHOR_AGE);
    }

    @Test
    @DisplayName("Doesn't get book by ID")
    void testFailGetBookByFakeID() {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/books/" + FAKE_BOOK_ID);
        response.then()
                .statusCode(404);
        RFCProblemDTO rfcProblemDTO = response.as(RFCProblemDTO.class);
        assertThat(rfcProblemDTO.getStatus().toString()).hasToString("404");
        assertThat(rfcProblemDTO.getDetail()).isEqualTo("Book not found.");
        assertThat(rfcProblemDTO.getInstance()).isNull();
        assertThat(rfcProblemDTO.getTitle()).isEqualTo("TECHNICAL ERROR");
        assertThat(rfcProblemDTO.getType()).isEqualTo("REST_EXCEPTION");
    }

    @Test
    @DisplayName("Doesn't get book by incorrect ID")
    void testFailGetBookByIncorrectID() {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/books/" + INCORRECT_BOOK_ID);
        response.then()
                .statusCode(404);
        RFCProblemDTO rfcProblemDTO = response.as(RFCProblemDTO.class);
        assertThat(rfcProblemDTO.getStatus().toString()).hasToString("404");
        assertThat(rfcProblemDTO.getDetail()).isEqualTo("Book not found.");
        assertThat(rfcProblemDTO.getInstance()).isNull();
        assertThat(rfcProblemDTO.getTitle()).isEqualTo("TECHNICAL ERROR");
        assertThat(rfcProblemDTO.getType()).isEqualTo("REST_EXCEPTION");
    }

    @Test
    @DisplayName("Finds all books by no criteria")
    void testSuccessFindBooksByNoCriteria() {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/books");
        response.then().statusCode(200);
        PageResultDTO pageResultDTO = response.as(PageResultDTO.class);
        assertThat(pageResultDTO.getSize()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(REAL_BOOKS_AMOUNT);
        assertThat(pageResultDTO.getStream().size()).isEqualTo(REAL_BOOKS_AMOUNT);
        assertThat(pageResultDTO.getTotalPages()).isEqualTo(DEFAULT_TOTAL_PAGES);
    }

    @Test
    @DisplayName("Finds all books by fake criteria")
    void testSuccessFindBooksByFakeCriteria() {
        Response response = given()
                .when()
                .queryParam("fakeParam", "fakeValue")
                .get("/books");
        response.then().statusCode(200);
        PageResultDTO pageResultDTO = response.as(PageResultDTO.class);
        assertThat(pageResultDTO.getSize()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(REAL_BOOKS_AMOUNT);
        assertThat(pageResultDTO.getTotalPages()).isEqualTo(DEFAULT_TOTAL_PAGES);
        assertThat(pageResultDTO.getStream().size()).isEqualTo(REAL_BOOKS_AMOUNT);
    }

    @Test
    @DisplayName("Finds book by title")
    void testSuccessFindBookByTitle() {
        Response response = given()
                .when()
                .queryParam("bookTitle", REAL_BOOK_TITLE)
                .get("/books/");
        response.then().statusCode(200);

        PageResultDTO<BookDTO> pageResultDTO = response.as(getBookDtoTypeRef());
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(1);

        BookDTO book = pageResultDTO.getStream().get(0);
        assertThat(book.getId()).isEqualTo(REAL_BOOK_ID);
        assertThat(book.getBookIsbn()).isEqualTo(REAL_BOOK_ISBN);
        assertThat(book.getBookTitle()).isEqualTo(REAL_BOOK_TITLE);
        assertThat(book.getBookPages()).isEqualTo(REAL_BOOK_PAGES);
        assertThat(book.getBookCategory()).isEqualTo(REAL_BOOK_CATEGORY);
        assertThat(book.getBookAuthor().getAuthorName()).isEqualTo(REAL_AUTHOR_NAME);
        assertThat(book.getBookAuthor().getAuthorSurname()).isEqualTo(REAL_AUTHOR_SURNAME);
        assertThat(book.getBookAuthor().getAuthorAge()).isEqualTo(REAL_AUTHOR_AGE);
    }

    @Test
    @DisplayName("Finds books by first two letters of title")
    void testSuccessFindBooksByFirstTwoLettersOfTitle() {
        Response response = given()
                .when()
                .queryParam("bookTitle", REAL_BOOK_TITLE.substring(0, 2))
                .get("/books/");
        response.then().statusCode(200);

        PageResultDTO<BookDTO> pageResultDTO = response.as(getBookDtoTypeRef());
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(1);

        BookDTO book = pageResultDTO.getStream().get(0);
        assertThat(book.getId()).isEqualTo(REAL_BOOK_ID);
        assertThat(book.getBookIsbn()).isEqualTo(REAL_BOOK_ISBN);
        assertThat(book.getBookTitle()).isEqualTo(REAL_BOOK_TITLE);
        assertThat(book.getBookPages()).isEqualTo(REAL_BOOK_PAGES);
        assertThat(book.getBookCategory()).isEqualTo(REAL_BOOK_CATEGORY);
        assertThat(book.getBookAuthor().getAuthorName()).isEqualTo(REAL_AUTHOR_NAME);
        assertThat(book.getBookAuthor().getAuthorSurname()).isEqualTo(REAL_AUTHOR_SURNAME);
        assertThat(book.getBookAuthor().getAuthorAge()).isEqualTo(REAL_AUTHOR_AGE);
    }

    @Test
    @DisplayName("Finds books by category 'FANTASY'")
    public void testSuccessFindBooksByCategory() {
        Response response = given()
                .when()
                .queryParam("bookCategory", REAL_BOOK_CATEGORY)
                .get("/books");
        response.then()
                .statusCode(200);

        PageResultDTO<BookDTO> pageResultDTO = response.as(getBookDtoTypeRef());
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(1);

        BookDTO book = pageResultDTO.getStream().get(0);
        assertThat(book.getId()).isEqualTo(REAL_BOOK_ID);
        assertThat(book.getBookIsbn()).isEqualTo(REAL_BOOK_ISBN);
        assertThat(book.getBookTitle()).isEqualTo(REAL_BOOK_TITLE);
        assertThat(book.getBookPages()).isEqualTo(REAL_BOOK_PAGES);
        assertThat(book.getBookCategory()).isEqualTo(REAL_BOOK_CATEGORY);
        assertThat(book.getBookAuthor().getAuthorName()).isEqualTo(REAL_AUTHOR_NAME);
        assertThat(book.getBookAuthor().getAuthorSurname()).isEqualTo(REAL_AUTHOR_SURNAME);
        assertThat(book.getBookAuthor().getAuthorAge()).isEqualTo(REAL_AUTHOR_AGE);
    }

    @Test
    @DisplayName("Finds books by author name")
    public void testSuccessFindBooksByAuthorName() {
        Response response = given()
                .when()
                .queryParam("authorName", REAL_AUTHOR_NAME)
                .get("/books");
        response.then().statusCode(200);

        PageResultDTO<BookDTO> pageResultDTO = response.as(getBookDtoTypeRef());
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(1);

        assertThat(pageResultDTO.getStream()
                .stream().allMatch(el -> el.getBookAuthor().getAuthorName().equals(REAL_AUTHOR_NAME))).isTrue();
    }

    @Test
    @DisplayName("Finds books by author surname")
    public void testSuccessFindBooksByAuthorSurname() {
        Response response = given()
                .when()
                .queryParam("authorSurname", REAL_AUTHOR_SURNAME)
                .get("/books");
        response.then().statusCode(200);

        PageResultDTO<BookDTO> pageResultDTO = response.as(getBookDtoTypeRef());
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(1);

        assertThat(pageResultDTO.getStream()
                .stream().allMatch(el -> el.getBookAuthor().getAuthorSurname().equals(REAL_AUTHOR_SURNAME))).isTrue();
    }

    @Test
    @DisplayName("Finds book by all criteria")
    public void testSuccessFindBookByAllCriteria() {
        Response response = given()
                .when()
                .queryParam("bookIsbn", REAL_BOOK_ISBN)
                .queryParam("bookTitle", REAL_BOOK_TITLE)
                .queryParam("bookPages", REAL_BOOK_PAGES)
                .queryParam("bookCategory", REAL_BOOK_CATEGORY)
                .queryParam("authorName", REAL_AUTHOR_NAME)
                .queryParam("authorSurname", REAL_AUTHOR_SURNAME)
                .queryParam("authorAge", REAL_AUTHOR_AGE)
                .get("/books");
        response.then().statusCode(200);

        PageResultDTO<BookDTO> pageResultDTO = response.as(getBookDtoTypeRef());
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(1);

        BookDTO book = pageResultDTO.getStream().get(0);
        assertThat(book.getId()).isEqualTo(REAL_BOOK_ID);
        assertThat(book.getBookIsbn()).isEqualTo(REAL_BOOK_ISBN);
        assertThat(book.getBookTitle()).isEqualTo(REAL_BOOK_TITLE);
        assertThat(book.getBookPages()).isEqualTo(REAL_BOOK_PAGES);
        assertThat(book.getBookCategory()).isEqualTo(REAL_BOOK_CATEGORY);
        assertThat(book.getBookAuthor().getAuthorName()).isEqualTo(REAL_AUTHOR_NAME);
        assertThat(book.getBookAuthor().getAuthorSurname()).isEqualTo(REAL_AUTHOR_SURNAME);
        assertThat(book.getBookAuthor().getAuthorAge()).isEqualTo(REAL_AUTHOR_AGE);
    }

    /* Save Tests {POST} */

    @Test
    @DisplayName("Saves book")
    public void testSuccessSaveBook() {

        BookDTO newBook = setNewBookData();

        Response postResponse = given()
                .body(newBook)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .when()
                .post("/books");
        postResponse.then()
                .statusCode(201);

        BookDTO savedBook = postResponse.as(BookDTO.class);
        assertThat(savedBook.getBookIsbn()).isEqualTo(NEW_BOOK_ISBN);
        assertThat(savedBook.getBookTitle()).isEqualTo(NEW_BOOK_TITLE);
        assertThat(savedBook.getBookCategory()).isEqualTo(NEW_BOOK_CATEGORY);
        assertThat(savedBook.getBookAuthor().getAuthorName()).isEqualTo(REAL_AUTHOR_NAME);
        assertThat(savedBook.getBookAuthor().getAuthorSurname()).isEqualTo(REAL_AUTHOR_SURNAME);
        assertThat(savedBook.getBookAuthor().getAuthorAge()).isEqualTo(REAL_AUTHOR_AGE);

        Response getResponse = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/books");
        PageResultDTO<BookDTO> pageResultDTO = getResponse.as(getBookDtoTypeRef());
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(REAL_BOOKS_AMOUNT + 1);
    }

    @Test
    @DisplayName("Doesn't save book without title")
    public void testFailSaveBookWithoutTitle() {

        BookDTO newBook = setNewBookData();

        newBook.setBookTitle(null);

        Response postResponse = given()
                .body(newBook)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .when()
                .post("/books");
        postResponse.then().statusCode(400);

        RFCProblemDTO rfcProblemDTO = postResponse.as(RFCProblemDTO.class);

        assertThat(rfcProblemDTO.getStatus().toString()).hasToString("400");
        assertThat(rfcProblemDTO.getInstance()).isNull();
        assertThat(rfcProblemDTO.getTitle()).isEqualTo(RFC_PROBLEM_TITLE_TECHNICAL_ERROR);
        assertThat(rfcProblemDTO.getType()).isEqualTo(RFC_PROBLEM_TYPE_VALIDATION_EXCEPTION);
    }

    @Test
    @DisplayName("Doesn't save book without category")
    public void testFailSaveBookWithoutCategory() {

        BookDTO newBook = setNewBookData();

        newBook.setBookCategory(null);

        Response postResponse = given()
                .body(newBook)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .when()
                .post("/books");
        postResponse.then().statusCode(400);

        RFCProblemDTO rfcProblemDTO = postResponse.as(RFCProblemDTO.class);

        assertThat(rfcProblemDTO.getStatus().toString()).hasToString("400");
        assertThat(rfcProblemDTO.getInstance()).isNull();
        assertThat(rfcProblemDTO.getTitle()).isEqualTo(RFC_PROBLEM_TITLE_TECHNICAL_ERROR);
        assertThat(rfcProblemDTO.getType()).isEqualTo(RFC_PROBLEM_TYPE_VALIDATION_EXCEPTION);
    }

    @Test
    @DisplayName("Doesn't save book without Author")
    public void testFailSaveBookWithoutAuthor() {

        BookDTO newBook = setNewBookData();

        newBook.setBookAuthor(null);

        Response postResponse = given()
                .body(newBook)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .when()
                .post("/books");
        postResponse.then().statusCode(400);

        RFCProblemDTO rfcProblemDTO = postResponse.as(RFCProblemDTO.class);

        assertThat(rfcProblemDTO.getStatus().toString()).hasToString("400");
        assertThat(rfcProblemDTO.getInstance()).isNull();
        assertThat(rfcProblemDTO.getTitle()).isEqualTo(RFC_PROBLEM_TITLE_TECHNICAL_ERROR);
        assertThat(rfcProblemDTO.getType()).isEqualTo(RFC_PROBLEM_TYPE_VALIDATION_EXCEPTION);
    }

    /* Delete Tests {DELETE} */

    @Test
    @DisplayName("Delete book")
    public void testSuccessDeleteBook() {
        Response response = given().
                when().delete("/books/" + REAL_BOOK_ID);
        response.then().
                statusCode(204);

        Response getResponse = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when().get("/books");
        getResponse.then()
                .statusCode(200);

        PageResultDTO<BookDTO> pageResultDTO = getResponse.as(getBookDtoTypeRef());
        assertThat(pageResultDTO.getTotalElements()).isEqualTo(REAL_BOOKS_AMOUNT - 1);

    }

    @Test
    @DisplayName("Doesn't delete book with fake ID")
    public void testFailDeleteBookWithFakeId() {
        Response response = given()
                .when().delete("/books/" + FAKE_BOOK_ID);
        RFCProblemDTO rfcProblemDTO = response.as(RFCProblemDTO.class);
        response.then()
                .statusCode(404);
        assertThat(rfcProblemDTO.getDetail()).isEqualTo(RFC_PROBLEM_DETAIL_ID_NOT_FOUND);
        assertThat(rfcProblemDTO.getTitle()).isEqualTo(RFC_PROBLEM_TITLE_TECHNICAL_ERROR);
        assertThat(rfcProblemDTO.getType()).isEqualTo(RFC_PROBLEM_TYPE_REST_EXCEPTION);
    }

    /* Update Tests {PUT} */

    @Test
    @DisplayName("Updates book")
    public void testSuccessUpdateBook() {

        BookDTO newBookData = setNewBookData();

        Response putResponse = given()
                .body(newBookData)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .when()
                .put("/books/" + REAL_BOOK_ID);

        BookDTO updatedBook = putResponse.body().as(BookDTO.class);

        Assertions.assertThat(updatedBook.getBookIsbn()).isEqualTo(NEW_BOOK_ISBN);
        Assertions.assertThat(updatedBook.getBookTitle()).isEqualTo(NEW_BOOK_TITLE);
        Assertions.assertThat(updatedBook.getBookPages()).isEqualTo(NEW_BOOK_PAGES);
        Assertions.assertThat(updatedBook.getBookCategory()).isEqualTo(NEW_BOOK_CATEGORY);

        Response getResponse = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/books");
        PageResultDTO<BookDTO> pageResultDTO = getResponse.as(getBookDtoTypeRef());
        Assertions.assertThat(pageResultDTO.getTotalElements()).isEqualTo(REAL_BOOKS_AMOUNT);
    }

    @Test
    @DisplayName("Doesn't update book by fake ID")
    public void testFailUpdateBookByFakeId() {

        BookDTO newBookData = setNewBookData();

        Response putResponse = given()
                .body(newBookData)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .when()
                .put("/books/" + FAKE_BOOK_ID);
        putResponse.then().statusCode(NOT_FOUND.getStatusCode());

        RFCProblemDTO rfcProblemDTO = putResponse.as(RFCProblemDTO.class);

        Assertions.assertThat(rfcProblemDTO.getStatus().toString()).hasToString("404");
        Assertions.assertThat(rfcProblemDTO.getDetail()).isEqualTo("Book not found.");
        Assertions.assertThat(rfcProblemDTO.getInstance()).isNull();
        Assertions.assertThat(rfcProblemDTO.getTitle()).isEqualTo("TECHNICAL ERROR");
        Assertions.assertThat(rfcProblemDTO.getType()).isEqualTo("REST_EXCEPTION");
    }

    @Test
    @DisplayName("Doesn't update book without category")
    public void testFailUpdateBookWithoutCategory() {

        BookDTO newBookData = setNewBookData();

        newBookData.setBookCategory(null);

        Response putResponse = given()
                .body(newBookData)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, APPLICATION_JSON)
                .when()
                .put("/books/" + REAL_BOOK_ID);

        putResponse.then().statusCode(BAD_REQUEST.getStatusCode());

        RFCProblemDTO rfcProblemDTO = putResponse.as(RFCProblemDTO.class);
        Assertions.assertThat(rfcProblemDTO.getStatus().toString()).hasToString("400");
        Assertions.assertThat(rfcProblemDTO.getInstance()).isNull();
        Assertions.assertThat(rfcProblemDTO.getTitle()).isEqualTo("TECHNICAL ERROR");
        Assertions.assertThat(rfcProblemDTO.getType()).isEqualTo("VALIDATION_EXCEPTION");
    }

    /* Private methods */

    private BookDTO setNewBookData() {
        BookDTO newBook = new BookDTO();
        newBook.setId(NEW_BOOK_ID);
        newBook.setBookIsbn(NEW_BOOK_ISBN);
        newBook.setBookTitle(NEW_BOOK_TITLE);
        newBook.setBookCategory(NEW_BOOK_CATEGORY);
        newBook.setBookPages(NEW_BOOK_PAGES);
        newBook.setBookAuthor(setExistingAuthorData());

        return newBook;
    }

    private AuthorDTO setExistingAuthorData() {
        AuthorDTO existingAuthor = new AuthorDTO();
        existingAuthor.setId(REAL_AUTHOR_ID);
        existingAuthor.setAuthorName(REAL_AUTHOR_NAME);
        existingAuthor.setAuthorSurname(REAL_AUTHOR_SURNAME);
        existingAuthor.setAuthorAge(REAL_AUTHOR_AGE);
        return existingAuthor;
    }

    private TypeRef<PageResultDTO<BookDTO>> getBookDtoTypeRef() {
        return new TypeRef<>() {
        };
    }
}
