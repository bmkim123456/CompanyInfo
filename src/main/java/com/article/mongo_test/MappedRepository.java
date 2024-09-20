package com.article.mongo_test;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MappedRepository extends MongoRepository<Mapped, String> {


}
