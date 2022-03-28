package games.moegirl.sinocraft.sinocore.utility.mixin;

import games.moegirl.sinocraft.sinocore.api.tree.Tree;
import net.minecraft.world.item.BoatItem;

public interface IBoatItem {

    BoatItem setTreeType(Tree tree);
}
