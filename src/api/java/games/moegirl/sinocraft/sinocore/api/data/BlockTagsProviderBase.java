package games.moegirl.sinocraft.sinocore.api.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * register block tags
 *
 * @author skyinr
 */
public abstract class BlockTagsProviderBase extends BlockTagsProvider {
    public BlockTagsProviderBase(DataGenerator pGenerator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        addPickaxe();
        addAxe();
        addShovel();
        addHoe();

        addStoneTool();
        addIronTool();
        addDiamondTool();
    }

    public abstract void addPickaxe();
    public abstract void addAxe();
    public abstract void addShovel();
    public abstract void addHoe();

    public abstract void addStoneTool();
    public abstract void addIronTool();
    public abstract void addDiamondTool();

}
