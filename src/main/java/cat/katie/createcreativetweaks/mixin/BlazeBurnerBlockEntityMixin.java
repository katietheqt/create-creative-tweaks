package cat.katie.createcreativetweaks.mixin;

import cat.katie.createcreativetweaks.util.CCTLang;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = BlazeBurnerBlockEntity.class, remap = false)
public abstract class BlazeBurnerBlockEntityMixin extends SmartBlockEntity implements IHaveGoggleInformation {

    @Shadow
    public boolean isCreative;

    @Shadow public abstract BlazeBurnerBlock.HeatLevel getHeatLevelFromBlock();

    @Shadow protected int remainingBurnTime;

    public BlazeBurnerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/processing/burner/BlazeBurnerBlockEntity;shouldTickAnimation()Z"
            )
    )
    private void alwaysTickRemainingBurnTime(CallbackInfo ci) {
        if (!isCreative && remainingBurnTime > 0) {
            remainingBurnTime--;
        }
    }

    @Override
    @Unique
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        switch (getHeatLevelFromBlock()) {
            case NONE, SMOULDERING -> {
                return false;
            }
            case FADING, KINDLED -> cct$addBurningTooltip(tooltip, false);
            case SEETHING -> cct$addBurningTooltip(tooltip, true);
        }

        return true;
    }

    @Unique
    private void cct$addBurningTooltip(List<Component> tooltip, boolean isSuperHeated) {
        HeatCondition condition = isSuperHeated ? HeatCondition.SUPERHEATED : HeatCondition.HEATED;
        LangBuilder heatBuilder = CreateLang.translate(condition.getTranslationKey()).color(condition.getColor());

        LangBuilder timeBuilder;

        if (isCreative) {
            timeBuilder = CCTLang.text("∞").style(ChatFormatting.LIGHT_PURPLE);
        } else {
            int burnTimeSeconds = (int)(remainingBurnTime / 20.0d);
            timeBuilder = CCTLang.number(burnTimeSeconds).style(ChatFormatting.WHITE);
        }

        CCTLang.translate("burner.header").forGoggles(tooltip);

        CCTLang.translate(
                "burner.tooltip",
                heatBuilder,
                timeBuilder
        ).style(ChatFormatting.GRAY).forGoggles(tooltip);
    }
}
