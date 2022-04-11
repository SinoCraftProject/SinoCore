package games.moegirl.sinocraft.sinocore.api.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Crafting API
 */
public interface ICrafting {

    /**
     * Create a PredicateIngredient, this ingredient adds {@link ICraftPredicateSerializer.Predicate} to another
     * ingredient used to filter result.
     *
     * @param ingredient existed ingredient
     * @param predicates predicates
     * @return new ingredient
     */
    Ingredient withPredicates(Ingredient ingredient, Collection<? extends ICraftPredicateSerializer.Predicate<?>> predicates);

    /**
     * Create a PredicateIngredient, this ingredient adds {@link ICraftPredicateSerializer.Predicate} to another
     * ingredient used to filter result.
     *
     * @param ingredient existed ingredient
     * @param predicates predicates
     * @return new ingredient
     */
    default Ingredient withPredicates(Ingredient ingredient, ICraftPredicateSerializer.Predicate<?>... predicates) {
        return withPredicates(ingredient, List.of(predicates));
    }

    /**
     * A predicate to filter item stack's count
     * @param count minimum count
     * @return predicate
     */
    ICraftPredicateSerializer.Predicate<?> count(int count);

    /**
     * Add a new predicate
     * @param serializer predicate serializer
     */
    void registerPredicate(ICraftPredicateSerializer<?> serializer);

    /**
     * Get a predicate serializer
     * @param id predicate id
     * @return serializer
     */
    Optional<ICraftPredicateSerializer<?>> getPredicateSerializer(ResourceLocation id);

    /**
     * Create a fluid ingredient to check fluid.
     * @param fluid fluid
     * @param amount amount
     * @return fluid ingredient
     */
    IFluidIngredient ofFluid(Fluid fluid, int amount);

    /**
     * Create a bucket of fluid ingredient to check fluid.
     * @param fluid fluid
     * @return fluid ingredient
     */
    default IFluidIngredient ofFluid(Fluid fluid) {
        return ofFluid(fluid, 1000);
    }

    /**
     * Create a fluid ingredient to check fluid tag.
     * @param fluid fluid tag
     * @param amount amount
     * @return fluid ingredient
     */
    IFluidIngredient ofFluid(Tag<Fluid> fluid, int amount);

    /**
     * Create a bucket of fluid ingredient to check fluid tag.
     * @param fluid fluid tag
     * @return fluid ingredient
     */
    default IFluidIngredient ofFluid(Tag<Fluid> fluid) {
        return ofFluid(fluid, 1000);
    }

    /**
     * Get serializer for IFluidIngredient to read from/write to json or network
     * @return serializer
     */
    IFluidIngredientSerializer getFluidSerializer();

}
