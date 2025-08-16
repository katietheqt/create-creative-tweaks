package cat.katie.createcreativetweaks.mixin.accessors;

import com.simibubi.create.api.contraption.storage.fluid.MountedFluidStorage;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorage;
import com.simibubi.create.content.contraptions.MountedStorageManager;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = MountedStorageManager.class, remap = false)
public interface MountedStorageManagerAccessor {
    @Accessor("itemsBuilder")
    Map<BlockPos, MountedItemStorage> cct$getItemsBuilder();

    @Accessor("fluidsBuilder")
    Map<BlockPos, MountedFluidStorage> cct$getFluidsBuilder();
}
