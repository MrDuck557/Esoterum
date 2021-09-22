package esoterum.world.blocks.binary;

import arc.Core;
import arc.func.Boolf;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import mindustry.graphics.Pal;

public class LogicGate extends BinaryBlock{
    public Boolf<boolean[]> operation;
    public LogicGate(String name, boolean l, boolean b, boolean r){
        super(name);
        inputs = new boolean[]{false, l, b, r};
        outputs = new boolean[]{true, false, false, false};
        emits = true;
        rotate = true;
        drawArrow = true;

        operation = e -> false;

        drawConnectionArrows = true;
    }

    @Override
    public void load() {
        super.load();
        region = Core.atlas.find("esoterum-gate-base");
    }

    public class LogicGateBuild extends BinaryBuild {

        @Override
        public void updateTile() {
            super.updateTile();
            lastSignal = signal();
        }

        @Override
        public boolean signal() {
            return operation.get(new boolean[]{
                getSignal(nb.get(1), this),
                getSignal(nb.get(2), this),
                getSignal(nb.get(3), this)
            });
        }

        @Override
        public void draw(){
            Draw.rect(region, x, y);

            drawConnections();
            Draw.color(Color.white, Pal.accent, lastSignal ? 1f : 0f);
            Draw.rect(topRegion, x, y, rotate ? rotdeg() : 0f);
        }

        @Override
        public boolean signalFront() {
            return lastSignal;
        }
    }
}
