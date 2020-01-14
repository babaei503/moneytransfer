# RESTful API for money transfers between accounts

```
1.Using Jersey
2.Using Jetty
3.Including test
```

```
In this project, I used the Jersey library for developing RESTful APIs for money transfers between accounts. I used the optimistic and pessimistic lock to make sure the balance of each account is consistent in case the API is invoked by multiple systems and services on behalf of end-users.
```
```
I Used the Jetty server with a minimum of 10 and a maximum of 100 QueuedThreadPool.
```
```
I used Junit for testing the AccountService for creating an account, closing account, disposing, and withdrawal account. I applied JerseyTest for testing RESTful endpoints, and also I benefited from ConcurrentTestRunner for testing the "transfer" REST endpoint with 15 concurrent threads.
```


