package games.moegirl.sinocraft.sinocore.data.gen.delegate;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Consumer;

@Deprecated(forRemoval = true, since = "1.2.0")
public abstract class DatapackProviderDelegateBase extends ProviderDelegateBase<DatapackProviderDelegateBase> {

    protected DatapackProviderDelegateBase(DataProviderBuilderBase<?, ?> builder) {
        super(builder);
    }

//    public abstract <T> void add(ResourceKey<? extends Registry<T>> type, Consumer<BootstapContext<T>> register);
}
