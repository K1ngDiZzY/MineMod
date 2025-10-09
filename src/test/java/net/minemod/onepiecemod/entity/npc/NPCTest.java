package net.minemod.onepiecemod.entity.npc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the NPC entity class to ensure proper functionality.
 * These tests validate the NPC structure and attributes without modifying existing behavior.
 */
public class NPCTest {

    @Test
    @DisplayName("NPC class should be loadable")
    public void testNPCClassExists() {
        assertDoesNotThrow(() -> {
            Class.forName("net.minemod.onepiecemod.entity.npc.NPC");
        }, "NPC class should be loadable");
    }

    @Test
    @DisplayName("NPC should extend AbstractNPC")
    public void testNPCExtendsAbstractNPC() {
        assertTrue(AbstractNPC.class.isAssignableFrom(NPC.class),
                "NPC should extend AbstractNPC");
    }

    @Test
    @DisplayName("NPC createAttributes method should exist and return AttributeSupplier.Builder")
    public void testCreateAttributesMethodExists() {
        assertDoesNotThrow(() -> {
            var method = NPC.class.getMethod("createAttributes");
            assertNotNull(method, "createAttributes method should exist");
            assertEquals("createAttributes", method.getName(), "Method name should be createAttributes");
        }, "createAttributes method should be accessible");
    }

    @Test
    @DisplayName("NPC createAttributes should configure health to 40.0")
    public void testNPCHealthAttribute() {
        var attributes = NPC.createAttributes().build();
        assertNotNull(attributes, "Attributes should not be null");
        
        // Check that MAX_HEALTH attribute is present and set to 40.0
        assertTrue(attributes.hasAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH),
                "NPC should have MAX_HEALTH attribute");
        assertEquals(40.0D, 
                attributes.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH),
                "NPC MAX_HEALTH should be 40.0");
    }

    @Test
    @DisplayName("NPC createAttributes should configure movement speed to 0.25")
    public void testNPCMovementSpeedAttribute() {
        var attributes = NPC.createAttributes().build();
        assertNotNull(attributes, "Attributes should not be null");
        
        // Check that MOVEMENT_SPEED attribute is present and set to 0.25
        assertTrue(attributes.hasAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED),
                "NPC should have MOVEMENT_SPEED attribute");
        assertEquals(0.25D, 
                attributes.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED),
                "NPC MOVEMENT_SPEED should be 0.25");
    }

    @Test
    @DisplayName("NPC should have proper constructor")
    public void testNPCConstructor() {
        assertDoesNotThrow(() -> {
            var constructor = NPC.class.getConstructor(
                    net.minecraft.world.entity.EntityType.class,
                    net.minecraft.world.level.Level.class
            );
            assertNotNull(constructor, "NPC constructor should exist");
        }, "NPC should have a constructor accepting EntityType and Level");
    }
}
