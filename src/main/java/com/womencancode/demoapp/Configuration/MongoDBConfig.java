package com.womencancode.demoapp.Configuration;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
public class MongoDBConfig extends AbstractMongoConfiguration {

    private String databaseName;
    private String host;
    private int port;

    public MongoDBConfig(
            @Value("${spring.data.mongodb.database:wwc}") String databaseName,
            @Value("${spring.data.mongodb.host:localhost}") String host,
            @Value("${spring.data.mongodb.port:27017}") int port
    ) {
        this.databaseName = databaseName;
        this.host = host;
        this.port = port;
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(host, port);
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }
}
