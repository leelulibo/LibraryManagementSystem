package org.example;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.UUID;

public class User {
    private final String email;
    private final String name;
    private final UUID id;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.id = UUID.randomUUID();
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("email", email)
                .add("name", name)
                .add("id", id)
                .toString();
    }
}
