package games.moegirl.sinocraft.sinocore.fabric.api.client.resource;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import games.moegirl.sinocraft.sinocore.SinoCore;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Load render type from model JSON like neoforge.
 * <p>
 * It is <b>not really same</b> as neoforge one:
 * In neoforge, it applies to model. Here it applies to block/item directly. <br/>
 * It assumes model {@code example:block/some_block} is for block {@code example:some_block}. <br/>
 * And items are always use translucent render type in fabric. See {@link BlockRenderLayerMap}.
 *
 * @author qyl27
 * @see <a href="https://docs.neoforged.net/docs/1.21.1/resources/client/models/#render-types">Neoforge Docs - Render Types</a>
 */
public class RenderTypeLoader extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {

    private static final Set<Block> EXCLUDED_BLOCKS = new HashSet<>();

    /**
     * Exclude blocks from being modified by this loader.
     * @param blocks Blocks to exclude.
     */
    public void exclude(Block... blocks) {
        EXCLUDED_BLOCKS.addAll(Arrays.asList(blocks));
    }

    private static final Map<Block, RenderType> MODIFIED_BLOCK_RENDER_TYPES = new HashMap<>();

    private RenderTypeLoader() {
        super(new Gson(), "models");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        profiler.push("sinocore_render_type_loader");

        Map<Block, RenderType> toModify = new HashMap<>();

        // Collect modifications.
        for (var entry : map.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();

            if (key.getPath().startsWith("block/")) {
                var id = ResourceLocation.fromNamespaceAndPath(key.getNamespace(), key.getPath().substring("block/".length()));
                var blockOptional = BuiltInRegistries.BLOCK.getOptional(id);
                if (blockOptional.isEmpty()) {
                    // Has no block with id, pass.
                    continue;
                }
                var block = blockOptional.get();
                if (EXCLUDED_BLOCKS.contains(block)) {
                    continue;
                }

                if (value instanceof JsonObject model) {
                    if (model.has("render_type")) {
                        var typeId = ResourceLocation.parse(model.get("render_type").getAsString());
                        RenderType renderType = getVanillaBlockRenderType(typeId);
                        if (renderType != null) {
                            toModify.put(block, renderType);
                        }
                    }
                }
            }
        }

        // Restore changed previous modifications.
        var it = MODIFIED_BLOCK_RENDER_TYPES.entrySet().iterator();
        while (it.hasNext()) {
            var entry = it.next();
            var block = entry.getKey();
            var renderType = entry.getValue();

            if (!toModify.containsKey(block)) {
                // Not modified now.
                var existing = ItemBlockRenderTypes.TYPE_BY_BLOCK.get(block);
                if (existing == renderType) {
                    // We modified before, but no need now, remove it.
                    ItemBlockRenderTypes.TYPE_BY_BLOCK.remove(block);
                }   // Else, somebody else modified it, leave it alone.
                it.remove();
            }

            var newRenderType = toModify.get(block);
            if (renderType == newRenderType) {
                // Same as before, skip.
                toModify.remove(block);
            }
        }

        // Apply new modifications.
        for (var entry : toModify.entrySet()) {
            var block = entry.getKey();
            var renderType = entry.getValue();

            var existing = ItemBlockRenderTypes.TYPE_BY_BLOCK.get(block);
            if (existing != null) {
                // It has a render type already, we back-off.
                continue;
            }

            BlockRenderLayerMap.INSTANCE.putBlock(block, renderType);
            MODIFIED_BLOCK_RENDER_TYPES.put(block, renderType);
        }

        profiler.pop();
    }

    private static final ResourceLocation SOLID = ResourceLocation.withDefaultNamespace("solid");
    private static final ResourceLocation CUTOUT = ResourceLocation.withDefaultNamespace("cutout");
    private static final ResourceLocation CUTOUT_MIPPED = ResourceLocation.withDefaultNamespace("cutout_mipped");
    private static final ResourceLocation CUTOUT_MIPPED_ALL = ResourceLocation.withDefaultNamespace("cutout_mipped_all");
    private static final ResourceLocation TRANSLUCENT = ResourceLocation.withDefaultNamespace("translucent");
    private static final ResourceLocation TRIPWIRE = ResourceLocation.withDefaultNamespace("tripwire");

    private static @Nullable RenderType getVanillaBlockRenderType(ResourceLocation id) {
        if (SOLID.equals(id)) {
            return RenderType.solid();
        } else if (CUTOUT.equals(id)) {
            return RenderType.cutout();
        } else if (CUTOUT_MIPPED.equals(id) || CUTOUT_MIPPED_ALL.equals(id)) {
            return RenderType.cutoutMipped();
        } else if (TRANSLUCENT.equals(id)) {
            return RenderType.translucent();
        } else if (TRIPWIRE.equals(id)) {
            return RenderType.tripwire();
        }

        return null;
    }

    public static final RenderTypeLoader INSTANCE = new RenderTypeLoader();

    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(SinoCore.MODID, "render_type_loader");

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }
}
