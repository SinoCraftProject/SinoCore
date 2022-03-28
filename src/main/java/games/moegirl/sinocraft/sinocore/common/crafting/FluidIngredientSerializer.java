package games.moegirl.sinocraft.sinocore.common.crafting;

import com.google.gson.JsonObject;
import games.moegirl.sinocraft.sinocore.api.crafting.IFluidIngredient;
import games.moegirl.sinocraft.sinocore.api.crafting.IFluidIngredientSerializer;
import games.moegirl.sinocraft.sinocore.common.util.TagHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
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
            Tag<Fluid> tag = TagHelper.getFluidTag(tagName);
            int amount = buffer.readInt();
            return new FluidIngredient(tag, amount);
        }
    }

    @Override
    public IFluidIngredient fromJson(JsonObject json) {
        int amount = GsonHelper.getAsInt(json, "amount", 1000);
        if (json.has("tag")) {
            Tag<Fluid> tag = TagHelper.getFluidTag(GsonHelper.getAsString(json, "tag"));
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
        Optional<Tag<Fluid>> tagOptional = ingredient.tag();
        buffer.writeBoolean(tagOptional.isEmpty());
        if (tagOptional.isEmpty()) {
            buffer.writeRegistryId(ingredient.fluid());
        } else {
            buffer.writeResourceLocation(TagHelper.getFluidId(tagOptional.get()));
        }
        buffer.writeInt(ingredient.amount());
    }

    @Override
    public JsonObject write(JsonObject object, IFluidIngredient ingredient) {
        if (ingredient.tag().isPresent()) {
            object.addProperty("tag", TagHelper.getFluidId(ingredient.tag().get()).toString());
        } else {
            object.addProperty("fluid", ingredient.fluid().delegate.name().toString());
        }
        object.addProperty("amount", ingredient.amount());
        return object;
    }
}
