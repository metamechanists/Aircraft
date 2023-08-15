package org.metamechanists.aircraft.vehicles;

import lombok.experimental.UtilityClass;
import org.metamechanists.aircraft.utils.id.simple.DisplayGroupId;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@UtilityClass
public class VehicleStorage {
    private Set<AircraftGroup> groups = new HashSet<>();

    public void add(final DisplayGroupId aircraftGroupId, final DisplayGroupId forceArrowGroupId) {
        groups.add(new AircraftGroup(aircraftGroupId, forceArrowGroupId));
    }

    public void remove(final DisplayGroupId aircraftGroupId, final DisplayGroupId forceArrowGroupId) {
        groups.remove(new AircraftGroup(aircraftGroupId, forceArrowGroupId));
    }

    public void tick() {
        groups = groups.stream().filter(AircraftGroup::isValid).collect(Collectors.toSet());
        groups.forEach(Glider::tickAircraft);
    }
}
