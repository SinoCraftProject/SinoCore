package games.moegirl.sinocraft.sinocore.data.gen.model;

import net.minecraft.resources.ResourceLocation;

/**
 * 描述一个模型文件
 */
@Deprecated(forRemoval = true, since = "1.2.0")
public interface IModelFile {

    ResourceLocation getLocation();

    ResourceLocation getUncheckedLocation();
}
