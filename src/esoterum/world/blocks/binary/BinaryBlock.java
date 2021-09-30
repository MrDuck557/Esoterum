package esoterum.world.blocks.binary;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.io.*;
import esoterum.interfaces.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import mindustry.logic.*;
import mindustry.core.*;

public class BinaryBlock extends Block {
    public TextureRegion connectionRegion;
    public TextureRegion topRegion;
    /** in order {front, left, back, right} */
    public boolean[] outputs = new boolean[]{false, false, false, false};
    /** in order {front, left, back, right} */
    public boolean[] inputs = new boolean[]{false, false, false, false};
    public boolean emits;
    public boolean drawConnectionArrows;
    public boolean drawRot = true;

    public BinaryBlock(String name) {
        super(name);
        rotate = false;
        update = true;
        solid = true;
        destructible = true;
        buildVisibility = BuildVisibility.shown;

        category = Category.logic;
    }

    public void load() {
        super.load();
        region = Core.atlas.find(name + "-base", "esoterum-base");
        connectionRegion = Core.atlas.find(name + "-connection", "esoterum-connection");
        topRegion = Core.atlas.find(name + "-top", "esoterum-router-top"); // router supremacy
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{
            region,
            topRegion
        };
    }

    @Override
    public boolean canReplace(Block other) {
        if(other.alwaysReplace) return true;
        return (other != this || rotate) && other instanceof BinaryBlock && size == other.size;
    }

    public class BinaryBuild extends Building implements Binaryc {
        /** in order {front, left, back, right} */
        public Seq<BinaryBuild> nb = new Seq<>(4);
        public boolean[] connections = new boolean[]{false, false, false, false};

        public boolean nextSignal;
        public boolean lastSignal;

        @Override
        public void draw(){
            super.draw();

            drawConnections();
            Draw.color(Color.white, Pal.accent, lastSignal ? 1f : 0f);
            Draw.rect(topRegion, x, y, (rotate && drawRot) ? rotdeg() : 0f);
        }

        public void drawConnections(){
            Draw.color(Color.white, Pal.accent, lastSignal ? 1f : 0f);
            for(int i = 0; i < 4; i++){
                if(connections[i]) Draw.rect(connectionRegion, x, y, rotdeg() + 90 * i);
            }
        }

        // bad, bad, bad, bad, bad, bad, bad, bad, bad, bad, bad
        // TODO fix layering without DOING THIS SHIT
        @Override
        public void drawSelect(){
            if(!drawConnectionArrows) return;
            BinaryBuild b;
            for(int i = 0; i < 4; i++){
                if(connections[i]){
                    b = nb.get(i);

                    Draw.z(Layer.overlayUI);
                    Lines.stroke(3f);
                    Draw.color(Pal.gray);
                    Lines.line(x, y, b.x, b.y);
                }
            }

            for(int i = 0; i < 4; i++){
                if(outputs()[i] && connections[i]){
                    b = nb.get(i);
                    Draw.z(Layer.overlayUI + 1);
                    Drawf.arrow(x, y, b.x, b.y, 2f, 2f, lastSignal ? Pal.accent : Color.white);
                }
            }

            for (int i = 0; i < 4; i++){
                if(connections[i]) {
                    b = nb.get(i);
                    Draw.z(Layer.overlayUI + 3);
                    Lines.stroke(1f);
                    Draw.color((outputs()[i] ? lastSignal : getSignal(b, this)) ? Pal.accent : Color.white);
                    Lines.line(x, y, b.x, b.y);

                    Draw.reset();
                }
            }
        }

        // Mindustry saves block placement rotation even for blocks that don't rotate.
        // Usually this doesn't cause any problems, but with the current implementation
        // it is necessary for non-rotatable binary blocks to have a rotation of 0.
        @Override
        public void created(){
            super.created();
            if(!rotate) rotation(0);
        }

        // connections
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
            for(int i = 0; i < 4; i++){
                connections[i] = connectionCheck(nb.get(i), this);
            }
        }

        @Override
        public void displayBars(Table table) {
            super.displayBars(table);
            table.table(e -> {
                Runnable rebuild = () -> {
                    e.clearChildren();
                    e.row();
                    e.left();
                    e.label(() -> "State: " + (lastSignal ? "1" : "0")).color(Color.lightGray);
                };

                e.update(rebuild);
            }).left();
        }

        // emission
        public boolean emits(){
            return emits;
        }

        public boolean[] outputs(){
            return outputs;
        }
        public boolean[] inputs() {
            return inputs;
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            if(revision >= 1){
                lastSignal = read.bool();
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.bool(lastSignal);
        }

        @Override
        public byte version() {
            return 1;
        }

        @Override
        public double sense(LAccess sensor){
            return switch(sensor){
                case x -> World.conv(x);
                case y -> World.conv(y);
                case dead -> !isValid() ? 1 : 0;
                case team -> team.id;
                case health -> health;
                case maxHealth -> maxHealth;
                case efficiency -> efficiency();
                case timescale -> timeScale;
                case range -> 0;
                case rotation -> rotation;
                case totalItems -> items == null ? 0 : items.total();
                case totalLiquids -> liquids == null ? 0 : liquids.total();
                case totalPower -> power == null || !block.consumes.hasPower() ? 0 : power.status * (block.consumes.getPower().buffered ? block.consumes.getPower().capacity : 1f);
                case itemCapacity -> block.hasItems ? block.itemCapacity : 0;
                case liquidCapacity -> block.hasLiquids ? block.liquidCapacity : 0;
                case powerCapacity -> block.consumes.hasPower() ? block.consumes.getPower().capacity : 0f;
                case powerNetIn -> power == null ? 0 : power.graph.getLastScaledPowerIn() * 60;
                case powerNetOut -> power == null ? 0 : power.graph.getLastScaledPowerOut() * 60;
                case powerNetStored -> power == null ? 0 : power.graph.getLastPowerStored();
                case powerNetCapacity -> power == null ? 0 : power.graph.getLastCapacity();
                //sensing capability
                case enabled -> lastSignal ? 1 : 0;
                case controlled -> 0;
                case payloadCount -> getPayload() != null ? 1 : 0;
                case size -> block.size;
                default -> Float.NaN;
            };
        }
    }
}
