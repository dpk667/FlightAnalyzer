# FlightAnalyzer

This Java program analyzes flight data from a json file, providing insights such as the minimum flight time for each airline between Vladivostok and Tel Aviv and the difference between the average price and median for flights between these cities.

## Installation

Clone the repository:
```bash
git clone https://github.com/dpk667/FlightAnalyzer.git
```

Navigate to the project directory:
```bash
cd flight-analyzer
```

Compile the program:
```bash
javac org/example/AppRunner.java
```

## Usage

Run the program from the command line with the following command:

```bash
java -cp . org.example.AppRunner path/to/tickets.json
```
Replace __path/to/tickets.json__ with the actual path to your __json file containing flight data__

## Results
The program calculates and prints the following:

Minimum flight time for each airline.
Difference between the average price and median for flights between Vladivostok and Tel Aviv.
Results are displayed in the console.

## Dependencies
The project uses the Gson library for json parsing. The necessary dependency is included in the Maven configuration


