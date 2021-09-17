package esoterum.world.blocks.binary;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.Seq;
import esoterum.interfaces.Binaryc;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

public class BinaryBlock extends Block {
    public TextureRegion connectionRegion;
    public TextureRegion topRegion;
    // used for drawing, in order {front, left, back, right}
    public boolean[] outputs = new boolean[]{false, false, false, false};
    public boolean emits = false;

    public BinaryBlock(String name) {
        super(name);
        rotate = false;
        update = true;
        solid = true;
        destructible = true;
    }

    public BinaryBlock(String name, boolean front, boolean left, boolean back, boolean right){
        super(name);
        outputs = new boolean[]{front, left, back, right};
    }

    public class BinaryBuild extends Building implements Binaryc {
        public Seq<BinaryBuild> nb = new Seq<BinaryBuild>(4);
        public boolean[] connections = new boolean[]{false, false, false, false};

        public boolean nextSignal;
        public boolean lastSignal;

        @Override
        public void draw() {
            super.draw();

            Draw.color(Color.white, Pal.accent, lastSignal ? 1f : 0f);
            for(int i = 0; i < 4; i++){
                if(nb.get(i) != null && connections[i]) {
                    Draw.rect(connectionRegion, x, y, rotdeg() + 90 * i);
                }
            }
            Draw.rect(topRegion, x, y, rotate ? rotdeg() : 0f);
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();

            // update connected builds only when necessary
            nb.clear();
            nb.add(
                checkType(front()),
                checkType(left()),
                checkType(back()),
                checkType(right())
            );
            updateConnections();
        }

        public void updateConnections(){
            for(BinaryBuild b : nb){
                connections[nb.indexOf(b)] = connectionCheck(b, this);
            }
        }

        public boolean emits(){
            return emits;
        }

        public boolean[] outputs(){
            return outputs;
        }
    }
}
