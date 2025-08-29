
package com.boilerplate.feature.chat.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ChatUserId implements Serializable {
    private UUID chat;
    private UUID user;

    public ChatUserId() {}

    public ChatUserId(UUID chat, UUID user) {
        this.chat = chat;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatUserId that)) return false;
        return Objects.equals(chat, that.chat) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chat, user);
    }
}

