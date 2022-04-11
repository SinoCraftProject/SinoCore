package games.moegirl.sinocraft.sinocore.common.crafting;

import games.moegirl.sinocraft.sinocore.api.crafting.IFluidIngredient;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * A fluid ingredient to check fluid or fluid tag and amount
 */
public class FluidIngredient implements IFluidIngredient {

    private final Fluid fluid;
    @Nullable
    private final TagKey<Fluid> tag;

    private final int amount;

    public FluidIngredient(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.tag = null;
        this.amount = amount;
    }

    public FluidIngredient(TagKey<Fluid> tag, int amount) {
        this.fluid = Fluids.EMPTY;
        this.tag = tag;
        this.amount = amount;
    }

    @Override
    public Fluid fluid() {
        return fluid;
    }

    @Override
    public Optional<TagKey<Fluid>> tag() {
        return Optional.ofNullable(tag);
    }

    @Override
    public int amount() {
        return amount;
    }

    @Override
    public boolean test(@Nullable FluidStack stack) {
        return stack != null && stack.getAmount() >= amount &&
                (tag == null ? fluid == stack.getRawFluid() : stack.getRawFluid().defaultFluidState().is(tag));
    }

    @Override
    public List<FluidStack> allStacks() {
        if (tag == null) {
            return List.of(new FluidStack(fluid, amount));
        }
        return StreamSupport.stream(Registry.FLUID.getTagOrEmpty(tag).spliterator(), false)
                .map(Holder::value)
                .map(f -> new FluidStack(f, amount))
                .toList();
    }
}
