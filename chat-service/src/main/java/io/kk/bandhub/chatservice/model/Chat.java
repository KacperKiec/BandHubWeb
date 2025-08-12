package io.kk.bandhub.chatservice.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Getter
@Setter
@Document(collection = "chat")
@TypeAlias("Chat")
public class Chat {

    @Id
    private String id;

    @NotNull
    @NotEmpty
    @Size(min = 2)
    @Field("participants")
    private Set<Integer> participants;
}
