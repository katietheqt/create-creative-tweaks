package cat.katie.createcreativetweaks.features.contraption_order;

import cat.katie.createcreativetweaks.duck.IContraptionSimulationAnchorBlock;
import cat.katie.createcreativetweaks.duck.IMovingContraptionAnchorBlockEntity;
import cat.katie.createcreativetweaks.features.contraption_order.state.OrderState;
import cat.katie.createcreativetweaks.features.contraption_order.state.ContraptionOrderState;
import cat.katie.createcreativetweaks.features.contraption_order.state.UnassembledOrderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

// TODO:
//  - add minecart interaction to visualize a minecart contraption
//  - handle clockwork bearings properly

public enum ContraptionOrder {
    INSTANCE;

    public static final Logger LOGGER = LogUtils.getLogger();

    // map of block anchorPos containing anchor (cart assembler/bearing/etc) to unassembled contraption info
    private final Set<OrderState> states = new HashSet<>();
    private final Map<BlockPos, OrderState> stationaryStates = new HashMap<>();
    private final WeakHashMap<AbstractContraptionEntity, ContraptionOrderState> movingContraptionStates = new WeakHashMap<>();

    /**
     * Destroys a given anchor state.
     *
     * @param state the state to remove
     */
    private void removeState(OrderState state) {
        if (state == null) {
            return;
        }

        states.remove(state);

        BlockPos anchorPos = state.anchorPos();

        if (anchorPos != null) {
            stationaryStates.remove(anchorPos);
        }

        Contraption contraption = state.contraption();

        if (contraption != null && contraption.entity != null) {
            movingContraptionStates.remove(contraption.entity);
        }
    }

    /**
     * Gets a stationary anchor state at given position.
     *
     * @param anchorPos the position to check
     * @return the stationary anchor state at the given position, if one exists
     */
    @Nullable
    private OrderState getStationaryAnchorState(BlockPos anchorPos) {
        OrderState state = stationaryStates.get(anchorPos);

        // remove invalid contraptions
        if (state == null) {
            return null;
        }

        if (state.isInvalid()) {
            removeState(state);
            return null;
        }

        return state;
    }

    /**
     * Handles a click on a stationary anchor, assuming the anchor already exists.
     *
     * @param anchorPos the position of the anchor
     * @return {@literal true} if an existing anchor exists, or {@literal false} if one does not.
     */
    public boolean handleExistingStationaryAnchorClick(BlockPos anchorPos) {
        OrderState state = getStationaryAnchorState(anchorPos);

        if (state == null) {
            return false;
        }

        if (!state.handleClick()) {
            // see `handleClick` documentation
            removeState(state);
        }

        return true;
    }

    /**
     * Gets a moving anchor state tied to a given entity.
     *
     * @param entity the entity to get the state of
     * @return the moving anchor state at the given position, if one exists
     */
    @Nullable
    private OrderState getMovingAnchorState(AbstractContraptionEntity entity) {
        OrderState state = movingContraptionStates.get(entity);

        // remove invalid contraptions
        if (state == null) {
            return null;
        }

        if (state.isInvalid()) {
            removeState(state);
            return null;
        }

        return state;
    }

    /**
     * Handles a click on a stationary anchor, assuming the anchor already exists.
     *
     * @param entity the contraption entity
     * @return {@literal true} if an existing anchor exists, or {@literal false} if one does not.
     */
    public boolean handleExistingMovingAnchorClick(AbstractContraptionEntity entity) {
        OrderState state = getMovingAnchorState(entity);

        if (state == null) {
            return false;
        }

        if (!state.handleClick()) {
            // see `handleClick` documentation
            removeState(state);
        }

        return true;
    }

    /**
     * Adds a new anchor state at the given position. One must not already exist.
     */
    public void addNewStationaryState(BlockPos blockPos, OrderState state) {
        if (stationaryStates.containsKey(blockPos)) {
            throw new IllegalStateException("attempted to add a state when one already exists at position " + blockPos);
        }

        Objects.requireNonNull(blockPos);

        states.add(state);
        stationaryStates.put(blockPos, state);

        if (state instanceof ContraptionOrderState contraptionState) {
            AbstractContraptionEntity entity = contraptionState.entity();
            movingContraptionStates.put(entity, contraptionState);
        }
    }

