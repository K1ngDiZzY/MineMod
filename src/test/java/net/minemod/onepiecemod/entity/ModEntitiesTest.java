package net.minemod.onepiecemod.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ModEntities class to ensure proper NPC entity registration.
 * These tests validate the entity registration structure without modifying existing behavior.
 */
public class ModEntitiesTest {

    @Test
    @DisplayName("ModEntities class should be loadable")
    public void testModEntitiesClassExists() {
        assertDoesNotThrow(() -> {
            Class.forName("net.minemod.onepiecemod.entity.ModEntities");
        }, "ModEntities class should be loadable");
    }

    @Test
    @DisplayName("ModEntities should have ENTITY_TYPES DeferredRegister")
    public void testEntityTypesDeferredRegisterExists() {
        assertDoesNotThrow(() -> {
            var field = ModEntities.class.getDeclaredField("ENTITY_TYPES");
            assertNotNull(field, "ENTITY_TYPES field should exist");
            assertEquals("ENTITY_TYPES", field.getName(), "Field name should be ENTITY_TYPES");
        }, "ENTITY_TYPES DeferredRegister should be accessible");
    }

    @Test
    @DisplayName("ModEntities should have NPC RegistryObject")
    public void testNPCRegistryObjectExists() {
        assertDoesNotThrow(() -> {
            var field = ModEntities.class.getDeclaredField("NPC");
            assertNotNull(field, "NPC field should exist");
            assertEquals("NPC", field.getName(), "Field name should be NPC");
        }, "NPC RegistryObject should be accessible");
    }

    @Test
    @DisplayName("ModEntities NPC RegistryObject should not be null")
    public void testNPCRegistryObjectNotNull() {
        assertNotNull(ModEntities.NPC, "NPC RegistryObject should not be null");
    }

    @Test
    @DisplayName("ModEntities should have register method")
    public void testRegisterMethodExists() {
        assertDoesNotThrow(() -> {
            var method = ModEntities.class.getMethod("register", 
                    net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.class);
            assertNotNull(method, "register method should exist");
            assertEquals("register", method.getName(), "Method name should be register");
        }, "register method should be accessible");
    }

    @Test
    @DisplayName("ModEntities class should be public")
    public void testModEntitiesIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(ModEntities.class.getModifiers()),
                "ModEntities class should be public");
    }

    @Test
    @DisplayName("ENTITY_TYPES should be public static final")
    public void testEntityTypesModifiers() throws NoSuchFieldException {
        var field = ModEntities.class.getDeclaredField("ENTITY_TYPES");
        int modifiers = field.getModifiers();
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers),
                "ENTITY_TYPES should be public");
        assertTrue(java.lang.reflect.Modifier.isStatic(modifiers),
                "ENTITY_TYPES should be static");
        assertTrue(java.lang.reflect.Modifier.isFinal(modifiers),
                "ENTITY_TYPES should be final");
    }

    @Test
    @DisplayName("NPC RegistryObject should be public static final")
    public void testNPCModifiers() throws NoSuchFieldException {
        var field = ModEntities.class.getDeclaredField("NPC");
        int modifiers = field.getModifiers();
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers),
                "NPC should be public");
        assertTrue(java.lang.reflect.Modifier.isStatic(modifiers),
                "NPC should be static");
        assertTrue(java.lang.reflect.Modifier.isFinal(modifiers),
                "NPC should be final");
    }
}
