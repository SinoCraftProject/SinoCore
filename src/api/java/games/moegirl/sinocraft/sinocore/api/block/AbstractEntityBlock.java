package games.moegirl.sinocraft.sinocore.api.block;

import com.google.common.reflect.TypeToken;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * A block with {@link  BlockEntity}.
 * <p>Base on {@link  BaseEntityBlock}, use model render and impl getTicker method</p>
 * <p>If entity need update, impl {@link  BlockEntityTicker} on BlockEntity.</p>
 */
public abstract class AbstractEntityBlock<T extends BlockEntity> extends BaseEntityBlock {

    protected final Lazy<BlockEntityType<T>> entityType;
    private final Class<?> typeClass;

    @SuppressWarnings("UnstableApiUsage")
    public AbstractEntityBlock(Properties properties, Supplier<BlockEntityType<T>> entityType) {
        super(properties);
        this.entityType = Lazy.of(entityType);
        this.typeClass = TypeToken.of(getClass()).getRawType();
    }

    public AbstractEntityBlock(Supplier<BlockEntityType<T>> entityType) {
        this(BlockBehaviour.Properties.of(Material.METAL), entityType);
    }

    public AbstractEntityBlock(Material material, float strength, Supplier<BlockEntityType<T>> entityType) {
        this(BlockBehaviour.Properties.of(material).strength(strength), entityType);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return entityType.get().create(pPos, pState);
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T2 extends BlockEntity> BlockEntityTicker<T2> getTicker(Level pLevel, BlockState pState, BlockEntityType<T2> pBlockEntityType) {
        return BlockEntityTicker.class.isAssignableFrom(typeClass) ? ((pLevel1, pPos, pState1, pBlockEntity) -> {
            if (pBlockEntity instanceof BlockEntityTicker ticker) {
                ticker.tick(pLevel1, pPos, pState1, pBlockEntity);
            }
        }) : null;
    }

    @Nullable
    @Override
    public <T2 extends BlockEntity> GameEventListener getListener(Level pLevel, T2 pBlockEntity) {
        return GameEventListener.class.isAssignableFrom(typeClass)
                ? (GameEventListener) pBlockEntity
                : this instanceof GameEventListener l ? l : null;
    }
}
