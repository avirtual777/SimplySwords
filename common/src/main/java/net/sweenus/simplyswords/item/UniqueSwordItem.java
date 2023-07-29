package net.sweenus.simplyswords.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.sweenus.simplyswords.SimplySwords;
import net.sweenus.simplyswords.registry.ItemsRegistry;
import net.sweenus.simplyswords.util.HelperMethods;
import net.sweenus.simplyswords.util.RunicMethods;

import java.util.List;

public class UniqueSwordItem extends SwordItem {

    public static int maxUseTime;
    String iRarity = "UNIQUE";

    public UniqueSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings.fireproof());
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        NbtCompound nbt = stack.getOrCreateNbt();
        //Socket rolling
        if (nbt.getString("runic_power").isEmpty() && nbt.getString("nether_power").isEmpty()) {
            float socketChance = (float) (Math.random() * 100);
            float socketChance2 = (float) (Math.random() * 100);

            if (socketChance > 49) nbt.putString("runic_power", "socket_empty");
            else if (socketChance < 50) nbt.putString("runic_power", "no_socket");
            if (socketChance2 > 49) nbt.putString("nether_power", "socket_empty");
            else if (socketChance2 < 50) nbt.putString("nether_power", "no_socket");
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player,
                             StackReference cursorStackReference) {
        if (SimplySwords.generalConfig.enableUniqueGemSockets) {
            String powerType;
            if (otherStack.isOf(ItemsRegistry.RUNEFUSED_GEM.get()) && stack.getOrCreateNbt().getString("runic_power").equals("socket_empty")
                    && !otherStack.getOrCreateNbt().getString("runic_power").isEmpty()) {
                powerType = "runic_power";
            } else if (otherStack.isOf(ItemsRegistry.NETHERFUSED_GEM.get()) && stack.getOrCreateNbt().getString("nether_power").equals("socket_empty")
                    && !otherStack.getOrCreateNbt().getString("nether_power").isEmpty()) {
                powerType = "nether_power";
            } else {
                return false;
            }
            String powerSelection = otherStack.getOrCreateNbt().getString(powerType);
            stack.getOrCreateNbt().putString(powerType, powerSelection);
            player.getWorld().playSoundFromEntity(null, player, SoundEvents.BLOCK_ANVIL_USE, player.getSoundCategory(), 1, 1);
            otherStack.decrement(1);
        }
        return false;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient()) {
            HelperMethods.playHitSounds(attacker, target);

            switch (stack.getOrCreateNbt().getString("runic_power")) {
                case "freeze" -> RunicMethods.postHitRunicFreeze(stack, target, attacker);
                case "wildfire" -> RunicMethods.postHitRunicWildfire(stack, target, attacker);
                case "slow" -> RunicMethods.postHitRunicSlow(stack, target, attacker);
                case "greater_slow" -> RunicMethods.postHitRunicGreaterSlow(stack, target, attacker);
                case "swiftness" -> RunicMethods.postHitRunicSwiftness(stack, target, attacker);
                case "greater_swiftness" -> RunicMethods.postHitRunicGreaterSwiftness(stack, target, attacker);
                case "float" -> RunicMethods.postHitRunicFloat(stack, target, attacker);
                case "greater_float" -> RunicMethods.postHitRunicGreaterFloat(stack, target, attacker);
                case "zephyr" -> RunicMethods.postHitRunicZephyr(stack, target, attacker);
                case "greater_zephyr" -> RunicMethods.postHitRunicGreaterZephyr(stack, target, attacker);
                case "shielding" -> RunicMethods.postHitRunicShielding(stack, target, attacker);
                case "greater_shielding" -> RunicMethods.postHitRunicGreaterShielding(stack, target, attacker);
                case "stoneskin" -> RunicMethods.postHitRunicStoneskin(stack, target, attacker);
                case "greater_stoneskin" -> RunicMethods.postHitRunicGreaterStoneskin(stack, target, attacker);
                case "trailblaze" -> RunicMethods.postHitRunicTrailblaze(stack, target, attacker);
                case "greater_trailblaze" -> RunicMethods.postHitRunicGreaterTrailblaze(stack, target, attacker);
                case "weaken" -> RunicMethods.postHitRunicWeaken(stack, target, attacker);
                case "greater_weaken" -> RunicMethods.postHitRunicGreaterWeaken(stack, target, attacker);
                case "imbued" -> RunicMethods.postHitRunicImbued(stack, target, attacker);
                case "greater_imbued" -> RunicMethods.postHitRunicGreaterImbued(stack, target, attacker);
                case "pincushion" -> RunicMethods.postHitRunicPinCushion(stack, target, attacker);
                case "greater_pincushion" -> RunicMethods.postHitRunicGreaterPinCushion(stack, target, attacker);
            }

            switch (stack.getOrCreateNbt().getString("nether_power")) {
                case "echo" -> RunicMethods.postHitNetherEcho(stack, target, attacker);
                case "berserk" -> RunicMethods.postHitNetherBerserk(stack, target, attacker);
                case "radiance" -> RunicMethods.postHitNetherRadiance(stack, target, attacker);
                case "onslaught" -> RunicMethods.postHitNetherOnslaught(stack, target, attacker);
                case "nullification" -> RunicMethods.postHitNetherNullification(stack, target, attacker);
            }
        }

        return super.postHit(stack, target, attacker);
    }

    @Override
    public Text getName(ItemStack stack) {

        Style COMMON = HelperMethods.getStyle("common");
        Style UNIQUE = HelperMethods.getStyle("unique");
        Style LEGENDARY = HelperMethods.getStyle("legendary");

        if (this.getDefaultStack().isOf(ItemsRegistry.AWAKENED_LICHBLADE.get())
                || this.getDefaultStack().isOf(ItemsRegistry.HARBINGER.get())
                || this.getDefaultStack().isOf(ItemsRegistry.SUNFIRE.get())) {
            this.iRarity = "LEGENDARY";
            return Text.translatable(this.getTranslationKey(stack)).setStyle(LEGENDARY);
        }

        if (this.iRarity.equals("UNIQUE")) return Text.translatable(this.getTranslationKey(stack)).setStyle(UNIQUE);
        else return Text.translatable(this.getTranslationKey(stack)).setStyle(COMMON);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext
            tooltipContext) {

        Style RUNIC = HelperMethods.getStyle("runic");
        Style NETHERFUSED = HelperMethods.getStyle("legendary");
        Style RIGHTCLICK = HelperMethods.getStyle("rightclick");
        Style TEXT = HelperMethods.getStyle("text");

        NbtCompound nbt = itemStack.getOrCreateNbt();
        if (!nbt.getString("runic_power").equals("no_socket") || !nbt.getString("nether_power").equals("no_socket")) {
            tooltip.add(Text.literal(""));
        }
        if (Screen.hasAltDown()) {
            if (nbt.getString("runic_power").contains("greater")) {
                tooltip.add(Text.translatable("item.simplyswords.greater_runic_power").setStyle(RUNIC));
            }
            switch (nbt.getString("runic_power")) {
                case "socket_empty" ->
                        tooltip.add(Text.translatable("item.simplyswords.empty_runic_slot").formatted(Formatting.GRAY));
                case "freeze" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.freeze").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.freezesworditem.tooltip2").setStyle(TEXT));
                }
                case "wildfire" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.wildfire").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.wildfiresworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.wildfiresworditem.tooltip3").setStyle(TEXT));
                }
                case "slow", "greater_slow" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.slow").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.slownesssworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.slownesssworditem.tooltip3").setStyle(TEXT));
                }
                case "swiftness", "greater_swiftness" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.swiftness").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.speedsworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.speedsworditem.tooltip3").setStyle(TEXT));
                }
                case "float", "greater_float" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.float").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.levitationsworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.levitationsworditem.tooltip3").setStyle(TEXT));
                }
                case "zephyr", "greater_zephyr" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.zephyr").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.zephyrsworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.zephyrsworditem.tooltip3").setStyle(TEXT));
                }
                case "shielding", "greater_shielding" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.shielding").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.shieldingsworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.shieldingsworditem.tooltip3").setStyle(TEXT));
                }
                case "stoneskin", "greater_stoneskin" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.stoneskin").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.stoneskinsworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.stoneskinsworditem.tooltip3").setStyle(TEXT));
                }
                case "frost_ward" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.frost_ward").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.frostwardsworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.frostwardsworditem.tooltip3").setStyle(TEXT));
                }
                case "trailblaze", "greater_trailblaze" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.trailblaze").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.trailblazesworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.trailblazesworditem.tooltip3").setStyle(TEXT));
                }
                case "active_defence" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.active_defence").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.activedefencesworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.activedefencesworditem.tooltip3").setStyle(TEXT));
                }
                case "weaken", "greater_weaken" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.weaken").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.weakensworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.weakensworditem.tooltip3").setStyle(TEXT));
                }
                case "unstable" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.unstable").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.unstablesworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.unstablesworditem.tooltip3").setStyle(TEXT));
                }
                case "momentum", "greater_momentum" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.momentum").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.momentumsworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.momentumsworditem.tooltip3").setStyle(TEXT));
                }
                case "imbued", "greater_imbued" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.imbued").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.imbuedsworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.imbuedsworditem.tooltip3").setStyle(TEXT));
                }
                case "pincushion", "greater_pincushion" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.pincushion").setStyle(RUNIC));
                    tooltip.add(Text.translatable("item.simplyswords.pincushionsworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.pincushionsworditem.tooltip3").setStyle(TEXT));
                }
                case "ward" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.ward").setStyle(RUNIC));
                    tooltip.add(Text.literal(""));
                    tooltip.add(Text.translatable("item.simplyswords.onrightclick").setStyle(RIGHTCLICK));
                    tooltip.add(Text.translatable("item.simplyswords.wardsworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.wardsworditem.tooltip3").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.wardsworditem.tooltip4").setStyle(TEXT));
                }
                case "immolation" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.runefused_power.immolation").setStyle(RUNIC));
                    tooltip.add(Text.literal(""));
                    tooltip.add(Text.translatable("item.simplyswords.onrightclick").setStyle(RIGHTCLICK));
                    tooltip.add(Text.translatable("item.simplyswords.immolationsworditem.tooltip2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.immolationsworditem.tooltip3").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.immolationsworditem.tooltip4").setStyle(TEXT));
                }
            }

            if (!nbt.getString("runic_power").equals("no_socket") && !nbt.getString("nether_power").equals("no_socket")) {
                tooltip.add(Text.literal(""));
            }

            switch (nbt.getString("nether_power")) {
                case "socket_empty" ->
                        tooltip.add(Text.translatable("item.simplyswords.empty_nether_slot").formatted(Formatting.GRAY));
                case "echo" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.echo").setStyle(NETHERFUSED));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.echo.description").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.echo.description2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.echo.description3").setStyle(TEXT));
                }
                case "berserk" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.berserk").setStyle(NETHERFUSED));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.berserk.description").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.berserk.description2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.berserk.description3").setStyle(TEXT));
                }
                case "radiance" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.radiance").setStyle(NETHERFUSED));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.radiance.description").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.radiance.description2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.radiance.description3").setStyle(TEXT));
                }
                case "onslaught" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.onslaught").setStyle(NETHERFUSED));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.onslaught.description").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.onslaught.description2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.onslaught.description3").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.onslaught.description4").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.onslaught.description5").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.onslaught.description6").setStyle(TEXT));
                }
                case "nullification" -> {
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.nullification").setStyle(NETHERFUSED));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.nullification.description").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.nullification.description2").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.nullification.description3").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.nullification.description4").setStyle(TEXT));
                    tooltip.add(Text.translatable("item.simplyswords.uniquesworditem.netherfused_power.nullification.description5").setStyle(TEXT));
                }
            }
        } else if (!nbt.getString("runic_power").equals("no_socket") || !nbt.getString("nether_power").equals("no_socket")) {
            tooltip.add(Text.translatable("item.simplyswords.common.showtooltip").formatted(Formatting.GRAY));
        }
    }
}