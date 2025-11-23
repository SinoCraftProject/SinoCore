package games.moegirl.sinocraft.sinocore.data.gen.blockstate;

import com.google.gson.JsonObject;
import net.minecraft.world.level.block.Block;

@Deprecated(forRemoval = true, since = "1.2.0")
public interface IBlockStateBuilder {

    Block getOwner();

    JsonObject toJson();
}
