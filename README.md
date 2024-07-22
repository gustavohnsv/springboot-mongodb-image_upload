# 🌐 Image Management in Database with Spring Boot Java Framework

- Link to access the base API url:
```
    localhost:8080/api/images/
```

- If you want test the application in your localhost, make sure to make a Cluster in mongoDB and follow this steps:

1. Make a `.env` file in root folder *(/image-upload)*

2. In the file, insert this line:
```
    MONGODB_URI=your-mongodb-uri
```

- Make sure you have a client for HTTP requests, like Insomnia or Postman
- Make sure you have Java 17 or later
- This project use Maven

# ⚙️ Using
<div style="display: flex; gap: 20px; align-items: center">

<img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original.svg" width="50"/>
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg" width="50"/>
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/maven/maven-original.svg" width="50"/> 
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/mongodb/mongodb-original.svg" width="50"/>

</div>

# 📶 Endpoints

```
    GET     localhost:8080/csrf-token
    GET     localhost:8080/api/images/
    GET     localhost:8080/api/images/image/?id={id}
    GET     localhost:8080/api/images/image/count/
    POST    localhost:8080/api/images/image/
    DELETE  localhost:8080/api/images/image/?id={id}
    HEAD    localhost:8080/api/images/image/?id={id}
    OPT     localhost:8080/api/images/image/
    
    to POST, DELETE, PATCH..., obtain 'csrf-token' first
    to API access, user='user' and password='password'
```

# 🌳 Repository Tree (maybe changed often)

```
.
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   └── org
    │   │       └── gustavohnsv
    │   │           └── imageupload
    │   │               ├── config
    │   │               │   ├── DotenvConfig.java
    │   │               │   ├── MongoConfig.java
    │   │               │   └── SecurityConfig.java
    │   │               ├── controller
    │   │               │   ├── CsrfTokenController.java
    │   │               │   └── ImageController.java
    │   │               ├── ImageUploadApplication.java
    │   │               ├── model
    │   │               │   ├── Image.java
    │   │               │   └── Message.java
    │   │               ├── repository
    │   │               │   └── ImageRepository.java
    │   │               └── service
    │   │                   └── ImageService.java
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── org
                └── gustavohnsv
                    └── imageupload
                        └── ImageUploadApplicationTests.java
```