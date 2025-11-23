package games.moegirl.sinocraft.sinocore.data.gen;

import dev.architectury.injectables.annotations.ExpectPlatform;
import games.moegirl.sinocraft.sinocore.data.gen.delegate.LanguageProviderDelegateBase;

@Deprecated(forRemoval = true, since = "1.2.0")
public abstract class AbstractLanguageProvider extends NeoForgeDataProviderBase<LanguageProviderDelegateBase> {

    protected final String modId;

    public AbstractLanguageProvider(DataGenContext context, String locale) {
        super(createDelegate(context, locale));
        this.modId = context.getModId();
    }

    @Override
    public String getModId() {
        return modId;
    }

    @ExpectPlatform
    public static LanguageProviderDelegateBase createDelegate(DataGenContext context, String locale) {
        throw new AssertionError();
    }
}
