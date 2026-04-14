package me.aleksilassila.litematica.printer.v1_21.actions;

import me.aleksilassila.litematica.printer.v1_21.implementation.PrinterPlacementContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.Input;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Direction;
import net.minecraft.util.Hand;

public class PrepareAction extends Action {
//    public final Direction lookDirection;
//    public final boolean requireSneaking;
//    public final Item item;

//    public PrepareAction(Direction lookDirection, boolean requireSneaking, Item item) {
//        this.lookDirection = lookDirection;
//        this.requireSneaking = requireSneaking;
//        this.item = item;
//    }
//
//    public PrepareAction(Direction lookDirection, boolean requireSneaking, BlockState requiredState) {
//        this(lookDirection, requireSneaking, requiredState.getBlock().asItem());
//    }

    public final PrinterPlacementContext context;

    public boolean modifyYaw = true;
    public boolean modifyPitch = true;
    public float yaw = 0;
    public float pitch = 0;

    public PrepareAction(PrinterPlacementContext context) {
        this.context = context;

        Direction lookDirection = context.lookDirection;

        if (lookDirection != null && lookDirection.getAxis().isHorizontal()) {
            this.yaw = lookDirection.getPositiveHorizontalDegrees();
        } else {
            this.modifyYaw = false;
        }

        if (lookDirection == Direction.UP) {
            this.pitch = -90;
        } else if (lookDirection == Direction.DOWN) {
            this.pitch = 90;
        } else if (lookDirection != null) {
            this.pitch = 0;
        } else {
            this.modifyPitch = false;
        }
    }

    public PrepareAction(PrinterPlacementContext context, float yaw, float pitch) {
        this.context = context;

        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void send(MinecraftClient client, ClientPlayerEntity player) {
        ItemStack itemStack = context.getStack();
        int slot = context.requiredItemSlot;

        if (itemStack != null) {
            PlayerInventory inventory = player.getInventory();

            // This thing is straight from MinecraftClient#doItemPick()
            if (player.getAbilities().creativeMode) {
                inventory.swapStackWithHotbar(itemStack);
                client.interactionManager.clickCreativeStack(player.getStackInHand(Hand.MAIN_HAND), 36 + inventory.getSelectedSlot());
            } else if (slot != -1) {
                if (PlayerInventory.isValidHotbarIndex(slot)) {
                    inventory.setSelectedSlot(slot);
                } else {
                    client.interactionManager.pickItem(slot);
                }
            }
        }

        if (modifyPitch || modifyYaw) {
            float yaw = modifyYaw ? this.yaw : player.getYaw();
            float pitch = modifyPitch ? this.pitch : player.getPitch();

            PlayerMoveC2SPacket packet = new PlayerMoveC2SPacket.Full(player.getX(), player.getY(), player.getZ(), yaw, pitch, player.isOnGround(), false);

            player.networkHandler.sendPacket(packet);
        }

        player.networkHandler.sendPacket(new PlayerInputC2SPacket(new Input(false, false, false, false, false, context.shouldSneak, false)));
    }

    @Override
    public String toString() {
        return "PrepareAction{" +
                "context=" + context +
                '}';
    }
}
