package games.moegirl.sinocraft.sinocore.common.util.mixin;

import games.moegirl.sinocraft.sinocore.api.tree.Tree;
import net.minecraft.world.item.BoatItem;

public interface IBoatItem {

    BoatItem setTreeType(Tree tree);
}
