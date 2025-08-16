package cat.katie.createcreativetweaks.infrastructure.config;

import cat.katie.createcreativetweaks.features.PermaGoggles;
import cat.katie.createcreativetweaks.infrastructure.rendering.CCTColors;
import net.createmod.catnip.config.ConfigBase;
import net.createmod.catnip.config.ui.ConfigAnnotations;
import net.createmod.catnip.theme.Color;

import javax.annotation.Nonnull;

public class CClient extends ConfigBase {
    public final ConfigEnum<PermaGoggles.PermaGogglesState> permaGoggles = e(PermaGoggles.PermaGogglesState.CREATIVE, "permaGoggles",
            "Whether to always treat the client as if they have goggles",
            "- `DISABLED` uses vanilla Create behaviour (player must be wearing goggles)",
            "- `CREATIVE` treats the player as always wearing goggles in creative / spectator",
            "- `ALWAYS` treats the player as always wearing goggles"
    );

    // no group
    public final ConfigBool renderOffhandExtendoGrip = b(true, "renderOffhandExtendoGrip",
            "Disables rendering of the first-person extendo-grip model");
    public final ConfigBool showToolboxTooltip = b(true, "showToolboxTooltip",
            "Adds a tooltip to toolboxes to view their contents"
    );

    // clipboard
    @SuppressWarnings("unused")
    public final ConfigGroup limits = group(1, "limits", "Configure client-side bypasses of various limits");
    public final ConfigBool allowIllegalCogPlacement = b(false, "allowIllegalCogPlacement",
            "Allows cog placement in all positions",
            "This is possible without modding in many ways (contraptions, schematics, etc.)"
    );
    public final ConfigBool uncapClipboards = b(false, "uncapClipboards",
            "Allows editing clipboards with excessively long text",
            "This is possible without modding using a resource pack or via schematics"
    );
    public final ConfigBool uncapPackageAddresses = b(false, "uncapPackageAddresses",
            "Removes the length and glob limits of package filters",
            "This is possible without modding using clipboards"
    );
    public final ConfigBool disableMechanicalArmRangeChecks = b(false, "disableArmRangeChecks",
            "Removes the range checks on mechanical arms",
            "This is NOT possible without modding"
    );

    // contraption order rendering
    @SuppressWarnings("unused")
    public final ConfigGroup contraptionOrder = group(1, "contraptionOrder", "Configure contraption order visualization");

    public final ConfigInt frontierOrderLineColor = i(CCTColors.TRANSLUCENT_GRAY.getRGB(), Integer.MIN_VALUE, Integer.MAX_VALUE, "frontierOrderLineColor",
            "The color to use for the frontier order lines",
            "[in Hex: #AaRrGgBb]", ConfigAnnotations.IntDisplay.HEX.asComment()
    );

    public final ConfigInt frontierTextColor = i(Color.SPRING_GREEN.getRGB(), Integer.MIN_VALUE, Integer.MAX_VALUE, "frontierTextColor",
            "The color to use for the frontier text labels",
            "[in Hex: #AaRrGgBb]", ConfigAnnotations.IntDisplay.HEX.asComment()
    );

    public final ConfigInt actorTextColor = i(CCTColors.GOLD.getRGB(), Integer.MIN_VALUE, Integer.MAX_VALUE, "actorTextColor",
            "The color to use for the text labels on actors",
            "[in Hex: #AaRrGgBb]", ConfigAnnotations.IntDisplay.HEX.asComment()
    );

    public final ConfigInt itemStorageTextColor = i(CCTColors.GRAY.getRGB(), Integer.MIN_VALUE, Integer.MAX_VALUE, "itemStorageTextColor",
            "The color to use for the text labels on item storages",
            "[in Hex: #AaRrGgBb]", ConfigAnnotations.IntDisplay.HEX.asComment()
    );

    public final ConfigInt fluidStorageTextColor = i(CCTColors.BLUE.getRGB(), Integer.MIN_VALUE, Integer.MAX_VALUE, "fluidStorageTextColor",
            "The color to use for the text labels on fluid storages",
            "[in Hex: #AaRrGgBb]", ConfigAnnotations.IntDisplay.HEX.asComment()
    );

    public final ConfigInt anchorPointTextColor = i(CCTColors.LIGHT_PURPLE.getRGB(), Integer.MIN_VALUE, Integer.MAX_VALUE, "anchorPosTextColor",
            "The color to use for the text labels on the anchor position",
            "[in Hex: #AaRrGgBb]", ConfigAnnotations.IntDisplay.HEX.asComment()
    );

    @Override
    @Nonnull
    public String getName() {
        return "client";
    }
}
