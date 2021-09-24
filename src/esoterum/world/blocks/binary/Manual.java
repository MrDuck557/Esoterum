package esoterum.world.blocks.binary;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import esoterum.ui.dialogs.ManualDialog;
import mindustry.gen.Building;
import mindustry.type.*;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

public class Manual extends Block{
    public Manual(String name){
        super(name);
        update = true;
        solid = true;
        destructible = true;
        configurable = true;

        buildVisibility = BuildVisibility.shown;
        category = Category.logic;
    }

    public class ManualBuild extends Building{
        public float drawRotation;
        ManualDialog manual = new ManualDialog();

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
    }
}
