package esoterum.world.blocks.binary;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.io.*;
import arc.math.*;
import mindustry.graphics.*;
import mindustry.logic.*;

public class BinaryButton extends BinaryBlock{
    // whether the button emits continuously (like a switch).
    public boolean continuous;
    // Buttons will have a pulse length of 60 ticks by default
    public float duration = 60;

    public TextureRegion onRegion, offRegion;

    public BinaryButton(String name, boolean cont){
        super(name);
        outputs = new boolean[]{true, true, true, true};
        configurable = true;
        continuous = cont;
        emits = true;

        config(Boolean.class, (BinaryButtonBuild b, Boolean on) -> {
            b.lastSignal = on;
            b.timer = duration;
        });
    }

    @Override
    public void load() {
        super.load();

        onRegion = Core.atlas.find(name + "-on");
        offRegion = Core.atlas.find(name + "-off");
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{
            region,
            topRegion,
            offRegion,
        };
    }

    public class BinaryButtonBuild extends BinaryBuild {
        public float timer;

        @Override
        public void updateTile() {
            enabledControlTime = 9999;
            super.updateTile();
            if (enabled){
                lastSignal = true;
            } else {
                lastSignal = false;
            }
            if(!continuous){
                if((timer -= delta()) <= 0){
                    lastSignal = false;
                    enabled = false;
                }
            }
        }

        @Override
        public boolean configTapped(){
            if(continuous){
                enabled = !enabled;
                configure(!lastSignal);
            }else{
                enabled = true;
                configure(true);
            }
            return false;
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);
            Draw.color(Color.white, Pal.accent, lastSignal ? 1f : 0f);
            for(int i = 0; i < 4; i++){
                if(connections[i]) Draw.rect(connectionRegion, x, y, rotdeg() + 90 * i);
            }
            Draw.color();
            Draw.rect(lastSignal ? onRegion : offRegion, x, y);
        }

        // yes, there is no other way to do this
        // absolutely no way.
        @Override
        public boolean signalFront() {
            return lastSignal;
        }

        @Override
        public boolean signalLeft() {
            return lastSignal;
        }

        @Override
        public boolean signalBack() {
            return lastSignal;
        }

        @Override
        public boolean signalRight() {
            return lastSignal;
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            if(revision >= 1){
                timer = read.f();
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.f(timer);
        }

        @Override
        public byte version() {
            return 1;
        }

        @Override
        public void control(LAccess type, double p1, double p2, double p3, double p4){
            if(type == LAccess.enabled){
                //controlling capability
                lastSignal = !Mathf.zero((float)p1);
            }
        }
    }
}
