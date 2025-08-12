package io.kk.bandhub.chatservice.repository;

import io.kk.bandhub.chatservice.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoMessageRepository extends MongoRepository<Message, String>, MessageRepository {

}
