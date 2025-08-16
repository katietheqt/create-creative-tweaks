package cat.katie.createcreativetweaks.mixin.accessors;

import com.simibubi.create.content.equipment.toolbox.ToolboxInventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = ToolboxInventory.class, remap = false)
public interface ToolboxInventoryAccessor {
    @Accessor("filters")
    List<ItemStack> cct$getFilters();
}
