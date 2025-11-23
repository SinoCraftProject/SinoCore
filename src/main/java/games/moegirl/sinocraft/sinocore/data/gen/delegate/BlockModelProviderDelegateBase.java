package games.moegirl.sinocraft.sinocore.data.gen.delegate;

import games.moegirl.sinocraft.sinocore.data.gen.model.IBlockModelBuilder;
import games.moegirl.sinocraft.sinocore.data.gen.model.IModelProvider;
import net.minecraft.data.DataProvider;

@Deprecated(forRemoval = true, since = "1.2.0")
public abstract class BlockModelProviderDelegateBase<T extends IBlockModelBuilder<T>>
        extends ProviderDelegateBase<BlockModelProviderDelegateBase<T>>
        implements IModelProvider<T> {
    protected BlockModelProviderDelegateBase(DataProvider provider) {
        super(provider);
    }
}