    public void addNewMovingState(ContraptionOrderState state) {
        if (state.anchorPos() != null) {
            throw new IllegalArgumentException("cannot add a state with an anchor position as a moving state");
        }

        states.add(state);
        movingContraptionStates.put(state.entity(), state);
    }

    public void renderOverlay(PoseStack stack, MultiBufferSource.BufferSource buffers, Camera camera, float partialTicks) {
        for (OrderState state : states) {
            if (state.isInvalid()) {
                continue;
            }

            Contraption contraption = state.contraption();

            if (contraption != null) {
                ContraptionOrderRenderer.renderOverlay(stack, buffers, camera, state, partialTicks);
            }
        }
    }

    public void tick() {
        Set<OrderState> invalidStates = new HashSet<>();

        for (OrderState state : states) {

            // remove invalid contraptions
            if (state.isInvalid()) {
                invalidStates.add(state);
                continue;
            }

            state.tick();
        }

        invalidStates.forEach(this::removeState);
    }

    public boolean addGogglesTooltip(List<Component> tooltip, BlockPos pos) {
        OrderState orderState = stationaryStates.get(pos);

        if (orderState == null) {
            return false;
        }

        return orderState.addGogglesTooltip(tooltip);
    }

    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();

        if (!level.isClientSide || !event.getItemStack().isEmpty() || !event.getEntity().isShiftKeyDown()) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        // TODO: clockwork bearings have two contraptions - no handling for that currently

        // if an existing stationary anchor exists at the position, use it
        if (handleExistingStationaryAnchorClick(pos)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        // check for an existing contraption at the block position
        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof IMovingContraptionAnchorBlockEntity anchorBe) {
            AbstractContraptionEntity entity = anchorBe.cct$getExistingContraptionEntity();

            if (entity != null) {
                addNewStationaryState(pos, new ContraptionOrderState(entity, pos));

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
                return;
            }
        }

        if (state.getBlock() instanceof IContraptionSimulationAnchorBlock anchorBlock) {
            addNewStationaryState(pos, new UnassembledOrderState(level, pos, state, () -> anchorBlock.cct$createContraption(state)));

            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    public void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        Level level = event.getLevel();

        if (!level.isClientSide || !event.getItemStack().isEmpty() || !event.getEntity().isShiftKeyDown()) {
            return;
        }

        if (!(event.getTarget() instanceof AbstractMinecart cart) || cart.getPassengers().isEmpty()) {
            return;
        }

        Entity passenger = cart.getPassengers().get(0);

        if (!(passenger instanceof OrientedContraptionEntity contraption)) {
            return;
        }

        if (!handleExistingMovingAnchorClick(contraption)) {
            addNewMovingState(new ContraptionOrderState(contraption, null));
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    /**
     * Hook that converts assembled contraptions to unassembled ones on disassembly.
     * @param entity the entity that was disassembled
     */
    public void onContraptionDisassembly(AbstractContraptionEntity entity) {
        ContraptionOrderState state = movingContraptionStates.get(entity);

        if (state == null) {
            return;
        }

        // remove the original state
        removeState(state);

        BlockPos anchorPos = state.anchorPos();

        if (anchorPos == null) {
            return;
        }

        // construct a new stationary contraption at the anchor block
        BlockState anchorState = state.level().getBlockState(anchorPos);

        if (anchorState.getBlock() instanceof IContraptionSimulationAnchorBlock anchorBlock) {
            UnassembledOrderState newState = new UnassembledOrderState(state.level(), anchorPos, anchorState,
                    () -> anchorBlock.cct$createContraption(anchorState));

            newState.setDisplayState(state.displayState());
            addNewStationaryState(anchorPos, newState);
        }
    }

    /**
     * Hook that converts stationary unassembled ones to assembled ones on assembly.
     * @param pos the position of the anchor
     * @param entity the new entity
     */
    public void onStationaryContraptionAssembly(BlockPos pos, AbstractContraptionEntity entity) {
        OrderState state = stationaryStates.get(pos);

        if (state == null) {
            return;
        }

        if (!(state instanceof UnassembledOrderState unassembled)) {
            return;
        }

        removeState(state);

        // swap to a contraption state
        addNewStationaryState(pos, new ContraptionOrderState(unassembled, entity));
    }
}
