package games.moegirl.sinocraft.sinocore.data.gen.advancement;

import net.minecraft.advancements.AdvancementType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

/**
 * Moved to {@link games.moegirl.sinocraft.sinocore.datagen.advancement.DisplayInfoBuilder}
 */
@Deprecated(forRemoval = true, since = "1.2.0")
public class DisplayInfoBuilder extends games.moegirl.sinocraft.sinocore.datagen.advancement.DisplayInfoBuilder {

    public DisplayInfoBuilder setBackground(@Nullable ResourceLocation background) {
        super.setBackground(background);
        return this;
    }

    public DisplayInfoBuilder setIcon(ItemStack icon) {
        super.setIcon(icon);
        return this;
    }

    public DisplayInfoBuilder setIcon(ItemLike icon) {
        return setIcon(new ItemStack(icon));
    }

    public DisplayInfoBuilder setTitle(Component title) {
        super.setTitle(title);
        return this;
    }

    public DisplayInfoBuilder setDesc(Component desc) {
        super.setDesc(desc);
        return this;
    }

    public DisplayInfoBuilder setFrameType(AdvancementType frameType) {
        super.setFrameType(frameType);
        return this;
    }

    public DisplayInfoBuilder showToast() {
        super.showToast();
        return this;
    }

    public DisplayInfoBuilder announceChat() {
        super.announceChat();
        return this;
    }

    public DisplayInfoBuilder hidden() {
        super.hidden();
        return this;
    }
}
