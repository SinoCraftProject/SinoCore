package games.moegirl.sinocraft.sinocore.crafting;

import games.moegirl.sinocraft.sinocore.SinoCore;
import games.moegirl.sinocraft.sinocore.api.impl.Crafting;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

public class IngredientRegister {

    public static final ResourceLocation PREDICATE_INGREDIENT = new ResourceLocation(SinoCore.MODID, "with_predicates");

    public static void register() {
        CraftingHelper.register(PREDICATE_INGREDIENT, PredicateIngredientSerializer.INSTANCE);

        Crafting.INSTANCE.registerPredicate(CountPredicateSerializer.INSTANCE);
    }
}
