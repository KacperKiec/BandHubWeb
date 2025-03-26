package io.kk.bandhub.chatservice.model;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@Document(collection = "message")
@TypeAlias("Message")
public class Message {

    @Id
    private String id;

    @NotNull
    @Field(name = "sender_id")
    private Integer sender;

    @NotNull
    @Field(name = "receiver_id")
    private Integer receiver;

    @NotNull
    @Field(name = "content")
    private String content;

    @NotNull
    @Field(name = "timestamp")
    private Date timestamp;

    @NotNull
    @Field(name = "is_received")
    private Boolean isReceived;
}
