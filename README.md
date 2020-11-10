[![Join the chat at https://gitter.im/microservice-catalog/community](https://badges.gitter.im/microservice-catalog/community.svg)](https://gitter.im/microservice-catalog/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![CircleCI](https://circleci.com/gh/tillias/microservice-catalog.svg?style=shield)](https://circleci.com/gh/tillias/microservice-catalog)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=microcatalog&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=microcatalog)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=microcatalog&metric=coverage)](https://sonarcloud.io/dashboard?id=microcatalog)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=microcatalog&metric=alert_status)](https://sonarcloud.io/dashboard?id=microcatalog)
[![microservice-catalog](https://img.shields.io/endpoint?url=https://dashboard.cypress.io/badge/simple/ge12oz&style=flat&logo=cypress)](https://dashboard.cypress.io/projects/ge12oz/runs)
[![microservice-catalog](https://img.shields.io/endpoint?url=https://dashboard.cypress.io/badge/count/ge12oz&style=flat&logo=cypress)](https://dashboard.cypress.io/projects/ge12oz/runs)
[![This project is using Percy.io for visual regression testing.](https://percy.io/static/images/percy-badge.svg)](https://percy.io/bb9225f6/microservice-catalog)
[![Docker Image Version (latest by date)](https://img.shields.io/docker/v/tillias/microcatalog)](https://hub.docker.com/r/tillias/microcatalog)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=microcatalog&metric=ncloc)](https://sonarcloud.io/dashboard?id=microcatalog)

[![Try in PWD](https://raw.githubusercontent.com/play-with-docker/stacks/master/assets/images/button.png)](https://labs.play-with-docker.com/?stack=https://raw.githubusercontent.com/tillias/microservice-catalog/master/docker-compose.yml)

# microcatalog

Microservice catalog helps you or your organization to centrally store information about your microservices and share information between developers in a modern, live way. See [original idea](https://tillias.wordpress.com/2020/10/02/microservice-catalog) and [blog posts](https://tillias.wordpress.com/tag/microservice-catalog) for details

Interested or have suggestions? Join [gitter chat](https://gitter.im/microservice-catalog/community) or ping me on [twitter](https://twitter.com/tillias)

## Check it yourself

Latest version and deployment instructions are always available on [Docker Hub](https://hub.docker.com/r/tillias/microcatalog).

You can also test it with only one mouse click on [Play with Docker](https://labs.play-with-docker.com/?stack=https://raw.githubusercontent.com/tillias/microservice-catalog/master/docker-compose.yml)

Or if you prefer kubernetes:

```
kubectl apply -k src\main\k8s
```

## Development

This application was generated using JHipster 6.10.3, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v6.10.3](https://www.jhipster.tech/documentation-archive/v6.10.3).

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

We use npm scripts and [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```

./mvnw


npm start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

### PWA Support

JHipster ships with PWA (Progressive Web App) support, and it's turned off by default. One of the main components of a PWA is a service worker.

The service worker initialization code is commented out by default. To enable it, uncomment the following code in `src/main/webapp/index.html`:

```html
<script>
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('./service-worker.js').then(function () {
      console.log('Service Worker Registered');
    });
  }
</script>
```

Note: [Workbox](https://developers.google.com/web/tools/workbox/) powers JHipster's service worker. It dynamically generates the `service-worker.js` file.

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

```
npm install --save --save-exact leaflet
```

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

```
npm install --save-dev --save-exact @types/leaflet
```

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Edit [src/main/webapp/app/vendor.ts](src/main/webapp/app/vendor.ts) file:

```
import 'leaflet/dist/leaflet.js';
```

Edit [src/main/webapp/content/scss/vendor.scss](src/main/webapp/content/scss/vendor.scss) file:

```
@import '~leaflet/dist/leaflet.css';
```

Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.module.ts
```

## Building for production

### Packaging as jar

To build the final jar and optimize the microcatalog application for production, run:

```

./mvnw -Pprod clean verify


```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```

java -jar target/*.jar


```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```

./mvnw -Pprod,war clean verify


```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

### Client tests

Unit tests are run by [Jest][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

```
npm test
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a postgresql database in a docker container, run:

```
docker-compose -f src/main/docker/postgresql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/postgresql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./mvnw -Pprod verify jib:dockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 6.10.3 archive]: https://www.jhipster.tech/documentation-archive/v6.10.3
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v6.10.3/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v6.10.3/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v6.10.3/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v6.10.3/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v6.10.3/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v6.10.3/setting-up-ci/
[node.js]: https://nodejs.org/
[yarn]: https://yarnpkg.org/
[webpack]: https://webpack.github.io/
[angular cli]: https://cli.angular.io/
[browsersync]: https://www.browsersync.io/
[jest]: https://facebook.github.io/jest/
[jasmine]: https://jasmine.github.io/2.0/introduction.html
[protractor]: https://angular.github.io/protractor/
[leaflet]: https://leafletjs.com/
[definitelytyped]: https://definitelytyped.org/
