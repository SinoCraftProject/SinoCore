package games.moegirl.sinocraft.sinocore.api.event;

import lombok.Getter;

public enum EventPriority {
    HIGH(3),
    NORMAL(2),
    LOW(1),
    ;

    @Getter
    private final int value;

    EventPriority(int value) {
        this.value = value;
    }
}
