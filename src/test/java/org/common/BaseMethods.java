package org.common;


import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.pojo.Transaction;
import org.utils.Reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.is;

/**
 * BaseMethods class contains methods used to modularize code in order to keep cleaner test methods.
 *
 * @author je.sarmiento
 */
public class BaseMethods {

    protected ArrayList<Transaction> transactions;

    /**
     * Method to create 10 different transactions without repeated emails.
     *
     * @return ArrayLIst\<Transaction\>
     */
    public ArrayList<Transaction> initializePojo() {
        int i = 0;
        transactions = new ArrayList<>();
        while(i < 10) {
            Transaction newTransaction = fakerFill(new Transaction());
            if(i == 0) {
                transactions.add(newTransaction);
            }
            else if(!emailExists(newTransaction.getEmail(), transactions)) {
                transactions.add(newTransaction);
            }
            i++;
        }
        return transactions;
    }

    /**
     * Method to create a new Transaction object with random relevant data.
     *
     * @param transaction
     * @return Transaction
     */
    public Transaction fakerFill(Transaction transaction) {
        Faker faker = new Faker();
        String[] transactionTypes = new String[]{"deposit","withdraw","payment","invoice"};
        int idx = faker.random().nextInt(0,3);
        transaction.setName(faker.name().firstName());
        transaction.setLastName(faker.name().lastName());
        transaction.setAccountNumber(faker.number().randomNumber(9, true));
        transaction.setAmount(faker.number().randomDouble(2,1000, 9999));
        transaction.setTransactionType(transactionTypes[idx]);
        transaction.setEmail(faker.internet().emailAddress());
        transaction.setActive(faker.random().nextBoolean());
        transaction.setCountry(faker.address().country());
        transaction.setPhone(faker.phoneNumber().phoneNumber());
        return transaction;
    }

    /**
     * Method to determine if an email exists in an ArrayList of Transaction type objects.
     *
     * @param email
     * @param transactions
     * @return boolean
     */
    public boolean emailExists(String email, ArrayList<Transaction> transactions) {
        Stream<Transaction> transactionStream = transactions.stream();
        List<Transaction> foundIn = transactionStream.filter(t -> t.getEmail().equals(email))
                .collect(Collectors.toList());
        return (foundIn.size() > 0);
    }

    /**
     * Method that retreives a random email from an ArrayList of Transaction type objects
     *
     * @param transactions
     * @return String
     */
    public String getEmailFromList(ArrayList<Transaction> transactions) {
        Faker faker = new Faker();
        int idx = faker.random().nextInt(0,9);
        return transactions.get(idx).getEmail();
    }

    /**
     * Method that takes a JsonPath object and adds each element of the path to an ArrayList of Transaction type
     * objects.
     *
     * @param path
     * @return ArrayLIst\<Transaction\>
     */
    public ArrayList<Transaction> jsonToObject(JsonPath path) {
        List<Transaction> transactionHistory = path.getList("transactions", Transaction.class);
        transactions = new ArrayList<Transaction>(transactionHistory);
        return transactions;
    }

    /**
     * Method to determine if an endpoint is empty
     *
     * @param response
     * @return boolean
     */
    public boolean isEndpointEmpty(Response response) {
        JsonPath path = new JsonPath(response.asString());
        int size = path.getInt("transactions.size()");
        return size == 0;
    }

    public int elementsInEndpoint(Response response) {
        JsonPath path = new JsonPath(response.asString());
        int size = path.getInt("transactions.size()");
        return size;
    }

    /**
     * Method to delete all data from an endpoint.
     *
     * @param url
     * @param response
     */
    public void cleanEndpoint(String url, Response response) {
        ArrayList<Transaction> transactions = jsonToObject(response.jsonPath());
        Stream<Transaction> transactionStream = transactions.stream();
        transactionStream.forEach(t -> deleteRequest(url,t.getId()));
    }

    /**
     * Assertion method to compare actual and expected values
     *
     * @param description
     * @param actualValue
     * @param expectedValue
     * @param <T>
     */
    protected <T> void checkThat(String description, T actualValue, Matcher<? super T> expectedValue) {
        Reporter.info(format("Checking " + description.toLowerCase() + " [Expected: %s]", expectedValue));
        try {
            MatcherAssert.assertThat(actualValue,expectedValue);
        } catch (AssertionError e) {
            Reporter.error(format("Assertion error: [%s]", e.getMessage().replaceAll("\n", "")));
        }
    }

    /**
     * The distinctByKey() function uses a ConcurrentHashMap instance to find out if there is an existing key with
     * the same value – where the key is obtained from a function reference.
     * The parameter to this function is a lambda expression that is used to generate the map key for
     * making the comparison. This method was taken from https://howtodoinjava.com/java8/java-stream-distinct-examples/
     *
     * @return map
     * @author Lokesh Gupta
     */
    protected static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Simple GET request method that asserts the expected status code.
     *
     * @param url
     * @return Response
     */
    public Response getRequest(String url) {
        Reporter.info("Sendig GET request to the endpoint");
        Response response = given().get(url);
        checkThat("status code is 200", response.getStatusCode(), is(200));
        return response;
    }

    /**
     * Simple POST request method that asserts the expected status code.
     *
     * @param url
     * @param postBody
     * @return Response
     */
    public Response postRequest(String url,String postBody) {
        Reporter.info("Sending POST request to the endpoint");
        Response response = (Response) given().contentType("application/json").body(postBody).post(url);
        checkThat("status code is 201", response.getStatusCode(), is(201));
        return response;
    }

    /**
     * Simple PUT request method that asserts the expected status code.
     *
     * @param url
     * @param idToUpdate
     * @param putBody
     * @return Response
     */
    public Response putRequest(String url, int idToUpdate, String putBody) {
        Reporter.info("Sending PUT request to the endpoint to update transaction with id " + idToUpdate);
        Response response = given().contentType("application/json").body(putBody).put(url+idToUpdate);
        checkThat("status code is 200", response.getStatusCode(), is(200));
        return response;
    }

    /**
     * Simple DELETE request method that asserts the expected status code.
     * @param url
     * @param idToDelete
     * @return Response
     */
    public Response deleteRequest(String url, int idToDelete) {
        Reporter.info("Sending DELETE request to the endpoint to delete transaction with id " + idToDelete);
        Response response = given().delete(url+idToDelete);
        checkThat("status code is 200", response.getStatusCode(), is(200));
        return response;
    }
}
