# Homework task for Nurlan Moldabekov from EpicGames

The task was quite interesting. Thank you.

### How to start the application

We have to be in the application folder. To create jar file:

```
./gradlew clean build
```

To build Docker image:

```
sudo docker build --tag=country-service:latest .
```

To start Docker container:

```
sudo docker run -p8080:8080 country-service:latest
```

The endpoint can be reached by this path:
http://localhost:8080/api/v1/country/continent?countries=CA,US

### Task

Design and implement a production-ready REST API that accepts a list of country codes and returns a list of country
codes that are in the same continent as the country code input.

Use the endpoint at https://countries.trevorblades.com/graphql to get the up-to-date data required for you to implement
your API.

[Bonus Task] In order to not overwhelm the server, add a rate limit of 5 requests per sec for unauthenticated users and
20 requests per second for authenticated users. You may use Docker to include and integrate any containers that you
might need for this.

List any assumptions that you make.

e.g. for input with "CA" and "US",

the expected output is:

```
{
  continent: [{
    countries: ["CA", "US"],
    name: "North America",
    otherCountries: ["AG", "AI", "AW", "BB", ...] // should not have CA or US here
  }]
}
```

