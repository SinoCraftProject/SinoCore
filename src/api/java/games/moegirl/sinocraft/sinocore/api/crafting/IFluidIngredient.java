package games.moegirl.sinocraft.sinocore.api.crafting;

import games.moegirl.sinocraft.sinocore.api.SinoCoreAPI;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * An ingredient to match fluid stack by fluid or tag and amount.
 */
public interface IFluidIngredient {

    IFluidIngredient EMPTY = SinoCoreAPI.getCrafting().ofFluid(Fluids.EMPTY, 0);

    /**
     * Get fluid
     * @return matched fluid, or {@link Fluids#EMPTY} if use tag
     */
    Fluid fluid();

    /**
     * Get tag to match
     * @return tag or empty
     */
    Optional<Tag<Fluid>> tag();

    /**
     * Get amount of fluid
     * @return minimum amount
     */
    int amount();

    /**
     * Check if the fluid is allowed
     * @param stack input
     * @return true if allowed
     */
    boolean test(@Nullable FluidStack stack);

    /**
     * Get all allowed fluid stack
     * @return all fluid stack
     */
    List<FluidStack> allStacks();
}
