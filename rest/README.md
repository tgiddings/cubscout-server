# REST API
CubScout exposes a REST API for applications, including the [official web frontend](https://github.com/robocubs4205/cubscout-server/tree/develop/webapp), to access its functions. CubScout is in a pre-release state. As such, it is recommended to wait to create an application using the CubScout api until it is released.

# REST Basics
REST takes the basic operations on data (create, read, update, and delete) and matches them to some of the HTTP verbs (POST, GET, PUT and PATCH, and DELETE). REST also requires the api to be **stateless**, meaning that you do not need to access different URLs in a particular order to perform a task. Further, each "resource" that you will be working with (e.g. a game, an event, a match, etc.) is one URL, and each URL is one resource. REST is a style of web API that is very popular do to its ease of use and practicality.

Many REST APIs, including CubScout's, use JSON for both requests and responses. Cubscout's responses also embrace the hideously-named but useful HATEOAS, wherein responses return links to its related resources. This removes the need for your software to construct URLs based on e.g. the IDs of various resources. Leveraging HATEOAS while consuming the CubScout API will make your application more tolerant of API updates. Many complex operations can be viewed and executed as walking through a series of resources, following links.

example response
```
{
  "shortName":"Mount Vernon",
  "address":"314 N 9th St, Mt Vernon, WA 98273",
  "startDate":1489809600000,
  "endDate":1489896000000,
  "links":[
    {
      "rel":"self",
      "href":"https://api.beta.robocubs4205.com/events/2"
    },
    {
      "rel":"district",
      "href":"https://api.beta.robocubs4205.com/districts/PNW"
    },
    {
      "rel":"game",
      "href":"https://api.beta.robocubs4205.com/games/1"
    },
    {
      "rel":"matches",
      "href":"https://api.beta.robocubs4205.com/events/2/matches"
    },
    {
      "rel":"results",
      "href":"https://api.beta.robocubs4205.com/events/2/results"
    }
  ],
  "id":2
}
```
(Note that actual server responses will not be this nicely formatted, but libraries for using JSON will interpret the responses identically)

For many REST APIs, updates to a resource are specified using a format known as JSON Patch sent to the server using the PATCH verb. CubScout uses a simplified version this approach. JSON Patch specifies which values are changed, added, removed, etc.

example request for changing an events name and address:
```
[
  {"op": "replace", "path": "/shortName", "value": "Glacier Peak"},
  {"op": "replace", "path": "/address", "value": "7401 144th Pl SE, Snohomish, WA 98296"}
]
```

