package net.minemod.onepiecemod.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class PacketHandler {
    private static final int PROTOCOL_VERSION = 1;

    public static final SimpleChannel INSTANCE = ChannelBuilder.named(
                    ResourceLocation.fromNamespaceAndPath("onepiecemod", "main"))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .simpleChannel();

    public static void register() {
        int id = 0;
        INSTANCE.messageBuilder(SkillCheckResultPacket.class, id++)
                .encoder(SkillCheckResultPacket::encode)
                .decoder(SkillCheckResultPacket::decode)
                .consumerNetworkThread(SkillCheckResultPacket::handle)
                .add();
    }

    public static void sendToServer(Object message) {
        INSTANCE.send(message, PacketDistributor.SERVER.noArg());
    }
}