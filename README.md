## File Upload,Insert single record and search for a record



## Getting Started
**1. Clone the repository**
```bash
git clone https://github.com/mbenodek/StockApplication.git
```
**2. Specify your database**

Open `src/main/resources/application.properties` file and change following properties accordingly.

```properties
spring.data.mongodb.port = [db port]
spring.data.mongodb.host = [host]
spring.data.mongodb.database = [database name]
server.port=24103


```


```properties
##Database Properties mapped to docker-compose.yaml

spring.data.mongodb.authentication-database=[]
spring.data.mongodb.username=[]
spring.data.mongodb.password=[]


```

## REST end-points
* Upload a File: `http://localhost:24103/upload`
* Search a record :`http://localhost:24103/searchRecords?name='AA'`
* Insert a record : `http://localhost:24103/recordUpload`



##
The application can be accessed at `http://localhost:24103` or open `src/main/resources/static/index.html` to access frontend

![File upload frontend snapshot](/src/main/resources/images/frontend.png)



