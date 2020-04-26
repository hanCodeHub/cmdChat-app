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

The frontend files are located in the resources/static directory and uses plain HTML, CSS, and Javascript. The frontend dependencies are defined as CDN links in the head of `index.html`, so no additional installation required. Due to this and the web-based nature of the app, please run the application while connected to the internet.

### How to run

Install all the dependencies defined in the pom.xml file. Any Java IDE will do this for you. Then execute the following:

`mvn spring-boot:run` will compile, build and run the application.

Or within the IDE run the `CmdChatApplication` class directly.

This will start the application as a web service.

`mvn clean` will remove all the output binaries.

To use the application, visit `http://localhost:8080/`

If you want to chat with yourself, you can use a different browser and login with a 2nd GitHub account. **Note:** opening a separate tab/window of the same browser is not enough, because OAuth2 relies on tokens stored as browser cookies to enable Single Sign-On.

### Software design

