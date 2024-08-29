# 🌐 Image Management in Database with Spring Boot Java Framework
<br>

- Link to access the base API url:
```
    localhost:8080/api/images/
```

- Link to acess the site url:
```
    localhost:8080/api/
```
---
# ⚙️ Program Language & Tools
<br>
<div style="display: flex; flex-direction: column; gap: 20px; justify-content: center">
    <div>
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg" width="50"/> - Java
    </div>
    <div>
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original.svg" width="50"/> - Spring Boot
    </div>
    <div>
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/maven/maven-original.svg" width="50"/> - Maven
    </div>
    <div>
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/mongodb/mongodb-original.svg" width="50"/> - MongoDB
    </div>
    <div>
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/docker/docker-original.svg" width="50"/> - Docker
    </div>
</div>

---
# 🐋 How to run with Docker
<br>

- Generate application `.jar` file:
``` 
    ./mvnw clean package
```
- Build Docker application image
```
    docker build -t <image-name> .
```
- Run Docker image
```
    docker run --env-file .env -p 8080:8080 <image-name>
```
---
# 📶 Endpoints
<br>

```
    # Site Endpoints
    GET     localhost:8080/api/
    GET     localhost:8080/api/img/

    # API Endpoints
    GET     localhost:8080/csrf-token
    GET     localhost:8080/api/images/
    GET     localhost:8080/api/images/image/?id={id}
    GET     localhost:8080/api/images/image/download/?id={id}
    GET     localhost:8080/api/images/image/count/
    POST    localhost:8080/api/images/image/
    DELETE  localhost:8080/api/images/image/?id={id}
    HEAD    localhost:8080/api/images/image/?id={id}
    OPT     localhost:8080/api/images/image/
    
    to POST, DELETE, PATCH..., obtain 'csrf-token' first
    to API ADMIN access, user='admin' and password='password'
    to API USER acess, user='user' and password='password'
```
---
# 🌳 Repository Tree (maybe changed often)
<br>

```
.
├── Dockerfile
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
    │   │               │   ├── ImageController.java
    │   │               │   └── SiteController.java
    │   │               ├── ImageUploadApplication.java
    │   │               ├── model
    │   │               │   ├── Image.java
    │   │               │   └── Message.java
    │   │               ├── repository
    │   │               │   └── ImageRepository.java
    │   │               ├── service
    │   │               │   ├── ImageService.java
    │   │               │   └── ResolutionType.java
    │   │               └── util
    │   │                   └── ImageCompressionUtil.java
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       │   └── css
    │       │       └── styles.css
    │       └── templates
    │           ├── Home.html
    │           └── Images.html
    └── test
        └── java
            └── org
                └── gustavohnsv
                    └── imageupload
                        └── ImageUploadApplicationTests.java
```