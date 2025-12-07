package games.moegirl.sinocraft.sinocore.api.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;

public interface ICustomStatRegistry extends IRegistryBase<ResourceLocation> {
    ResourceLocation register(String name, StatFormatter statFormatter);

    default ResourceLocation register(String name) {
        return register(name, StatFormatter.DEFAULT);
    }
}
