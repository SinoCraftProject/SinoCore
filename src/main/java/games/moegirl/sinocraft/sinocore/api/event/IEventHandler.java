package games.moegirl.sinocraft.sinocore.api.event;

import java.util.function.Consumer;

@FunctionalInterface
public interface IEventHandler<ARGS> extends Consumer<ARGS>, Comparable<IEventHandler<ARGS>> {
    default EventPriority getPriority() {
        return EventPriority.NORMAL;
    }

    @Override
    default int compareTo(IEventHandler<ARGS> other) {
        return Integer.compare(this.getPriority().getValue(), other.getPriority().getValue());
    }
}
