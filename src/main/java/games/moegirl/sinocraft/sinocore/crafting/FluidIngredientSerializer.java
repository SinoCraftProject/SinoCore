package games.moegirl.sinocraft.sinocore.crafting;

import com.google.gson.JsonObject;
import games.moegirl.sinocraft.sinocore.api.crafting.IFluidIngredient;
import games.moegirl.sinocraft.sinocore.api.crafting.IFluidIngredientSerializer;
import games.moegirl.sinocraft.sinocore.utility.TagHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.Optional;

/**
 * Fluid ingredient serializer
 */
public enum FluidIngredientSerializer implements IFluidIngredientSerializer {
    INSTANCE;

    @Override
    public IFluidIngredient fromNetwork(FriendlyByteBuf buffer) {
        if (buffer.readBoolean()) {
            Fluid fluid = buffer.readRegistryId();
            int amount = buffer.readInt();
            return new FluidIngredient(fluid, amount);
        } else {
            ResourceLocation tagName = buffer.readResourceLocation();
            int amount = buffer.readInt();
            return new FluidIngredient(FluidTags.create(tagName), amount);
        }
    }

    @Override
    public IFluidIngredient fromJson(JsonObject json) {
        int amount = GsonHelper.getAsInt(json, "amount", 1000);
        if (json.has("tag")) {
            TagKey<Fluid> tag = TagHelper.getFluidTag(GsonHelper.getAsString(json, "tag"));
            return new FluidIngredient(tag, amount);
        } else if (json.has("fluid")) {
            String fluidName = GsonHelper.getAsString(json, "fluid");
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidName));
            Objects.requireNonNull(fluid, "Unknown fluid " + fluidName);
            return new FluidIngredient(fluid, amount);
        } else {
            return IFluidIngredient.EMPTY;
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer, IFluidIngredient ingredient) {
        Optional<TagKey<Fluid>> tagOptional = ingredient.tag();
        buffer.writeBoolean(tagOptional.isEmpty());
        if (tagOptional.isEmpty()) {
            buffer.writeRegistryId(ingredient.fluid());
        } else {
            buffer.writeResourceLocation(tagOptional.get().location());
        }
        buffer.writeInt(ingredient.amount());
    }

    @Override
    public JsonObject write(JsonObject object, IFluidIngredient ingredient) {
        if (ingredient.tag().isPresent()) {
            object.addProperty("tag", ingredient.tag().get().location().toString());
        } else {
            object.addProperty("fluid", ingredient.fluid().delegate.name().toString());
        }
        object.addProperty("amount", ingredient.amount());
        return object;
    }
}