package org.gustavohnsv.imageupload.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.cdimascio.dotenv.Dotenv;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "org.gustavohnsv.imageupload.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

    private final Dotenv dotenv;

    public MongoConfig(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    @NotNull
    @Override
    protected String getDatabaseName() { return "clients"; }

    @NotNull
    @Override
    @Bean
    public MongoClient mongoClient() {
        String mongoUri = dotenv.get("MONGODB_URI");
        if (mongoUri != null) {
            return MongoClients.create(mongoUri);
        }
        return MongoClients.create();
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }

}
