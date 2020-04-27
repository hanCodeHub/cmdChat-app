# CS665 Term Project - Han Xu


### Description

This project is a web-based chat application titled 'cmdChat'. Multiple users can login using OAuth2 authentication via GitHub. Once logged in, the user's GitHub username will be the chat name that appears next to their messages. The user can then proceed with joining a channel, after which they may begin chatting with the other users subscribed to the same channel. Everyone in the channel can see each other's messages, and will be notified when a new user joins or leaves.

Planned for future release:
- dynamic creation of multi-channels
- direct messaging between users via typed commands
- file attachment support via typed commands
    
The design choices used in this project takes into consideration these current and future requirements.


### Major dependencies

This project is developed with Java JDK 11, although the features used should work with JDK 8 as well. For the backend, it also uses the following key dependencies and defines them in pom.xml.

- Spring Boot web framework
- Spring Boot security 
- Spring Boot OAuth2 client
- Spring Boot web sockets
- Spring Boot Data (JPA + Hibernate)
- H2 in-memory database

The frontend files are located in the resources/static directory and uses plain HTML, CSS, and Javascript. The frontend dependencies are defined as CDN links in the head of `index.html`, which requires no additional installation. Due to this and the web-based nature of the app, please run the application while connected to the internet.


### How to run

Install all the dependencies defined in the pom.xml file. Any Java IDE will do this for you. Then execute the following:

`mvn spring-boot:run` will compile, build and run the application.

Or within the IDE run the `CmdChatApplication` class directly.

This will start the application with an embedded HTTP server (Tomcat).

To use the application, visit `http://localhost:8080/`

If you want to chat with yourself, you can use a different browser and login with a 2nd GitHub account. **Note:** Opening a separate tab/window of the same browser is not enough, because OAuth2 relies on tokens stored as browser cookies to enable Single Sign-On.


### Software design

Due to the frameworks and libraries used in this project, some design patterns do not follow a traditional textbook implementation. For example, the repository interfaces do not need classes to implement them, and @Autowired components do not need explicit instantiation. For more info on how this works, please visit [Spring Beans and Dependency Injection](https://docs.spring.io/spring-boot/docs/current/reference/html/using-spring-boot.html#using-boot-spring-beans-and-dependency-injection).

Even though this project is a more real-world implementation, the benefits of the following design patterns remain the same. Also, it may be obvious that a chat application would benefit from the Observer pattern. In this rather simple project however, the broadcasting/messaging logic is already handled by the web socket framework and the STOMP protocol.

#### Model-View-Controller (MVC)

The general architecture of this application follows the MVC pattern for a greater separation of concerns between the Model, View, and Controller layers. This is because in a typical web application, there is a lot of data flow between the UI (View), the REST endpoint handlers (Model), and the domain classes (Model). This is even more the case when it needs to support chat features via web sockets, as the data flow then becomes bi-directional (server may also initiate). The MVC design allows each layer to scale by itself, thereby reducing the complexity in each layer.

For example, when a user sends a message in the View, the `PublicChatController` Controller broadcasts the update to all other subscribers, and uses `TextMessage` to create the message in the Model. When a user successfully logs in, it triggers the `AuthEventListener` Controller, which calls `User` to create the user in the Model.

In the future, the application may need to add more elements in the View that trigger messages, or add more business logic to Model, or update existing endpoints in the Controller. Without the MVC pattern, the application becomes tightly coupled and hard to maintain. With it, each layer can grow independently and still communicate with each other. Also, since the Controller classes handle the communication between them, the representation of data is completely decoupled from the domain data itself, which makes the application more robust and secure.

The View logic in this project is implemented in Javascript, which is the modern standard for web frontends. Model classes are in the model package. Controller classes are in the controller package.

#### Repository

The main goal of using the Repository pattern is to avoid duplication of query code. Thanks to this pattern, all of the database access logic is now centralized within classes/interfaces that relate to domain classes. For example, the Model layer of this application has `TextMessageRepo` interface that provides database CRUD methods for `TextMessage` objects, and `UserRepo` interface does the same for `User` objects. 

These interfaces extend `JpaRepository` interface from the Spring Boot Data library, so no concrete classes are needed. In fact, it already comes with basic CRUD methods such as `save()`, `findById()`, etc. Custom SQL can be added directly to the repository interface, as seen in `UserRepo`:

```java
@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    /* custom query to find a User by name */
    @Query(value = "SELECT * FROM users WHERE name = ?1", nativeQuery = true)
    User findByName(String name);
    
    /* custom query to find a User by oauthClientId */
    @Query(value = "SELECT * FROM users WHERE oauth_client_id = ?1", nativeQuery = true)
    User findByOauthClientId(Integer oauthClientId);
}
```

Now any class make use of these queries by autowiring the repository:

```java
@RestController
public class UserExample {
    @Autowired UserRepo userRepo;

    exampleMethod() {
        User savedUser = userRepo.save(user);
    }
}
```

This design has multiple benefits for the program:
- Data query/update code does not have to be duplicated in classes that need access to the database.
- It's easier to read and change all the query code in a single place for each domain class.
- It hides the complexity of SQL queries and provides easy to use API methods for client code.

#### Builder

The Builder pattern solves the problem of constructing complex objects that have many optional properties. Java does not support optional parameters, so the constructor has to be overloaded to achieve the same effect. For objects with many properties, this becomes hard to maintain and parameter code has to be duplicated for each constructor.

In the Model layer, TextMessageBuilder is used to construct TextMessage objects (Product), and UserBuilder does the same for User objects. The Builder class holds references to the same properties as the Product class and each method allows the client to set an optional property like so:

```java
// Abbreviated version
public class UserBuilder {
    private Integer oauthClientId;
    private String password;
    
    // constructor sets mandatory property such as name //
    
    /** Optional builder method to add oauthClientId to User product. */
    public UserBuilder withOauthClientId(Integer oauthClientId) {
        this.oauthClientId = oauthClientId;
        return this;
    }
    
    /** Optional builder method to add password to User product. */
    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }
}
```

Finally, the client calls the `build()` method to return a User with the exact properties suitable for the situation. In the `AuthEventListener` class for example, when a user successfully authenticates via the OAuth2 3rd party client (GitHub), a user did not register with a password. The `UserBuilder` provides this flexibility:

```
User user = new UserBuilder(oauthUserName).withOauthClientId(oauthUserId).build();
```

In a future release, the application will have a registration form with a password field. Then the client can simply call `withPassword()` while building the User object. 
 
 The TextMessageBuilder is also important for future releases, when the application needs to attach other properties to messages such as files, channels or commands. By adding a method to both the Builder and the Product classes, the client can just call `withFile()` or `withCommand` while building the message.
 
 That's why this pattern greatly simplifies the construction process by providing the client with flexible methods. It makes it easier for others to work with the domain classes, because they don't have to memorize the order of constructor parameters as an example. 

**Note:** This implementation of the Builder pattern does not have separate classes defined for each Product property, because that would increase the number of classes too much for now.

#### Callback

### References

links to libraries and resources.