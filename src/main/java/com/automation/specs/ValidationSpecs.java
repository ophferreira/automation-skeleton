package com.automation.specs;

import com.automation.config.Configuration;
import com.automation.config.ConfigurationManager;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
public abstract class ValidationSpecs {

    protected final static Configuration configuration = ConfigurationManager.getConfiguration();
    /**
     * See more assertions
     * https://www.petrikainulainen.net/programming/testing/junit-5-tutorial-writing-assertions-with-hamcrest
     */
    protected final Response response;

    protected ValidationSpecs(final Response response) {
        this.response = response;
    }

    protected JsonPath jsonPath() {
        return response.getBody().jsonPath();
    }

    protected String getJsonValue(final String key) {
        return this.jsonPath().getString(key);
    }

    public void verifyResponseStatusValue(final Integer expectedCode) {

        if (expectedCode == null)
            throw new IllegalArgumentException("Expected code cannot be null.");

        assertThat(response.statusCode(), is(expectedCode));
    }

    public void validationSchema(final String schemaName, Integer expectedCode) {

        if (schemaName == null)
            throw new IllegalArgumentException("Schema name cannot be null.");

        response.then().assertThat().
                statusCode(expectedCode).
                body(matchesJsonSchema(this.getContract(schemaName)));
    }

    private File getContract(String contractName) {

        final File jsonSchemaFile = new File(configuration.contractPath().concat(contractName).concat(configuration.contractExtension()));

        if (!jsonSchemaFile.exists()) {
            throw new IllegalArgumentException("The schema is not exists in [ " + jsonSchemaFile + " ]");
        }

        return jsonSchemaFile;
    }

    public void verifyResponseKeyValues(final String key, final String value) {

        if (key == null)
            throw new IllegalArgumentException("Key cannot be null.");

        if (value == null)
            throw new IllegalArgumentException("Value cannot be null.");

        assertThat(getJsonValue(key), is(value));
    }

    public void verifyEmpty(final Object value) {

        if (value == null)
            throw new IllegalArgumentException("Value cannot be null.");

        assertThat(value, nullValue());
    }

    public void verifyTrue(final Boolean value) {

        if (value == null)
            throw new IllegalArgumentException("Value cannot be null.");

        assertThat(true, is(value));
    }

    public void verifyFalse(final Boolean value) {

        if (value == null)
            throw new IllegalArgumentException("Value cannot be null.");

        assertThat(false, is(value));
    }

    void verifyEqual(final String actual, final String expected) {

        if (actual == null)
            throw new IllegalArgumentException("Actual cannot be null.");

        if (expected == null)
            throw new IllegalArgumentException("Expected cannot be null.");


        assertThat(actual, is(expected));
    }

    void verifyReferToSameObject(final Object actual, final Object expected) {

        if (actual == null)
            throw new IllegalArgumentException("Actual cannot be null.");

        if (expected == null)
            throw new IllegalArgumentException("Expected cannot be null.");

        assertThat(actual, sameInstance(expected));
    }

    void validateSizeElements(final List<Object> list, final Integer size) {
        if (list.isEmpty())
            throw new IllegalArgumentException("List cannot be null.");

        if (size == null)
            throw new IllegalArgumentException("Size cannot be null.");

        assertThat(list, hasSize(size));
    }
}