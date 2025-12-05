package games.moegirl.sinocraft.sinocore.data.gen.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

/**
 * Moved to {@link games.moegirl.sinocraft.sinocore.datagen.advancement.AdvancementTree}
 */
@Deprecated(forRemoval = true, since = "1.2.0")
public class AdvancementTree extends games.moegirl.sinocraft.sinocore.datagen.advancement.AdvancementTree {
    public AdvancementTree(Consumer<AdvancementHolder> saver, ResourceLocation rootId, Advancement.Builder advancement) {
        super(saver, rootId, advancement);
    }

    public AdvancementTree child(ResourceLocation id, Advancement.Builder advancement) {
        super.child(id, advancement);
        return this;
    }

    public AdvancementTree branch(ResourceLocation id, Advancement.Builder advancement) {
        super.branch(id, advancement);
        return this;
    }

    public AdvancementTree endBranch() {
        super.endBranch();
        return this;
    }
}
