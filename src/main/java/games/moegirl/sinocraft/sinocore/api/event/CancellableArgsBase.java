package games.moegirl.sinocraft.sinocore.api.event;

import lombok.Getter;

public abstract class CancellableArgsBase implements ICancellableArgs {
    @Getter
    private boolean cancelled = false;

    @Override
    public void cancel() {
        cancelled = true;
    }
}
