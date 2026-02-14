# Web app for displaying data from HDC1080

Datasheet for HDC1080 can be found here:

[Link](https://www.ti.com/lit/ds/symlink/hdc1080.pdf?ts=1620137207479&ref_url=https%253A%252F%252Fwww.ti.com%252Fproduct%252FHDC1080)

# Used technologies
* Java 17, PostgreSQL, Thymeleaf, HTML, CSS, ApexCharts, Bootstrap, Docker.

# How it works
Data from HDC1080 is periodically fetched from Home Assistant via REST API. 
Then it is saved in PostgreSQL database.

The web app connects with database using Spring Data and JPQL queries.
From there it reads temperature and humidity data as well as measurement timestamps. 
This app uses charts from ApexCharts which are described [here](https://apexcharts.com/).

The data is then displayed in a table (last 60 measurements).
![Alt text](SCREENSHOTS/tabela.png?raw=true "Title")

The temperature and humidity from last 6h and last 24h is also displayed on subsequent pages.
Below you can see chart with data from the last 6 hours. 
![Alt text](SCREENSHOTS/wykres6h.png?raw=true "Title")

# How to run

1. Start database:
cd src/main/resources/database/changelog/
docker-compose -f postgres1.yml up -d

2. Run liquibase scripts:
mvn resources:resources liquibase:update -P dev


