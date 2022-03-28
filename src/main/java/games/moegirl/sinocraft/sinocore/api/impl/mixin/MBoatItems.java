package games.moegirl.sinocraft.sinocore.api.impl.mixin;

import games.moegirl.sinocraft.sinocore.api.mixin.IBoatItems;
import games.moegirl.sinocraft.sinocore.api.tree.Tree;
import games.moegirl.sinocraft.sinocore.common.util.mixin.IBoatItem;
import net.minecraft.world.item.BoatItem;

public record MBoatItems(BoatItem boat) implements IBoatItems {

    @Override
    public BoatItem setTreeType(Tree tree) {
        return ((IBoatItem) boat).setTreeType(tree);
    }
}
