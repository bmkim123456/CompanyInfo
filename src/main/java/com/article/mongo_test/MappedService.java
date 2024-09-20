/*
package com.article.mongo_test;

import com.mongodb.client.MongoCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MappedService {

    @Autowired
    private MongoRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;


    public List<Mapped> getBatchMappedEntities() {
        List<Mapped> result = new ArrayList<>();

        int batchSize = 100;

        MongoCursor<Mapped> cursor = mongoTemplate.find(repository.findAll(), Mapped.class).iterator();

        while (cursor.hasNext()) {a*/
/**//*

            List<Mapped> batch = new ArrayList<>();

            for (int i = 0; i < batchSize && cursor.hasNext(); i++) {
                batch.add(cursor.next());
            }

            result.addAll(batch);
        }

        return result;
    }
}
*/
