package com.automation.steps;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.*;
import io.cucumber.java.en.Given;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class DataTableStepDefinitions {

    private final Author expectedAuthor = new Author("Annie M. G.", "Schmidt", "1911-03-20");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @DefaultParameterTransformer
    @DefaultDataTableEntryTransformer(headersToProperties = true)
    @DefaultDataTableCellTransformer
    public Object defaultTransformer(Object fromValue, Type toValueType) {
        return objectMapper.convertValue(fromValue, objectMapper.constructType(toValueType));
    }

//    @DataTableType
//    public Author authorEntryTransformer(Map<String, String> entry) {
//        return new Author(
//                entry.get("firstName"),
//                entry.get("lastName"),
//                entry.get("birthDate"));
//    }


    @DataTableType(replaceWithEmptyString = "[blank]")
    public Publication publicatioDataTable(Map<String, String> entry) {
        return new Publication(
                entry.get("name"),
                entry.get("first publication"));
    }

    @Given("some publication")
    public void given_some_publication(List<Publication> publications){
        log.info(publications.toString());

    }

    @Given("a list of authors in a table")
    public void aListOfAuthorsInATable(List<Author> authors) {
        assertTrue(authors.contains(expectedAuthor));
    }

    @Given("a table with title case headers")
    public void aTableWithCapitalCaseHeaders(List<Author> authors) {
        assertTrue(authors.contains(expectedAuthor));
    }

    @Given("a single currency in a table")
    public void aSingleCurrencyInATable(Currency currency) {
        assertThat(currency, is(Currency.getInstance("EUR")));
    }

    @Given("a currency in a parameter {}")
    public void aCurrencyInAParameter(Currency currency) {
        assertThat(currency, is(Currency.getInstance("EUR")));
    }

    @DataTableType
    public User invertedDataTable(Map<String, String> entry){
        return new User(
                entry.get("firstname"),
                entry.get("lastname"),
                entry.get("nationality")
        );
    }

    @Given("the user is")
    public void the_user_is(@Transpose User user){
        log.info("The user is {}", user);
    }

    @Data
    @AllArgsConstructor
    public static class User {

        String firstname;
        String lastname;
        String nationality;

    }
    @Data
    @AllArgsConstructor
    public static class Publication {
        String name;
        String publication;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Author {
        String firstName;
        String lastName;
        String birthDate;
    }

    private final LocalDate expected = LocalDate.of(1907, 11, 14);

    @ParameterType("([0-9]{4})-([0-9]{2})-([0-9]{2})")
    public LocalDate iso8601Date(String year, String month, String day) {
        return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    }

    @Given("today is {iso8601Date}")
    public void today_is(LocalDate date) {
        assertEquals(expected, date);
    }
}
