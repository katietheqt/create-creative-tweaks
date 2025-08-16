package cat.katie.createcreativetweaks.features;

import cat.katie.createcreativetweaks.infrastructure.config.AllConfigs;
import cat.katie.createcreativetweaks.mixin.accessors.ToolboxInventoryAccessor;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.toolbox.ToolboxInventory;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Adds a shulker box style tooltip to toolbox items.
 */
public class ToolboxTooltip {
    private ToolboxTooltip() {
        throw new UnsupportedOperationException("Cannot instantiate ToolboxTooltip");
    }

    public static void onItemTooltip(ItemStack stack, List<Component> tooltip) {
        if (!AllConfigs.client().showToolboxTooltip.get() || !stack.is(AllTags.AllItemTags.TOOLBOXES.tag)) {
            return;
        }

        CompoundTag tag = stack.getTag();

        if (tag == null || !tag.contains("Inventory", Tag.TAG_COMPOUND)) {
            return;
        }

        ToolboxInventory inventory = new ToolboxInventory(null);
        CompoundTag inventoryTag = tag.getCompound("Inventory");
        inventory.deserializeNBT(inventoryTag);

        addTooltip(tooltip, inventory);
    }

    private static void addTooltip(List<Component> tooltip, ToolboxInventory inventory) {
        List<ItemStack> filters = ((ToolboxInventoryAccessor) inventory).cct$getFilters();

        int displayedRows = 0;
        int allRows = 0;

        for (int i = 0; i < filters.size(); i++) {
            ItemStack filterStack = filters.get(i);
            int count = 0;

            for (int offset = 0; offset < ToolboxInventory.STACKS_PER_COMPARTMENT; offset++) {
                ItemStack stackInSlot = inventory.getStackInSlot(i * ToolboxInventory.STACKS_PER_COMPARTMENT + offset);

                if (!stackInSlot.isEmpty()) {
                    count += stackInSlot.getCount();
                }
            }

            if (count != 0) {
                allRows++;

                if (displayedRows < 4) {
                    displayedRows++;
                    MutableComponent component = filterStack.getHoverName().copy();
                    component.append(" x").append(String.valueOf(count));
                    tooltip.add(component);
                }
            }
        }

        if (allRows - displayedRows > 0) {
            tooltip.add(Component.translatable("container.shulkerBox.more", allRows - displayedRows)
                    .withStyle(ChatFormatting.ITALIC));
        }
    }
}
