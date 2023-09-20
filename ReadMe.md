# Url-Shortner

**Url-Shortner** is a scalable url shorter application.

## Architecture Features

* The application is dockerized, and also cloud ready.
* There is a Nginx sitting in front of the application as load balancer.
* There are two instances of application running(app1,app2).
* To create short url we need to send a POST to http://localhost:8080/api/v1/shortUrl
  ```
  curl -X POST \
  http://localhost:8080/api/v1/shortUrl \
  -H 'content-type: application/json' \
  -d '{
    "longUrl": "https://www.paycomonline.net/v4/ats/web.php/session/index/login",
    "alias": "https://test/"
  }'
  ```
* The response will be
  ```
  {
  "shortUrl": "https://test/6592ac"
  }
   ```
* The application will check for an existing entry in redis or else will create one and return the key(shorturl)
* Now from a short url(key) we can map the long url if that is already existing by sending a GET request
  to http://localhost:8080/api/v1/shortUrl/{key}
  ``` 
  curl -X GET \
  http://localhost:8080/api/v1/shortUrl/url/https://test/6592ac \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  ```
  * The response will be
    ```
    {
    "https://www.paycomonline.net/v4/ats/web.php/session/index/login"
    }
     ```
* The redis itself can be run as in distributed HA mode.
* The application itself is as well the redis can both scale.
* Swagger document can be found in

``` 
  http://localhost:8080/swagger-ui/index.html
``` 

* Actuator Prometheus endpoint is exposed via the following url

``` 
  http://localhost:8080/actuator/prometheus
  
  count(http_server_requests_seconds_count{uri="/api/v1/shortUrl/url/**"})
``` 

* Also we have included a docker image of promethus configured with the actuator api.

``` 
  http://localhost:9090/
``` 

### Stack

* Java 18
* Spring Boot 3
* JUnit
* Mockito
* maven
* Docker
* Redis
* Nginx
* Open API Swagger document
* Prometheus and Actuator for monitoring

### Setup and Run

* Build and install
  ```
    mvn clean install
  ```
* Deploy and run the application
  ```
    docker-compose up -d 
  ```
*  Some Class and their purpose
- UrlResource - Controller class providing apis to shorten url and also get the original one. Handles all the non error flows
- ShortUrlRequest, ShortUrlResponse - Used as dto to trnasfer data also provides type saftey.
- RedisConfig, RedisProperties - Related to redis setup.
- ShortUrlService - Holds the business logic to shorten the url. (From SOLID - SRS)
- RedisUrlRepository - I used redis as a key pair dristributed fast nosql storage. only deals with data storage and retrival.(From SOLID - SRS)
- UrlDetails -Actual bussiness object that holds the data.
- ErrorHandlingControllerAdvice - handles all the error flows.
- ErrorResponse - gives the business object for errors.
- UrlResourceTestIT - Integration test for testing end to end functionality.
- ShortUrlServiceTest, RedisUrlRepositoryTest - Unit test for each layers in a granular way.

* Architecture Diagram
  ```

  http request shorten url
         |
         |
         V
        Nginx
         |
         |
     ___________
    |           |
   App1        App2
    |           |
    ------------
        |
      Redis cluster

```
