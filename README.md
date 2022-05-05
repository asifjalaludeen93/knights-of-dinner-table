# Knights of the Dinner Table   

## Tech information
- Java 11(Amazon Corretto)
- Maven 3.6.3
- Log4j 2.13


## Solution
- Getting the input file with the happiness gain/lose score among each person
- Extract the names of the people at the table
- Calculate the happiness score between each and every person at the table by extracting the score from the input file
- Store the relation and their happiness score in a map
- Get the permutation of all the persons
- Find the optimal seating score by iterating through all the permutations and adding the scores.


## Installation
```sh
git clone git@github.com:asifjalaludeen93/knights-of-dinner-table.git
mvn clean package
java -jar target\AdventOfCode-1.0-jar-with-dependencies.jar {input-file-path}
```
