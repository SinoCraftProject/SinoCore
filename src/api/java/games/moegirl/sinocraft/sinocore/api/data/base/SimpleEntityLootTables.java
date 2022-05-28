package games.moegirl.sinocraft.sinocore.api.data.base;

import net.minecraft.data.loot.EntityLoot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SimpleEntityLootTables extends EntityLoot {

    private final Map<EntityType<?>, LootTable.Builder> tables = new HashMap<>();

    public void addEntity(EntityType<?> entity, LootTable.Builder table) {
        tables.put(entity, table);
    }

    public void addEntities(Map<EntityType<?>, LootTable.Builder> tables) {
        tables.forEach(this::addEntity);
    }

    public void removeEntity(EntityType<?> entity) {
        tables.remove(entity);
    }

    public void removeEntities(Iterable<EntityType<?>> entities) {
        entities.forEach(tables::remove);
    }

    @Override
    protected void addTables() {
        tables.forEach(this::add);
    }

    @Override
    protected Iterable<EntityType<?>> getKnownEntities() {
        return Collections.unmodifiableCollection(tables.keySet());
    }
}
