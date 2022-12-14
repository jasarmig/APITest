package org.tests;

import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.common.BaseMethods;
import org.pojo.Transaction;
import org.testng.annotations.*;
import org.utils.Reporter;

import java.util.ArrayList;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class ApiTests extends BaseMethods {

    /**
     * Test method that verifies if the endpoint is empty, if data is found it erases the data and leaves the endpoint
     * empty.
     *
     * @param url
     */
    @Test
    @Parameters({"url"})
    public void cleanEndpoint(String url) {
        Response response = getRequest(url);
        boolean isEmpty = isEndpointEmpty(response);
        if(!isEmpty){
            Reporter.info("Found data at the endpoint, cleaning endpoint");
            cleanEndpoint(url, response);
            Reporter.info("All data has been wiped from the endpoint.");
            Reporter.info("Sending new request to verify endpoint is empty");
            response = getRequest(url);
            isEmpty = isEndpointEmpty(response);
        }
        checkThat("the endpoint has no stored data",isEmpty,is(true));
    }
    /**
     * Test method that adds 10 transactions to the endpoint verifying there are no duplicate emails.
     *
     * @param url
     */
    @Test
    @Parameters({"url"})
    public void initializeEndpointData(String url) {
        Reporter.info("Getting transactions from endpoint");
        Response response = getRequest(url);
        int initialTransactions = elementsInEndpoint(response);
        Reporter.info("Initial transaction count is: " + initialTransactions);
        JsonPath path = new JsonPath(response.asString());
        Reporter.info("Initializing 10 new transactions with random data");
        ArrayList<Transaction> transactions = initializePojo(jsonToObject(path));
        Stream<Transaction> transactionStream = transactions.stream();
        Reporter.info("Posting new transactions to endpoint");
        transactionStream.forEach(t -> postRequest(url,t.toString()));
        path = new JsonPath(getRequest(url).asString());
        checkThat("endpoint has 10 more elements",path.getInt("transactions.size()"),is(10 + initialTransactions));
    }

    /**
     * Test method that gets elements from the endpoint and verifies they don't have duplicate emails
     *
     * @param url
     */
    @Test
    @Parameters({"url"})
    public void noDuplicateEmails(String url) {
        Response response = getRequest(url);
        ArrayList<Transaction> transactions = jsonToObject(response.jsonPath());
        Stream<Transaction> transactionStream = transactions.stream();
        Reporter.info("Counting total transactions");
        int countTransactions = (int) transactionStream.count();
        transactionStream = transactions.stream();
        Reporter.info("Counting different emails");
        int countEmails = (int) (int) transactionStream
                .filter(distinctByKey(Transaction::getEmail))
                .count();
        checkThat("number of emails matches number of transactions", countEmails, is(countTransactions));
    }

    /**
     * Test method that updates an account number, the method selects randomly al element to update, updates the
     * element, and then it makes a new GET request to verify the account number was updated.
     *
     * @param url
     */
    @Test
    @Parameters({"url"})
    public void updateAccountNumber(String url) {
        Faker faker = new Faker();
        Reporter.info("Generating random account number");
        long newAccountNumber = faker.number().randomNumber(9, true);
        String jBody = "{\"accountNumber\": \"" + newAccountNumber + "\"}";
        Reporter.info("Randomly selecting one of the first 10 transactions to update the account number");
        int idToUpdate = faker.number().numberBetween(1,10);
        Reporter.info(idToUpdate + " is the id to update");
        putRequest(url,idToUpdate,jBody);
        Reporter.info("Requesting transaction with id " + idToUpdate);
        Response response = getRequest(url+idToUpdate);
        JsonPath path = new JsonPath(response.asString());
        Long fromApi = path.getLong("accountNumber");
        checkThat("account number was updated",newAccountNumber,is(fromApi));
    }

    /**
     * Simple annotation to log the beginning of a new test.
     */
    @BeforeMethod
    public void reportTestInit() {
        Reporter.info("******************** Test Initiated ********************");
    }

    /**
     * Simple annotation to log the end of a test leaving an empty line for readability.
     */
    @AfterMethod
    public void reportTestEnd() {
        Reporter.info("******************** End of Test ********************");
        Reporter.info("");
    }

}
