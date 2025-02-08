package ca.mcgill.ecse321.eventregistration.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Registration {
    @EmbeddedId
    private Key key;

    protected Registration() {}

    public Registration(Key key) {
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    // Class for composite key of registration
    @Embeddable
    public static class Key implements Serializable {
        @ManyToOne
        private Event event;
        @ManyToOne
        private Person person;

        public Key() {}

        public Key(Event event, Person person) {
            this.event = event;
            this.person = person;
        }

        public Event getEvent() {
            return event;
        }

        public Person getPerson() {
            return person;
        }

        @Override
        public boolean equals(Object obj) {
            //New pattern matching feature in java 16
            if (!(obj instanceof Key other)) {
                return false;
            }

            return ((this.getPerson().getId() == other.getPerson().getId()) &&
                    (this.getEvent().getId() == other.getEvent().getId()));
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getPerson().getId(), this.getEvent().getId());
        }
    }
}

