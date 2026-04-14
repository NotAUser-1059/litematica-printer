package me.aleksilassila.litematica.printer.v1_21.actions;

import net.minecraft.entity.player.Input;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;

public class ReleaseShiftAction extends Action {
    @Override
    public void send(MinecraftClient client, ClientPlayerEntity player) {
        player.networkHandler.sendPacket(new PlayerInputC2SPacket(new Input(false, false, false, false, false, false, false)));
    }
}
