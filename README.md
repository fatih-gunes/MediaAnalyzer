# Media Analyzer Application

This Spring Boot application analyzes MPEG4 Part 12 ISO Base Media files, such as MP4 files, and provides structured
information about their content.

## Prerequisites

Before running this application, ensure you have the following prerequisites:

- Java 17 or higher
- Maven (for building the application)
- `curl` (for making sample requests)

## Build and Run

1. Clone this repository to your local machine:

   ```bash
   git clone https://github.com/fatih-gunes/MediaAnalyzer.git
   cd MediaAnalyzer
2. Build the Spring Boot application using Maven:

   ```bash
   mvn clean install
3. Run the application
   ```bash
   java -jar target/media-analyzer.jar

This application should be running on http://localhost:8080.

## Usage

In order to get the detailed structure of an MP4 file, you can use curl to make a sample request to the following URL:

      curl -Uri "http://localhost:8080/api/media-data?url=https://demo.castlabs.com/tmp/text0.mp4"

Replace http://demo.castlabs.com/tmp/text0.mp4 with the URL of the MP4 file you want to analyze.

## Why is the Bonus Part Missing?

For the bonus part I needed to implement a solution with non-blocking IO by using Netty and Project Reactor. When I made
research on this, I realized that I needed to replace the rest controller with these libraries and make some more
configurations. Also, I needed to review the exception handling mechanism (of Spring) for returning suitable information
in case of a service failure. Since these are new subjects for me, it would take some time but today was the last date
for my submission. If this part is important for the rest of the recruitment process, and if you would like to give me
some more time, I can make these changes.