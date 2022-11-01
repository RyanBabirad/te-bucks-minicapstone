# Module 2 Capstone - TEBucks

Congratulations—you've landed a job with TEBucks, whose product is an online payment service for transferring "TE bucks" between friends. However, they don't have a product yet. You've been tasked with writing a RESTful API server and command-line application.

## Use cases

### Required use cases

You should attempt to complete all the following required use cases.

1. **[COMPLETE]** As a user of the system, I need to be able to register myself with a username and password.
   1. The ability to register has been provided in your starter code.
2. **[COMPLETE]** As a user of the system, I need to be able to log in using my registered username and password.
   1. Logging in returns an Authentication Token. I need to include this token with all my subsequent interactions with the system outside of registering and logging in.
   2. The ability to log in has been provided in your starter code.
3. A newly registered user should start with an initial balance of 1,000 TE Bucks.
4. As an authenticated user of the system, I need to be able to see my Account Balance.
5. As an authenticated user of the system, I need to be able to *send* a transfer of a specific amount of TE Bucks to a registered user.
   1. I should be able to choose from a list of users to send TE Bucks to.
   2. A transfer includes the User IDs of the from and to users and the amount of TE Bucks.
   3. The receiver's account balance is increased by the amount of the transfer.
   4. The sender's account balance is decreased by the amount of the transfer.
   5. I can't send more TE Bucks than I have in my account.
   6. I can't send a zero or negative amount.
   7. I must not be allowed to send money to myself.
   8. A Sending Transfer has an initial status of *Approved*.
6. As an authenticated user of the system, I need to be able to see transfers I have sent or received.
7. As an authenticated user of the system, I need to be able to retrieve the details of any transfer based upon the transfer ID.
8. As a Tech Elevator Banking System, I need to log specific transactions with the Tech Elevator Internal Revenue Service.
   1. I must log any transfer of at least $1,000 TE Bucks.
   2. I must log any transfer attempt that would result in an overdraft.
      - The Tech Elevator Internal Revenue Service has an API to facilitate logging. The documentation for the API is located at https://te-pgh-api.azurewebsites.net/. Use the appropriate TxLog endpoint(s) to accomplish this. 
      - The login credentials for the API are as follows:
         - username: `Team<XX>` where <XX> is a two digit number representing your team number
         - password: `password`
      - Login using the `/api/Login` endpoint. This endpoint will return a JWT token, which will be used in all subsequent requests to log information.

### Optional use cases

If you complete all the required use cases and are looking for additional challenge, complete as many of the following optional use cases as you can.

9. As an authenticated user of the system, I need to be able to *request* a transfer of a specific amount of TE Bucks from another registered user.
   1. I should be able to choose from a list of users to request TE Bucks from.
   2. I must not be allowed to request money from myself.
   3. I can't request a zero or negative amount.
   4. A transfer includes the User IDs of the from and to users and the amount of TE Bucks.
   5. A Request Transfer has an initial status of *Pending*.
   6. No account balance changes until the request is approved.
   7. The transfer request should appear in both users' list of transfers (use case #7).
10. As an authenticated user of the system, I need to be able to see my *Pending* transfers.
11. As an authenticated user of the system, I need to be able to either approve or reject a Request Transfer.
    1. I can't "approve" a given Request Transfer for more TE Bucks than I have in my account.
    2. The Request Transfer status is *Approved* if I approve, or *Rejected* if I reject the request.
    3. If the transfer is approved, the requester's account balance is increased by the amount of the request.
    4. If the transfer is approved, the requestee's account balance is decreased by the amount of the request.
    5. If the transfer is rejected, no account balance changes.

## Sample screens

### Use case 4: Current balance
```
Your current account balance is: $9999.99
```

### Use case 5: Send TE Bucks
```
-------------------------------------------
Users
ID          Name
-------------------------------------------
313         Bernice
54          Larry
---------

Enter ID of user you are sending to (0 to cancel):
Enter amount:
```

### Use case 6: View transfers
```
-------------------------------------------
Transfers
ID          From/To                 Amount
-------------------------------------------
23          From: Bernice          $ 903.14
79          To:    Larry           $  12.55
---------
Please enter transfer ID to view details (0 to cancel): "
```

### Use case 7: Transfer details
```
--------------------------------------------
Transfer Details
--------------------------------------------
 Id: 23
 From: Bernice
 To: Me Myselfandi
 Type: Send
 Status: Approved
 Amount: $903.14
```

### Use case 9: Requesting TE Bucks
```
-------------------------------------------
Users
ID          Name
-------------------------------------------
313         Bernice
54          Larry
---------

Enter ID of user you are requesting from (0 to cancel):
Enter amount:
```

### Use case 10: Pending requests
```
-------------------------------------------
Pending Transfers
ID          To                     Amount
-------------------------------------------
88          Bernice                $ 142.56
147         Larry                  $  10.17
---------
Please enter transfer ID to approve/reject (0 to cancel): "
```

### Use case 11: Approve or reject pending transfer
```
1: Approve
2: Reject
0: Don't approve or reject
---------
Please choose an option:
```

## How to set up the database

Create a new Postgres database called `tebucks`. Run the `database/tebucks.sql` script in pgAdmin to set up the database.

The very first task that you should complete after digesting the requirements is to build the remainder of database. Add the tables and data necessary for completing this project to the `database/tebucks.sql` script. Rerun the `database/tebucks.sql` script whenever would like to apply your changes.

### Datasource

A Datasource has been configured for you in `/src/resources/application.properties`.

```
# datasource connection properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tebucks
spring.datasource.name=tebucks
spring.datasource.username=postgres
spring.datasource.password=postgres1
```

### JdbcTemplate

If you look in `/src/main/java/com/techelevator/tebucks/dao`, you'll see `JdbcUserDao`. This is an example of how to get an instance of `JdbcTemplate` in your DAOs. If you declare a field of type `JdbcTemplate` and add it as an argument to the constructor, Spring automatically injects an instance for you:

```java
@Service
public class JdbcUserDao implements UserDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
```

## Testing


### DAO integration tests

`com.techelevator.tebucks.dao.BaseDaoTests` has been provided for you to use as a base class for any DAO integration test. It initializes a Datasource for testing and manages rollback of database changes between tests.

`com.techelevator.tebucks.dao.JdbUserDaoTests` has been provided for you as an example for writing your own DAO integration tests.

Remember that when testing, you're using a copy of the real database. The schema and data for the test database are defined in `/src/test/resources/test-data.sql`. The schema in this file matches the schema defined in `database/tebucks.sql`.

## API design
Provided

| Request Method | Path      | Request Body    | Returns          |
|----------------|-----------|-----------------|------------------|
| POST           | /login    | LoginDto        | LoginResponseDto |
| POST           | /register | RegisterUserDto | void             |


For you to complete

| Request Method | Path                       | Request Body            | Returns        |
|----------------|----------------------------|-------------------------|----------------|
| GET            | /api/account/balance       | N/A                     | BigDecimal     |
| GET            | /api/account/transfers     | N/A                     | List<Transfer> |
| GET            | /api/transfers/{id}        | N/A                     | Transfer       |
| POST           | /api/transfers             | NewTransferDto          | Transfer       |
| PUT            | /api/transfers/{id}/status | TransferStatusUpdateDto | Transfer       |
| GET            | /api/users                 | N/A                     | List<User>     |