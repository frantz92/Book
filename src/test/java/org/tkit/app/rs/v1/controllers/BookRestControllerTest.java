//package org.tkit.app.rs.v1.controllers;
//
//import io.quarkus.test.junit.QuarkusTest;
//import io.restassured.common.mapper.TypeRef;
//import io.restassured.response.Response;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.tkit.app.AbstractTest;
//import org.tkit.app.domain.models.enums.BookCategory;
//import org.tkit.app.rs.v1.models.BookDTO;
//import org.tkit.app.rs.v1.rfc.RFCProblemDTO;
//import org.tkit.quarkus.rs.models.PageResultDTO;
//import org.tkit.quarkus.test.WithDBData;
//
//import javax.ws.rs.core.MediaType;
//
//import static io.restassured.RestAssured.given;
//
//import static org.assertj.core.api.Assertions.*;
//
//@QuarkusTest
//@WithDBData(value = {"book-controller-test-data.xls"}, deleteBeforeInsert = true, rinseAndRepeat = true)
//class BookRestControllerTest extends AbstractTest {
//
//    private static final int DEFAULT_PAGE_SIZE = 100;
//    private static final int DEFAULT_TOTAL_PAGES = 1;
//
//    private static final int REAL_BOOKS_AMOUNT = 3;
//    private static final String REAL_BOOK_ID = "1";
//    private static final String REAL_BOOK_ISBN = "73-110";
//    private static final String REAL_BOOK_TITLE = "Long time";
//    private static final Integer REAL_BOOK_PAGES = 105;
//    private static final String REAL_AUTHOR_NAME = "JOHN";
//    private static final String REAL_AUTHOR_SURNAME = "WAYNE";
//    private static final Integer REAL_AUTHOR_AGE = 29;
//
//    private static final String FAKE_BOOK_ID = "100";
//
//    private static final String INCORRECT_BOOK_ID = "-2";
//
//    @Test
//    @DisplayName("Gets book by ID")
//    void testSuccessGetBookById() {
//        Response response = given()
//                .contentType(MediaType.APPLICATION_JSON)
//                .when()
//                .get("/books/" + REAL_BOOK_ID);
//        response.then()
//                .statusCode(200);
//        BookDTO book = response.body().as(BookDTO.class);
//        assertThat(book.getId()).isEqualTo(REAL_BOOK_ID);
//        assertThat(book.getBookIsbn()).isEqualTo(REAL_BOOK_ISBN);
//        assertThat(book.getBookTitle()).isEqualTo(REAL_BOOK_TITLE);
//        assertThat(book.getBookPages()).isEqualTo(REAL_BOOK_PAGES);
//        assertThat(book.getBookCategory()).isEqualTo(BookCategory.FANTASY);
//        assertThat(book.getBookAuthor().getAuthorName()).isEqualTo(REAL_AUTHOR_NAME);
//        assertThat(book.getBookAuthor().getAuthorSurname()).isEqualTo(REAL_AUTHOR_SURNAME);
//        assertThat(book.getBookAuthor().getAuthorAge()).isEqualTo(REAL_AUTHOR_AGE);
//    }
//
//    @Test
//    @DisplayName("Doesn't get book by ID")
//    void testFailGetBookByFakeID() {
//        Response response = given()
//                .contentType(MediaType.APPLICATION_JSON)
//                .when()
//                .get("/books/" + FAKE_BOOK_ID);
//        response.then()
//                .statusCode(404);
//        RFCProblemDTO rfcProblemDTO = response.as(RFCProblemDTO.class);
//        assertThat(rfcProblemDTO.getStatus().toString()).hasToString("404");
//        assertThat(rfcProblemDTO.getDetail()).isEqualTo("Book not found.");
//        assertThat(rfcProblemDTO.getInstance()).isNull();
//        assertThat(rfcProblemDTO.getTitle()).isEqualTo("TECHNICAL ERROR");
//        assertThat(rfcProblemDTO.getType()).isEqualTo("REST_EXCEPTION");
//    }
//
//    @Test
//    @DisplayName("Doesn't get book by incorrect ID")
//    void testFailGetBookByIncorrectID() {
//        Response response = given()
//                .contentType(MediaType.APPLICATION_JSON)
//                .when()
//                .get("/books/" + INCORRECT_BOOK_ID);
//        response.then()
//                .statusCode(404);
//        RFCProblemDTO rfcProblemDTO = response.as(RFCProblemDTO.class);
//        assertThat(rfcProblemDTO.getStatus().toString()).hasToString("404");
//        assertThat(rfcProblemDTO.getDetail()).isEqualTo("Book not found.");
//        assertThat(rfcProblemDTO.getInstance()).isNull();
//        assertThat(rfcProblemDTO.getTitle()).isEqualTo("TECHNICAL ERROR");
//        assertThat(rfcProblemDTO.getType()).isEqualTo("REST_EXCEPTION");
//    }
//
//    @Test
//    @DisplayName("Finds all books by no criteria")
//    void testSuccessFindBooksByNoCriteria() {
//        Response response = given()
//                .contentType(MediaType.APPLICATION_JSON)
//                .when()
//                .get("/books");
//        response.then().statusCode(200);
//        PageResultDTO pageResultDTO = response.as(PageResultDTO.class);
//        assertThat(pageResultDTO.getSize()).isEqualTo(DEFAULT_PAGE_SIZE);
//        assertThat(pageResultDTO.getTotalElements()).isEqualTo(REAL_BOOKS_AMOUNT);
//        assertThat(pageResultDTO.getStream().size()).isEqualTo(REAL_BOOKS_AMOUNT);
//        assertThat(pageResultDTO.getTotalPages()).isEqualTo(DEFAULT_TOTAL_PAGES);
//    }
//
//    @Test
//    @DisplayName("Finds all books by fake criteria")
//    void testSuccessFindBooksByFakeCriteria() {
//        Response response = given()
//                .when()
//                .queryParam("fakeParam", "fakeValue")
//                .get("/books");
//        response.then().statusCode(200);
//        PageResultDTO pageResultDTO = response.as(PageResultDTO.class);
//        assertThat(pageResultDTO.getSize()).isEqualTo(DEFAULT_PAGE_SIZE);
//        assertThat(pageResultDTO.getTotalElements()).isEqualTo(REAL_BOOKS_AMOUNT);
//        assertThat(pageResultDTO.getTotalPages()).isEqualTo(DEFAULT_TOTAL_PAGES);
//        assertThat(pageResultDTO.getStream().size()).isEqualTo(REAL_BOOKS_AMOUNT);
//    }
//
//    @Test
//    @DisplayName("Finds book by title")
//    void testSuccessFindBookByTitle() {
//        Response response = given()
//                .when()
//                .queryParam("bookTitle", REAL_BOOK_TITLE)
//                .get("/books/");
//        response.then().statusCode(200);
//
//        PageResultDTO<BookDTO> pageResultDTO = response.as(getBookDtoTypeRef());
//        assertThat(pageResultDTO.getTotalElements()).isEqualTo(1);
//
//        BookDTO book = pageResultDTO.getStream().get(0);
//        assertThat(book.getId()).isEqualTo(REAL_BOOK_ID);
//        assertThat(book.getBookIsbn()).isEqualTo(REAL_BOOK_ISBN);
//        assertThat(book.getBookTitle()).isEqualTo(REAL_BOOK_TITLE);
//        assertThat(book.getBookPages()).isEqualTo(REAL_BOOK_PAGES);
//        assertThat(book.getBookCategory()).isEqualTo(BookCategory.FANTASY);
//        assertThat(book.getBookAuthor().getAuthorName()).isEqualTo(REAL_AUTHOR_NAME);
//        assertThat(book.getBookAuthor().getAuthorSurname()).isEqualTo(REAL_AUTHOR_SURNAME);
//        assertThat(book.getBookAuthor().getAuthorAge()).isEqualTo(REAL_AUTHOR_AGE);
//    }
//
//    @Test
//    @DisplayName("Finds books by first two letters of title")
//    void testSuccessFindBooksByFirstTwoLettersOfTitle() {
//        Response response = given()
//                .when()
//                .queryParam("bookTitle", REAL_BOOK_TITLE.substring(0, 2))
//                .get("/books/");
//        response.then().statusCode(200);
//
//        PageResultDTO<BookDTO> pageResultDTO = response.as(getBookDtoTypeRef());
//        assertThat(pageResultDTO.getTotalElements()).isEqualTo(1);
//
//        BookDTO book = pageResultDTO.getStream().get(0);
//        assertThat(book.getId()).isEqualTo(REAL_BOOK_ID);
//        assertThat(book.getBookIsbn()).isEqualTo(REAL_BOOK_ISBN);
//        assertThat(book.getBookTitle()).isEqualTo(REAL_BOOK_TITLE);
//        assertThat(book.getBookPages()).isEqualTo(REAL_BOOK_PAGES);
//        assertThat(book.getBookCategory()).isEqualTo(BookCategory.FANTASY);
//        assertThat(book.getBookAuthor().getAuthorName()).isEqualTo(REAL_AUTHOR_NAME);
//        assertThat(book.getBookAuthor().getAuthorSurname()).isEqualTo(REAL_AUTHOR_SURNAME);
//        assertThat(book.getBookAuthor().getAuthorAge()).isEqualTo(REAL_AUTHOR_AGE);
//    }
//
//    private TypeRef<PageResultDTO<BookDTO>> getBookDtoTypeRef() {
//        return new TypeRef<>() {
//        };
//    }
//}
