package games.moegirl.sinocraft.sinocore.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import games.moegirl.sinocraft.sinocore.api.crafting.ICraftPredicateSerializer;
import games.moegirl.sinocraft.sinocore.api.impl.Crafting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import java.util.*;

/**
 * A serializer for Predicate Ingredient
 * <p>It has difference for different ingredient.</p>
 * <p>If ingredient json is object and not contains "type2" property:</p>
 *
 * {@code {
 *     "type": "sinocore:with_predicates",
 *     // ingredient type
 *     "type2": ...,
 *
 *     // other ingredient object properties
 *     // ...,
 *
 *     // other predicates
 *     // "sinocore:count": 5
 *     // ...
 * }
 * }
 *
 * <p>If ingredient is not a object (is array) or contains "type2" properties: </p>
 *
 * {@code {
 *     "type": "sinocore:with_predicates",
 *
 *     // value is ingredient body
 *     "type2": ...,
 *
 *     // other predicates
 *     // "sinocore:count": 5
 *     // ...
 * }
 * }
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public enum PredicateIngredientSerializer implements IIngredientSerializer<PredicateIngredient> {
    INSTANCE;

    public static final String KEY_TYPE = "type2";

    @Override
    public PredicateIngredient parse(FriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ICraftPredicateSerializer.Predicate<?>[] predicates = new ICraftPredicateSerializer.Predicate[buffer.readVarInt()];
        for (int i = 0; i < predicates.length; i++) {
            ResourceLocation id = buffer.readResourceLocation();
            predicates[i] = Crafting.INSTANCE.getPredicateSerializer(id)
                    .orElseThrow(() -> new IllegalStateException("Unknown predicate " + id))
                    .fromNetwork(buffer);
        }
        return PredicateIngredient.of(ingredient, List.of(predicates));
    }

    @Override
    public PredicateIngredient parse(JsonObject json) {
        List<ICraftPredicateSerializer.Predicate<?>> predicates = new ArrayList<>();
        Iterator<String> keys = json.keySet().iterator();
        while (keys.hasNext()) {
            String s = keys.next();
            if (s.contains(":")) {
                ResourceLocation id = new ResourceLocation(s);
                Crafting.INSTANCE.getPredicateSerializer(id)
                        .map(serializer -> serializer.fromJson(json.get(s)))
                        .ifPresent(p -> {
                            keys.remove();
                            predicates.add(p);
                        });
            }
        }
        JsonElement ingredientJson;
        if (json.has(KEY_TYPE)) {
            JsonElement ingredientElement = json.get(KEY_TYPE);
            if (ingredientElement.isJsonPrimitive()) {
                json.add("type", ingredientElement);
                json.remove(KEY_TYPE);
                ingredientJson = json;
            } else {
                ingredientJson = json.get(KEY_TYPE);
            }
        } else {
            ingredientJson = json;
        }
        Ingredient ingredient = CraftingHelper.getIngredient(ingredientJson);
        return PredicateIngredient.of(ingredient, predicates);
    }

    @Override
    public void write(FriendlyByteBuf buffer, PredicateIngredient ingredient) {
        PredicateIngredient.Value value = ingredient.value;
        CraftingHelper.write(buffer, value.ingredient());
        Collection<? extends ICraftPredicateSerializer.Predicate<?>> predicates = value.predicates();
        buffer.writeVarInt(predicates.size());
        for (ICraftPredicateSerializer.Predicate predicate : predicates) {
            buffer.writeResourceLocation(predicate.serializer().id());
            predicate.serializer().toNetwork(buffer, predicate);
        }
    }

    public JsonObject write(PredicateIngredient ingredient) {
        JsonObject object = writeValue(ingredient.value);
        object.addProperty("type", IngredientRegister.PREDICATE_INGREDIENT.toString());
        return object;
    }

    JsonObject writeValue(PredicateIngredient.Value value) {
        Ingredient i = value.ingredient();
        JsonElement element = i.toJson();
        JsonObject object;

        if (!element.isJsonObject() || element.getAsJsonObject().has(KEY_TYPE)) {
            object = new JsonObject();
            object.add(KEY_TYPE, element);
        } else {
            object = element.getAsJsonObject();
            if (object.has("type")) {
                object.add(KEY_TYPE, object.get("type"));
            }
        }

        for (ICraftPredicateSerializer.Predicate predicate : value.predicates()) {
            ICraftPredicateSerializer serializer = predicate.serializer();
            object.add(serializer.id().toString(), serializer.toJson(predicate));
        }

        return object;
    }
}
