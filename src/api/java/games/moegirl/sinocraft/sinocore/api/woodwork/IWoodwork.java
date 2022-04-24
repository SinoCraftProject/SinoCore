package games.moegirl.sinocraft.sinocore.api.woodwork;

import net.minecraft.world.level.block.state.properties.WoodType;

public interface IWoodwork {

    Woodwork getWoodwork();

    default WoodType getWoodType() {
        return getWoodwork().type;
    }
}
