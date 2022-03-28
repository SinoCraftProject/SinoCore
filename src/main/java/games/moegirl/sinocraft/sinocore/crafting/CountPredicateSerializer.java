package games.moegirl.sinocraft.sinocore.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import games.moegirl.sinocraft.sinocore.SinoCore;
import games.moegirl.sinocraft.sinocore.api.crafting.ICraftPredicateSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A predicate serializer to check item count
 */
public enum CountPredicateSerializer implements ICraftPredicateSerializer<CountPredicateSerializer.Predicate> {

    INSTANCE;

    private final ResourceLocation ID = new ResourceLocation(SinoCore.MODID, "count");

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public Predicate fromJson(JsonElement json) {
        return new Predicate(json.getAsInt());
    }

    @Override
    public Predicate fromNetwork(FriendlyByteBuf buffer) {
        return new Predicate(buffer.readVarInt());
    }

    @Override
    public JsonElement toJson(Predicate predicate) {
        return new JsonPrimitive(predicate.count);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, Predicate predicate) {
        buffer.writeVarInt(predicate.count);
    }

    public record Predicate(int count) implements ICraftPredicateSerializer.Predicate<Predicate> {

        @Override
        public boolean test(@Nullable ItemStack stack) {
            return count == 0 || (stack != null && stack.getCount() >= count);
        }

        @Override
        public List<ItemStack> getAllStack(List<ItemStack> input) {
            return input.stream().peek(s -> s.setCount(count)).toList();
        }

        @Override
        public ICraftPredicateSerializer<Predicate> serializer() {
            return INSTANCE;
        }
    }
}
