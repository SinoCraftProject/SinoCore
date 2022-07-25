package games.moegirl.sinocraft.sinocore.api.data.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class QuizSucceedCondition implements LootItemCondition {
    public static final LootItemConditionType QUIZ_SUCCEED_TYPE =
            new LootItemConditionType(new ConditionSerializer());

    private final boolean isSucceed;

    protected QuizSucceedCondition(boolean succeed) {
        isSucceed = succeed;
    }

    @Override
    public LootItemConditionType getType() {
        return QUIZ_SUCCEED_TYPE;
    }

    @Override
    public boolean test(LootContext context) {
        var entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);

        if (!(entity instanceof Player player)) {
            return false;
        }

        return PlayerQuizzingHelper.isCompleteSuccessfully(player, isSucceed);
    }

    public static QuizSucceedCondition.Builder builder(boolean succeed) {
        return new QuizSucceedCondition.Builder(succeed);
    }

    public static class Builder implements LootItemCondition.Builder {
        private final boolean isSucceed;

        public Builder(boolean succeed) {
            isSucceed = succeed;
        }

        @Override
        public LootItemCondition build() {
            return new QuizSucceedCondition(isSucceed);
        }
    }

    public static class ConditionSerializer implements Serializer<QuizSucceedCondition> {
        @Override
        public void serialize(JsonObject json, QuizSucceedCondition value, JsonSerializationContext serializationContext) {
            json.addProperty("isSucceed", value.isSucceed);
        }

        @Override
        public QuizSucceedCondition deserialize(JsonObject json, JsonDeserializationContext serializationContext) {
            return new QuizSucceedCondition(json.get("isSucceed").getAsBoolean());
        }
    }
}
