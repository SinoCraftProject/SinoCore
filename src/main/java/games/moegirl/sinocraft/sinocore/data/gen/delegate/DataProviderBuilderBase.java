package games.moegirl.sinocraft.sinocore.data.gen.delegate;

import net.minecraft.data.DataProvider;

@Deprecated(forRemoval = true, since = "1.2.0")
public abstract class DataProviderBuilderBase<D extends ProviderDelegateBase<? super D>, P extends DataProvider> {

    protected D delegate;

    public <T> T bind(ProviderDelegateBase<?> delegate) {
        this.delegate = (D) delegate;
        return (T) this;
    }

    public P build() {
        delegate.generateData();
        return build(delegate);
    }

    public abstract P build(D delegate);

    public abstract String getDataProviderName();
}
