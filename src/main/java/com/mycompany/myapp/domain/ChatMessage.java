package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * A Chat message.
 */
@Entity
public class ChatMessage extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 4096)
    @Column(name = "text", length = 4096, nullable = false)
    private String text;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm dd.MM.yyyy")
    @NotNull
    @Column(name = "time", length = 60, nullable = false)
    private Date time;

    @Size(min = 1)
    @Column(name = "user")
    private String user;

    @Size(min = 1)
    @NotNull
    @Column(name = "room")
    private String room;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatMessage)) {
            return false;
        }
        return id != null && id.equals(((ChatMessage) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "User{" +
            "text='" + text + '\'' +
            ", time='" + time + '\'' +
            ", user='" + user + '\'' +
            ", room='" + room + '\'' +
            "}";
    }
}
