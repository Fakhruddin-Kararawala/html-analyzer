# Scout24 technical challenge

The challenge is to build a web application that allows user to conduct some analysis of an HTML web document.


### Tech Stack
The tech stack I choose to use are list as follow:
* Java 8
* Library
  * Jsoup (HTML Parser)
  * Guava (Util library)
  * Apache commons-validator (Util library)
* Spring-boot (Application)
* Thymeleaf (Template engine)
* Bootstrap (CSS)
* Maven 3

### Run the Application

You can package and run the backend application by executing the following command:

```
#!bash
mvn package spring-boot:run
```

This will start an instance running on the default port `8080`.

You can also build a single executable JAR file that contains all the necessary dependencies, classes, and resources, and run that.
The JAR file will be create by executing the command `mvn package`, and the JAR file will be located under target directory.

Then you can run the JAR file:
```
java -jar target/html-analyzer-1.0-SNAPSHOT.jar
```

After successfully running the application, you can now open your browser with the following url:
 ```
 http://localhost:8080/analyzer
 ```

 Now you can start analyzing the url you want. For example: `https://github.com/`.As state in the problem statement.

### Application Design

In this section I will explain more about the application design. 

##### Identify Login form
In order to identify the login form exists in the web document or not, I look for the type of input. 
If `type=password` exists, then I assume there is a login form in this web document.  

##### Resource Validation
A web document can contain massive links. 
Performance matter a lot if we want to validate these resources.
  
I used a `ExecutorService` as a thread-pool to improve the performance. 
Thread size is set to 5 (`#CPU +1` is optimal on average). As per my system requirement.
      
Each thread will take a subset of the collected hyperlink, and check if the resource are reachable.
The way to know this is to check the HttpResponse Code.

Consider the effect of redirection, I simply set the connection object not to allow redirection.   

```
HttpURLConnection.setFollowRedirects(false);
```

If the hyperlink does not go through HTTP protocol(Example: SMTP), the response code will return `-1`.
  
  

