# WellBe

## Introduction

### The WeeBe application is a social media-type app built on Ktor framework that allows users to exchange various content connected with mental health, motivation, psychology, and improving oneself. Users can share posts with texts, images, videos, and links, as well as discuss the content in the comment section

## See our tutorial posts:

Part 1 - [How to Build an Android Wellness App with the Ktor Framework. Part I: Backend](https://perpet.io/blog/how-to-build-an-android-wellness-app-with-the-ktor-framework-part-i-backend/)

### You can see out [wellbe_android](https://github.com/perpetio/wellbe_android), and notice how Ktor works on client side, with same plugins principes.

#### Ktor is an asynchronous framework for creating microservices, web applications, and more.
  - Kotlin and Coroutines
  - Lightweight and Flexible
  - Easy to set up
  - Easy to deploy and maintain
  - Assemble project only with plugins which you need
  - Can fully replace Spring Boot
  - Growing community, sufficient documentation

### Features include:
 - Registration/Login
 - Update user/avatar
 - Create/Edit/Remove post
 - Feed/Popular/Favourite/My posts
 - Like/Unlike post
 - Chats list/Join room/Leave Room
 - Send message (Notify chat members by WebSockets)
 - Search Filter and Pagination for posts requests
 - Deploy with Docker or Amazon Elastic Beanstalk with Fat JAR

### Ktor Features include:
* [JWTToken Security](<https://ktor.io/docs/jwt.html#add_dependencies>)
* [WebSockets](<https://ktor.io/docs/websocket.html#add_dependencies>)
* [Routing](<https://ktor.io/docs/routing-in-ktor.html>)
* [StatusPages](<https://ktor.io/docs/status-pages.html#install_plugin>)
* [ContentNegotiation](<https://ktor.io/docs/serialization.html>)
* [Ktorm ORM](<https://www.ktorm.org/api-docs/org.ktorm.database/-database/index.html>)
* [Koin for Ktor](<https://insert-koin.io/docs/reference/koin-ktor/ktor/>)

### Building

#### Before launching you need to adjust application.conf file : 

- on demand change application port 
 `ktor {
    deployment {
        port = 8082
        port = ${?PORT}
    }`
 - setup db with your own URl, Username, Password
`db {
   jdbcUrl = "jdbc:mysql://***********"
   jdbcDriver = "com.mysql.cj.jdbc.Driver"
   dbUser = "***********"
   dbPassword = "***********"
}`
- provide secret key for jwtToken
`jwt {
   secret = "***********"`
- replace AWS credentials with you own
`aws{
   accessKey = "***********"
   secretKey = "***********"
}`

#### With IntelliJ IDEA

The easiest way to build is to install [IntelliJ IDEA](https://www.jetbrains.com/idea/download) with Gradle. Once installed, then you can import the project into IntelliJ IDEA:

Import project with Git and Run from WellBeApplication main enty point

Then, Gradle will do everything for you.

#### With Gradle
https://ktor.io/docs/fatjar.html
