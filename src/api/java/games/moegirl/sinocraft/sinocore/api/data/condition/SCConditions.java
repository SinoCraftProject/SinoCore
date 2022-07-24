package games.moegirl.sinocraft.sinocore.api.data.condition;

import games.moegirl.sinocraft.sinocore.api.SinoCoreAPI;
import games.moegirl.sinocraft.sinocore.api.data.condition.trigger.AnswerTrigger;
import games.moegirl.sinocraft.sinocore.api.data.condition.trigger.ParityDaysTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SinoCoreAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCConditions {
    public static final ResourceLocation PARITY_DAYS_ID = new ResourceLocation(SinoCoreAPI.getId(), "parity_days");
    public static final ResourceLocation ANSWER_ID = new ResourceLocation(SinoCoreAPI.getId(), "answer");

    public static final ParityDaysTrigger PARITY_DAYS_TRIGGER = CriteriaTriggers.register(new ParityDaysTrigger(PARITY_DAYS_ID));
    public static final AnswerTrigger ANSWER_TRIGGER = CriteriaTriggers.register(new AnswerTrigger(ANSWER_ID));

    @SubscribeEvent
    public static void onRegisterPredicates(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        Registry.register(Registry.LOOT_CONDITION_TYPE, PARITY_DAYS_ID, ParityDaysCondition.PARITY_DAYS_CONDITION_TYPE);
        Registry.register(Registry.LOOT_CONDITION_TYPE, ANSWER_ID, AnswerCondition.ANSWER_CONDITION_TYPE);
    }
}
