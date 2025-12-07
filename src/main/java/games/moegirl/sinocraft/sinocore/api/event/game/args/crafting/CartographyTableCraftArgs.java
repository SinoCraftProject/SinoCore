package games.moegirl.sinocraft.sinocore.api.event.game.args.crafting;

import games.moegirl.sinocraft.sinocore.api.event.CancellableArgsBase;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.item.ItemStack;

public class CartographyTableCraftArgs extends CancellableArgsBase {
    @Getter
    private final ItemStack mapInput;

    @Getter
    private final ItemStack otherInput;

    @Getter
    @Setter
    private ItemStack output;

    public CartographyTableCraftArgs(ItemStack mapInput, ItemStack otherInput, ItemStack output) {
        this.mapInput = mapInput;
        this.otherInput = otherInput;
        this.output = output;
    }
}
