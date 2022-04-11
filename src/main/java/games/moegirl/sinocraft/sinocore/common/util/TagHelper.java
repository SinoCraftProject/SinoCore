package games.moegirl.sinocraft.sinocore.common.util;

import com.google.gson.JsonSyntaxException;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

/**
 * A helper to get tag or name
 */
public class TagHelper {

    /**
     * Get item tag
     * @param name tag name
     * @return item tag
     * @exception JsonSyntaxException if the tag is not existed
     * @deprecated use {@link ItemTags#create(ResourceLocation)}
     */
    @Deprecated(forRemoval = true)
    public static TagKey<Item> getItemTag(ResourceLocation name) {
        return ItemTags.create(name);
    }

    /**
     * Get fluid tag
     * @param name tag name
     * @return fluid tag
     * @exception JsonSyntaxException if the tag is not existed
     * @deprecated use {@link FluidTags#create(ResourceLocation)}
     */
    @Deprecated(forRemoval = true)
    public static TagKey<Fluid> getFluidTag(ResourceLocation name) {
        return FluidTags.create(name);
    }

    /**
     * Get fluid tag
     * @param name tag name
     * @return fluid tag
     * @exception JsonSyntaxException if the tag is not existed
     */
    public static TagKey<Fluid> getFluidTag(String name) {
        return FluidTags.create(new ResourceLocation(name));
    }

    /**
     * Get item tag name
     * @param tag item tag
     * @return tag name
     * @throws IllegalStateException if the tag is not existed
     * @deprecated use {@link TagKey#location()}
     */
    @Deprecated(forRemoval = true)
    public static ResourceLocation getItemId(TagKey<Item> tag) {
        return tag.location();
    }

    /**
     * Get fluid tag name
     * @param tag fluid tag
     * @return tag name
     * @throws IllegalStateException if the tag is not existed
     * @deprecated use {@link TagKey#location()}
     */
    @Deprecated(forRemoval = true)
    public static ResourceLocation getFluidId(TagKey<Fluid> tag) {
        return tag.location();
    }
}
