package games.moegirl.sinocraft.sinocore.crafting;

import games.moegirl.sinocraft.sinocore.api.crafting.IFluidIngredient;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * A fluid ingredient to check fluid or fluid tag and amount
 */
public class FluidIngredient implements IFluidIngredient {

    private final Fluid fluid;
    @Nullable
    private final Tag<Fluid> tag;

    private final int amount;

    public FluidIngredient(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.tag = null;
        this.amount = amount;
    }

    public FluidIngredient(Tag<Fluid> tag, int amount) {
        this.fluid = Fluids.EMPTY;
        this.tag = tag;
        this.amount = amount;
    }

    @Override
    public Fluid fluid() {
        return fluid;
    }

    @Override
    public Optional<Tag<Fluid>> tag() {
        return Optional.ofNullable(tag);
    }

    @Override
    public int amount() {
        return amount;
    }

    @Override
    public boolean test(@Nullable FluidStack stack) {
        return stack != null && stack.getAmount() >= amount &&
                (tag == null ? fluid == stack.getRawFluid() : tag.contains(stack.getRawFluid()));
    }

    @Override
    public List<FluidStack> allStacks() {
        if (tag == null) {
            return List.of(new FluidStack(fluid, amount));
        }
        return tag.getValues().stream().map(f -> new FluidStack(f, amount)).toList();
    }
}
