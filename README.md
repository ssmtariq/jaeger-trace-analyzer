# jaeger-trace-analyzer
#### Presequisite
* Jdk-1.8
* Maven-3.6+

#### 1. Clone the Repository
```bash
git clone https://github.com/ssmtariq/jaeger-trace-analyzer.git
cd jaeger-trace-analyzer/
```

#### 2. Build the Application
```bash
mvn clean package
```
#### 3. Execute analysis using the application
```bash
java -jar target/jaeger-trace-analyzer-0.0.1-SNAPSHOT.jar
```
#### 4. Save analysis to a file
```bash
java -jar target/jaeger-trace-analyzer-0.0.1-SNAPSHOT.jar >  output.txt
```