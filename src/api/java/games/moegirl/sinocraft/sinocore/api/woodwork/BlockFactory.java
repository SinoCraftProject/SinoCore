package games.moegirl.sinocraft.sinocore.api.woodwork;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BlockFactory<B extends Block, I extends BlockItem, E extends BlockEntity> {

    WoodworkBuilder builder;
    String name;
    Function<Woodwork, BlockBehaviour.Properties> properties;
    @Nullable
    Function<Woodwork, Item.Properties> itemProperties;
    BiFunction<BlockBehaviour.Properties, Woodwork, B> factory;
    BiFunction<Item.Properties, Woodwork, I> itemFactory;
    boolean customEntity = false;
    boolean noBlock = false;

    public BlockFactory(WoodworkBuilder builder, String name,
                        Function<Woodwork, BlockBehaviour.Properties> properties,
                        @Nullable
                        Function<Woodwork, Item.Properties> itemProperties,
                        BiFunction<BlockBehaviour.Properties, Woodwork, B> factory,
                        BiFunction<Item.Properties, Woodwork, I> itemFactory) {
        this.builder = builder;
        this.name = name;
        this.properties = properties;
        this.itemProperties = itemProperties;
        this.factory = factory;
        this.itemFactory = itemFactory;
    }

    public BlockFactory(WoodworkBuilder builder, String name,
                        Function<Woodwork, BlockBehaviour.Properties> properties,
                        BiFunction<BlockBehaviour.Properties, Woodwork, B> factory,
                        BiFunction<Item.Properties, Woodwork, I> itemFactory) {
        this(builder, name, properties, null, factory, itemFactory);
    }

    public BlockFactory(WoodworkBuilder builder, String name,
                        Function<Woodwork, BlockBehaviour.Properties> properties,
                        Function<BlockBehaviour.Properties, B> factory,
                        BiFunction<Item.Properties, Woodwork, I> itemFactory) {
        this(builder, name, properties, null, (p, b) -> factory.apply(p), itemFactory);
    }

    public B newBlock(Woodwork woodwork) {
        return factory.apply(properties.apply(woodwork), woodwork);
    }

    public I newItem(Woodwork woodwork) {
        return itemFactory.apply(itemProperties == null
                ? builder.defaultItemProperties.apply(woodwork)
                : itemProperties.apply(woodwork), woodwork);
    }
}
