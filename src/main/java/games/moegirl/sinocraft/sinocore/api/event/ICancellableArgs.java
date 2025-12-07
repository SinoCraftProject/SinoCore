package games.moegirl.sinocraft.sinocore.api.event;

public interface ICancellableArgs {
    void cancel();

    boolean isCancelled();
}
