package io.kk.bandhub.chatservice.repository;

import io.kk.bandhub.chatservice.model.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoChatRepository extends MongoRepository<Chat, String>, ChatRepository {

}
