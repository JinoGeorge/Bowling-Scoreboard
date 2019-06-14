# Bowling Scoreboard
This is an implementation of the Bowling scoreboard calculator.
For simplicity, it is implemented for only one player.
The rules of the scoreboard calculations are as mentioned in the following link:
* https://slocums.homestead.com/gamescore.html

#### How to build and run the application
1.Clone the project repository from Github
 ~~~~
 $ git clone https://github.com/JinoGeorge/Bowling-Scoreboard.git
 ~~~~
2.From the project base directory:
 ~~~~
 $ mvn install
 ~~~~
3.Run the project using spring-boot maven plugin
 
~~~~
 $ mvn spring-boot:run
~~~~
4.From the browser navigate to localhost:8080/

#### Tools used
1. Spring boot 2.1 framework for Backend
2. Thymeleaf for Frontend
3. Spring data mongodb for persistence layer
4. Embedded mongodb as database
5. Junit 5 for testing
6. Maven build system
