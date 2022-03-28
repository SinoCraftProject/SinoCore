package games.moegirl.sinocraft.sinocore.utility;

import com.google.gson.JsonSyntaxException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.SerializationTags;
import net.minecraft.tags.Tag;
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
     */
    public static Tag<Item> getItemTag(ResourceLocation name) {
        return SerializationTags.getInstance()
                .getTagOrThrow(Registry.ITEM_REGISTRY, name, (t) -> new JsonSyntaxException("Unknown item tag '" + t + "'"));
    }

    /**
     * Get item tag
     * @param name tag name
     * @return item tag
     * @exception JsonSyntaxException if the tag is not existed
     */
    public static Tag<Item> getItemTag(String name) {
        return getItemTag(new ResourceLocation(name));
    }

    /**
     * Get fluid tag
     * @param name tag name
     * @return fluid tag
     * @exception JsonSyntaxException if the tag is not existed
     */
    public static Tag<Fluid> getFluidTag(ResourceLocation name) {
        return SerializationTags.getInstance()
                .getTagOrThrow(Registry.FLUID_REGISTRY, name, (t) -> new JsonSyntaxException("Unknown fluid tag '" + t + "'"));
    }

    /**
     * Get fluid tag
     * @param name tag name
     * @return fluid tag
     * @exception JsonSyntaxException if the tag is not existed
     */
    public static Tag<Fluid> getFluidTag(String name) {
        return getFluidTag(new ResourceLocation(name));
    }

    /**
     * Get item tag name
     * @param tag item tag
     * @return tag name
     * @throws IllegalStateException if the tag is not existed
     */
    public static ResourceLocation getItemId(Tag<Item> tag) {
        return SerializationTags.getInstance()
                .getIdOrThrow(Registry.ITEM_REGISTRY, tag, () -> new IllegalStateException("Unknown item tag"));
    }

    /**
     * Get fluid tag name
     * @param tag fluid tag
     * @return tag name
     * @throws IllegalStateException if the tag is not existed
     */
    public static ResourceLocation getFluidId(Tag<Fluid> tag) {
        return SerializationTags.getInstance()
                .getIdOrThrow(Registry.FLUID_REGISTRY, tag, () -> new IllegalStateException("Unknown fluid tag"));
    }
}
