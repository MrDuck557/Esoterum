package esoterum.util;

import mindustry.gen.*;

public class EsoUtil {

    // relativeTo does not account for building rotation.
    public static int relativeDirection(Building from, Building to){
        return (4 + from.relativeTo(to) - from.rotation) % 4;
    }
}
