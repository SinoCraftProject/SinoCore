package games.moegirl.sinocraft.sinocore.api.woodwork;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class ModChestBlockItem extends BlockItem {

    private final Woodwork woodwork;

    protected ModChestBlockItem(Woodwork woodwork, Block block, Properties properties) {
        super(block, properties);
        this.woodwork = woodwork;
    }

    public ModChestBlockItem(Properties properties, Woodwork woodwork) {
        this(woodwork, woodwork.chest(), properties);
    }

    public Woodwork getWoodwork() {
        return woodwork;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(ModChestItemRenderer.INSTANCE);
    }
}
