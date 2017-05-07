# Cubscout
Cubscout is an application for scouting matches in FIRST Robotics Competition. Scouting is the process of tracking the performance of other robotics teams in their matches in order to make educated decisions. Cubscout focuses on alliance selection, wherin the higher-ranking teams after qualifying rounds choose teams for their alliance of three to compete in the playoffs.

# Motivation
Many teams, ours formerly included, use paper scouting forms and simple spreadsheets for scouting. This is an extremely cumbersome process and locks teams in to tracking what they expected to be important at the time they printed off the scouting sheets. Cubscout aims to allow teams to track detailed information that is important to them and change what information is relevent without losing the progress they made in earlier matches.

Many of the existing scouting solutions did very little actual data processing, instead serving purely as data collection to be later used in a spreadsheet. While this alleviates some of the logistical problems of the conventional paper+Excel method, spreadsheets are not a convienient data processing method, especially when a team wants to change how the data is processed. Cubscout's primary purpose is to do the hard work in processing scouting information while providing the flexibility possible in spreadsheet-based methods.

# Contributing
CubScout is currently in the pre-release stage. You can [create a new issue](https://github.com/robocubs4205/cubscout-server/issues) for proposed features, bugs, etc. as well as comment on [existing issues](https://github.com/robocubs4205/cubscout-server/issues). You can also contribute code by creating a fork of the repository with the fork button at the top of the page. You can make your changes to your fork then [create a pull request](https://github.com/robocubs4205/cubscout-server/pulls) to have your changes integrated into the project. Take a look at the [issues](https://github.com/robocubs4205/cubscout-server/issues) for information on what needs to be done, and please [create a new issue](https://github.com/robocubs4205/cubscout-server/issues) and discuss it with a core team member before starting on a feature that does not already have an issue.

# Modules
this repository contains multiple modules for the different components of CubScout.

## REST API Server
This component performs the data analysis and collection and exposes the functionality of CubScout via a REST API. This module is contained in the `rest` directory of the repository and is the `rest` gradle project.

## Web app.
This component is the official front end of CubScout. It creates a convenient user interface for the functionality of the CubSout server. It is contained in the `webapp` directory of the project ans is the `webapp` gradle project.

# Building
This project uses gradle for builds. The repository comes with a gradle wrapper script that allows you to build the project without installing gradle. Since this is a java-based project, java must be installed.

To Build the REST API server, run `./gradlew :rest:build` from the repository's root directory.
To Build the web app, run `./gradlew :webapp:build` from the repository's root directory. Building the web app requires npm and angular cli to be installed

# Running
Both the web app and REST API server build into war archives. These can be ran in any Java servlet container, such as Apache Tomcat. The default configuration for the REST API uses an in-memory database that does not save any data. In order to use a real backing database, you must add an application.properties or application.yaml in the directory the war is in before starting your servlet container. These files will override configuration properties and will be specific to your database. See [the spring documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html)

The Web app needs to have the `environment.ts` modified before building to specify the URL for the REST API server.

# License
This project is licensed under the [Mit License](https://github.com/robocubs4205/cubscout-server/blob/develop/LICENSE).
