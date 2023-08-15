package org.metamechanists.aircraft.vehicles;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.metamechanists.aircraft.utils.PersistentDataTraverser;
import org.metamechanists.aircraft.utils.id.simple.DisplayGroupId;
import org.metamechanists.aircraft.utils.models.ModelBuilder;
import org.metamechanists.aircraft.utils.models.components.ModelCuboid;

import java.util.HashSet;
import java.util.Set;


public class Glider extends SlimefunItem {
    private static final double AERODYNAMIC_COEFFICIENT = 12 * 5;
    private static final double DRAG_COEFFICIENT_WING = 8 * 5;
    private static final double LIFT_COEFFICIENT_BODY = 2 * 5;
    private static final double LIFT_COEFFICIENT_WING = 10 * 5;

    public static final SlimefunItemStack GLIDER = new SlimefunItemStack(
            "ACR_GLIDER",
            Material.FEATHER,
            "&4&ljustin don't hurt me",
            "&cpls");

    public Glider(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        addItemHandler(onItemUse());
    }

    private static @NotNull ItemUseHandler onItemUse() {
        return event -> {
            if (event.getClickedBlock().isPresent()) {
                event.cancel();
                place(event.getClickedBlock().get().getRelative(event.getClickedFace()));
            }
        };
    }

    public static @NotNull Set<AircraftSurface> getSurfaces() {
        final Set<AircraftSurface> surfaces = new HashSet<>();
        surfaces.addAll(modelMain().getSurfaces(AERODYNAMIC_COEFFICIENT, LIFT_COEFFICIENT_BODY));
        surfaces.addAll(modelWingFront1().getSurfaces(DRAG_COEFFICIENT_WING, LIFT_COEFFICIENT_WING));
        surfaces.addAll(modelWingFront2().getSurfaces(DRAG_COEFFICIENT_WING, LIFT_COEFFICIENT_WING));
        surfaces.addAll(modelWingBack1().getSurfaces(DRAG_COEFFICIENT_WING, LIFT_COEFFICIENT_WING));
        surfaces.addAll(modelWingBack2().getSurfaces(DRAG_COEFFICIENT_WING, LIFT_COEFFICIENT_WING));
        surfaces.addAll(modelRudder().getSurfaces(DRAG_COEFFICIENT_WING, LIFT_COEFFICIENT_WING));
        return surfaces;
    }

    public static ModelCuboid modelMain() {
        return new ModelCuboid()
                .material(Material.WHITE_CONCRETE)
                .size(2.0F, 0.4F, 0.4F);
    }
    public static ModelCuboid modelWingFront1() {
        return new ModelCuboid()
                .material(Material.GRAY_CONCRETE)
                .size(0.6F, 0.1F, 1.6F)
                .location(0.5F, 0.0F, 0.8F);
    }
    public static ModelCuboid modelWingFront2() {
        return new ModelCuboid()
                .material(Material.GRAY_CONCRETE)
                .size(0.6F, 0.1F, 1.6F)
                .location(0.5F, 0.0F, -0.8F);
    }
    public static ModelCuboid modelWingBack1() {
        return new ModelCuboid()
                .material(Material.BLUE_CONCRETE)
                .size(0.4F, 0.1F, 0.8F)
                .location(-0.7F, 0.0F, 0.6F);
    }
    public static ModelCuboid modelWingBack2() {
        return new ModelCuboid()
                .material(Material.BLUE_CONCRETE)
                .size(0.4F, 0.1F, 0.8F)
                .location(-0.7F, 0.0F, -0.6F);
    }
    public static ModelCuboid modelRudder() {
        return new ModelCuboid()
                .material(Material.BLUE_CONCRETE)
                .size(0.4F, 0.8F, 0.1F)
                .location(-0.7F, 0.6F, 0.0F);
    }

    private static void place(final @NotNull Block block) {
        final DisplayGroup displayGroup = new ModelBuilder()
                .rotation(0.0, 0.0, 0.0)
                .add("main", modelMain())
                .add("wing_front_1", modelWingFront1())
                .add("wing_front_2", modelWingFront2())
                .add("wing_back_1", modelWingBack1())
                .add("wing_back_2", modelWingBack2())
                .add("rudder", modelRudder())
                .buildAtBlockCenter(block.getLocation());

        final PersistentDataTraverser traverser = new PersistentDataTraverser(displayGroup.getParentUUID());
        traverser.set("velocity", new Vector3d(0.0, 0.00001, 0.0)); // must start off with some velocity to prevent NaN issues
        traverser.set("angularVelocity", new Vector3d(0.0, 0.0, 0.0)); // roll, yaw, pitch
        traverser.set("rotation", new Vector3d(0.0, 0.0, 0.0)); // roll, yaw, pitch

        VehicleStorage.add(new DisplayGroupId(displayGroup.getParentUUID()));
    }
}
