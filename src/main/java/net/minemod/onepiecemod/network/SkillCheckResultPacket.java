package net.minemod.onepiecemod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minemod.onepiecemod.entity.interfaces.Pickpocketable;

public class SkillCheckResultPacket {
    private final int mobId;
    private final boolean success;

    public SkillCheckResultPacket(int mobId, boolean success) {
        this.mobId = mobId;
        this.success = success;
    }

    public static void encode(SkillCheckResultPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.mobId);
        buffer.writeBoolean(msg.success);
    }

    public static SkillCheckResultPacket decode(FriendlyByteBuf buffer) {
        return new SkillCheckResultPacket(buffer.readInt(), buffer.readBoolean());
    }

    public static void handle(SkillCheckResultPacket payload, CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                Entity entity = player.level().getEntity(payload.mobId);
                if (entity instanceof PathfinderMob mob && entity instanceof Pickpocketable pickpocketable) {
                    if (payload.success) {
                        pickpocketable.onPickpocketSuccess(player, mob);
                    } else {
                        pickpocketable.onPickpocketFailed(player, mob);
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}