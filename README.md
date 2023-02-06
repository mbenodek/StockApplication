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

##

Assumptions # 

1. While inserting the records the number of columns are of fixed size. Else it will throw an excpetion 
2. Duplicate entries are allowed to be uploaded through a file/inserted manually.
3. As of now there are no back buttons/links provided in the user interface.
4. In the code  there is restriction on the size of the file to be uploaded to be of 256MB

<b>Future design </b>

1. While uploading the files into the database if at any point there is failure a complete reload is needed in the current code.As part of future changes following provisions can be made 
   Maintain a temporary  file while uploading records 
   In the event of a failure read from the temporary file first and then continue with the other records when doing a re-try.
 This will enable in execution for only the failed records and not all of them.
2. A thread pool executor can be implemented to do the operations using multiple threads to increase the execution time for the applications 
3. An alternative approach would be sending the data via message drive architecture and 3 re-tires in case of failures. 
4. Reactive programing could be implemented so that the code flow would async.
5. Build a microservice application that uses Spring Cloud LoadBalancer to provide client-side load-balancing in calls to another microservice.
6. If the input is to be uploaded on daily basis from a location (NAS etc) the could be written in spring batch which would run on a daily basis.
7. The use of SAGA could be done which would work in scenarios where if one transaction fails for some reason, the saga executes compensating transactions(rollbacks) to undo the impact of the preceding transactions.
