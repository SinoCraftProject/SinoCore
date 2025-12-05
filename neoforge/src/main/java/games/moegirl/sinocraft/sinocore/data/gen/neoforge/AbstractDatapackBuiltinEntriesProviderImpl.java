package games.moegirl.sinocraft.sinocore.data.gen.neoforge;

import games.moegirl.sinocraft.sinocore.data.gen.DataGenContext;
import games.moegirl.sinocraft.sinocore.data.gen.delegate.DatapackProviderDelegateBase;
import games.moegirl.sinocraft.sinocore.data.gen.neoforge.impl.NeoForgeDatapackProviderDelegate;

@Deprecated(forRemoval = true, since = "1.2.0")
public class AbstractDatapackBuiltinEntriesProviderImpl {

    public static DatapackProviderDelegateBase createDelegate(DataGenContext context) {
        if (context instanceof NeoForgeDataGenContext impl) {
            return new NeoForgeDatapackProviderDelegate(impl);
        }
        throw new ClassCastException("Can't cast " + context + " to ForgeDataGenContextImpl at Forge Platform. " +
                "Use SinoCorePlatform#buildDataGeneratorContext to create this context. " +
                "Don't use context implemented yourself, because it contains different information in different platform");
    }
}
