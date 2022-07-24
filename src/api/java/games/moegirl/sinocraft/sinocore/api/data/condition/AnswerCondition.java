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

import java.util.Calendar;

public class AnswerCondition implements LootItemCondition {
    public static final LootItemConditionType ANSWER_CONDITION_TYPE =
            new LootItemConditionType(new ConditionSerializer());

    private final String ans;

    protected AnswerCondition(String answer) {
        ans = answer;
    }

    @Override
    public LootItemConditionType getType() {
        return ANSWER_CONDITION_TYPE;
    }

    @Override
    public boolean test(LootContext context) {
        var entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);

        if (!(entity instanceof Player player)) {
            return false;
        }

        return PlayerQuizzingHelper.isCorrect(player, ans);
    }

    public static AnswerCondition.Builder builder(String answer) {
        return new AnswerCondition.Builder(answer);
    }

    public static class Builder implements LootItemCondition.Builder {
        private final String ans;

        public Builder(String answer) {
            ans = answer;
        }

        @Override
        public LootItemCondition build() {
            return new AnswerCondition(ans);
        }
    }

    public static class ConditionSerializer implements Serializer<AnswerCondition> {
        @Override
        public void serialize(JsonObject json, AnswerCondition value, JsonSerializationContext serializationContext) {
            json.addProperty("answer", value.ans);
        }

        @Override
        public AnswerCondition deserialize(JsonObject json, JsonDeserializationContext serializationContext) {
            return new AnswerCondition(json.get("answer").getAsString());
        }
    }
}
