package model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Email {

    private int ID;
    private String sender;
    private String receiver;
    private String subject;
    private String content;
    private LocalDateTime timeStamp;


}
