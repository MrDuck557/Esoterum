package esoterum.util;

import mindustry.gen.*;

public class EsoUtil {

    // relativeTo does not account for building rotation.
    public static int relativeDirection(Building from, Building to){
        return (from.relativeTo(to) - from.rotation) % 4;
    }
}
