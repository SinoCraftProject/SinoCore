package games.moegirl.sinocraft.sinocore.api.impl.mixin;

import games.moegirl.sinocraft.sinocore.api.mixin.IBoats;
import games.moegirl.sinocraft.sinocore.api.tree.Tree;
import games.moegirl.sinocraft.sinocore.common.util.mixin.IBoat;
import net.minecraft.world.entity.vehicle.Boat;

public record MBoat(Boat boat) implements IBoats {

    @Override
    public Tree getTreeType() {
        return ((IBoat) boat).getTreeType();
    }

    @Override
    public void setTreeType(Tree tree) {
        ((IBoat) boat).setTreeType(tree);
    }
}
