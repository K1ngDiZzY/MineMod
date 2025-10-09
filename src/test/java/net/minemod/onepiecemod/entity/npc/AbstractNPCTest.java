package net.minemod.onepiecemod.entity.npc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the AbstractNPC entity class to ensure proper functionality.
 * These tests validate the AbstractNPC structure and behavior without modifying existing behavior.
 */
public class AbstractNPCTest {

    @Test
    @DisplayName("AbstractNPC class should be loadable")
    public void testAbstractNPCClassExists() {
        assertDoesNotThrow(() -> {
            Class.forName("net.minemod.onepiecemod.entity.npc.AbstractNPC");
        }, "AbstractNPC class should be loadable");
    }

    @Test
    @DisplayName("AbstractNPC should extend PathfinderMob")
    public void testAbstractNPCExtendsPathfinderMob() {
        assertTrue(net.minecraft.world.entity.PathfinderMob.class.isAssignableFrom(AbstractNPC.class),
                "AbstractNPC should extend PathfinderMob");
    }

    @Test
    @DisplayName("AbstractNPC should have constructor accepting EntityType and Level")
    public void testAbstractNPCConstructor() {
        assertDoesNotThrow(() -> {
            var constructor = AbstractNPC.class.getConstructor(
                    net.minecraft.world.entity.EntityType.class,
                    net.minecraft.world.level.Level.class
            );
            assertNotNull(constructor, "AbstractNPC constructor should exist");
        }, "AbstractNPC should have a constructor accepting EntityType and Level");
    }

    @Test
    @DisplayName("AbstractNPC should have registerGoals method")
    public void testRegisterGoalsMethodExists() {
        assertDoesNotThrow(() -> {
            var method = AbstractNPC.class.getDeclaredMethod("registerGoals");
            assertNotNull(method, "registerGoals method should exist");
            assertEquals("registerGoals", method.getName(), "Method name should be registerGoals");
        }, "registerGoals method should be accessible");
    }

    @Test
    @DisplayName("AbstractNPC should have isPushable method")
    public void testIsPushableMethodExists() {
        assertDoesNotThrow(() -> {
            var method = AbstractNPC.class.getMethod("isPushable");
            assertNotNull(method, "isPushable method should exist");
            assertEquals("isPushable", method.getName(), "Method name should be isPushable");
            assertEquals(boolean.class, method.getReturnType(), "isPushable should return boolean");
        }, "isPushable method should be accessible");
    }

    @Test
    @DisplayName("AbstractNPC class should be public")
    public void testAbstractNPCIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(AbstractNPC.class.getModifiers()),
                "AbstractNPC class should be public");
    }

    @Test
    @DisplayName("AbstractNPC should not be abstract")
    public void testAbstractNPCIsNotAbstract() {
        assertFalse(java.lang.reflect.Modifier.isAbstract(AbstractNPC.class.getModifiers()),
                "AbstractNPC class should not be abstract despite its name");
    }
}
