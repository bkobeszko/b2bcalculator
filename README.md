# B2B calculator
## Find out how much you will earn at your own business in Poland!
A B2B income calculator with Polish tax rules. 

Built with Java, powered by [Spring Boot](https://spring.io/projects/spring-boot), money calculation by [Joda Money](https://www.joda.org/joda-money/), presented as a single page, mobile-first app by [Thymeleaf](https://www.thymeleaf.org/) and [Semantic UI](https://semantic-ui.com/), all running on top of a [Jetty](http://www.eclipse.org/jetty/) app server.
Tested by [TestNG](http://testng.org) & [AssertJ](http://joel-costigliola.github.io/assertj) duo.

You can check this code, which is running on https://kalkulatorb2b.beskode.pl

### Compilation
This is a classic Maven app. This is based on Spring Boot, so popular IDEs will open, compile and run it seamlessly.
However, the quicker way is to run one of these Maven commands:

Full build:
```sh
mvn clean package
```

After one full build you can build without frontend Semantic UI components:
```sh
mvn clean package -Dskip.npm -Dskip.gulp
```

Without unit tests:
```sh
mvn clean package -Dmaven.test.skip=true
```

Without frontend and unit tests:
```sh
mvn clean package -Dskip.npm -Dskip.gulp -Dmaven.test.skip=true
```

### Development
Watch and build frontend file at runtime during frontend development and debugging (for Unix systems use /):
```sh
cd src\main\frontend
gulp semantic_ui_watch
```

### Running
```sh
cd target
java -jar b2bcalculator-1.1.2.jar
```
Then open in web browser:
```sh
http://localhost:5000
```



Brought to you by [BESKODE](https://beskode.pl).
Author does not take any responsibility for using calculation results in tax settlements.
