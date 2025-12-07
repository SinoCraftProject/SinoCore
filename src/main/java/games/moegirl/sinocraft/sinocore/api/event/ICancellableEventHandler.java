package games.moegirl.sinocraft.sinocore.api.event;

@FunctionalInterface
public interface ICancellableEventHandler<ARGS extends ICancellableArgs> extends IEventHandler<ARGS> {
}
