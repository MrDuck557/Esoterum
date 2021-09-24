package esoterum.world.blocks.binary;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.util.io.Reads;
import arc.util.io.Writes;
import esoterum.ui.dialogs.ManualDialog;
import mindustry.gen.Building;
import mindustry.type.*;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

public class Manual extends Block{
    ManualDialog manual;
    public Manual(String name){
        super(name);
        update = true;
        solid = true;
        destructible = true;
        configurable = true;

        buildVisibility = BuildVisibility.shown;
        category = Category.logic;
    }

    @Override
    public void init() {
        super.init();
        manual = new ManualDialog();
    }

    public class ManualBuild extends Building{
        public float drawRotation;

        @Override
        public void placed() {
            super.placed();
            drawRotation = Mathf.random(-50, 50);
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y, drawRotation);
        }

        @Override
        public boolean configTapped(){
            manual.show();
            return false;
        }

        @Override
        public byte version() {
            return 1;
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            if(revision == 1){
                drawRotation = read.f();
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.f(drawRotation);
        }
    }
}
