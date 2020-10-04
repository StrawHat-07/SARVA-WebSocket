# SARVA-WebSocket
## WebSocket Game

### I have also created a simple basic UI for the game. After running the jar file run http://localhost:8080, 
### the UI at this endpoint will act as client and spring boot application running on terminal will act as the server

### Ensure that java8 and maven are installed in the system to run 
### You can ensure the installation of java8 on mac by


```bash
brew cask install adoptopenjdk/openjdk/adoptopenjdk8
```

### You can install maven by
```bash
brew install maven
```

```bash
git clone https://github.com/StrawHat-07/SARVA-WebSocket.git
```

### Ensure that you are checked out in the master branch of the repo. Use the following command
```bash
git checkout -b local origin/master
```

### Install all the dependencies using this command
```bash
mvn clean install
```

### Run the application using after succesful building using previous command.
```bash
java -jar target/messaging-stomp-websocket-0.0.1-SNAPSHOT.jar 
```

### Terminal will act as server and use localhost:8080 as client.
