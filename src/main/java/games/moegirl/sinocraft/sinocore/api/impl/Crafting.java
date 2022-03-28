package games.moegirl.sinocraft.sinocore.api.impl;

import games.moegirl.sinocraft.sinocore.api.crafting.ICraftPredicateSerializer;
import games.moegirl.sinocraft.sinocore.api.crafting.ICrafting;
import games.moegirl.sinocraft.sinocore.api.crafting.IFluidIngredient;
import games.moegirl.sinocraft.sinocore.api.crafting.IFluidIngredientSerializer;
import games.moegirl.sinocraft.sinocore.common.crafting.CountPredicateSerializer;
import games.moegirl.sinocraft.sinocore.common.crafting.FluidIngredient;
import games.moegirl.sinocraft.sinocore.common.crafting.FluidIngredientSerializer;
import games.moegirl.sinocraft.sinocore.common.crafting.PredicateIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Crafting implements ICrafting {

    INSTANCE;

    private static final Map<ResourceLocation, ICraftPredicateSerializer<?>> SERIALIZER_MAP = new HashMap<>();

    @Override
    public PredicateIngredient withPredicates(Ingredient ingredient,
                                              Collection<? extends ICraftPredicateSerializer.Predicate<?>> predicates) {
        return PredicateIngredient.of(ingredient, predicates);
    }

    @Override
    public ICraftPredicateSerializer.Predicate<?> count(int count) {
        return new CountPredicateSerializer.Predicate(count);
    }

    @Override
    public void registerPredicate(ICraftPredicateSerializer<?> serializer) {
        SERIALIZER_MAP.put(serializer.id(), serializer);
    }

    @Override
    public Optional<ICraftPredicateSerializer<?>> getPredicateSerializer(ResourceLocation id) {
        return Optional.ofNullable(SERIALIZER_MAP.get(id));
    }

    @Override
    public IFluidIngredient ofFluid(Fluid fluid, int amount) {
        return new FluidIngredient(fluid, amount);
    }

    @Override
    public IFluidIngredient ofFluid(Tag<Fluid> fluid, int amount) {
        return new FluidIngredient(fluid, amount);
    }

    @Override
    public IFluidIngredientSerializer getFluidSerializer() {
        return FluidIngredientSerializer.INSTANCE;
    }
}
