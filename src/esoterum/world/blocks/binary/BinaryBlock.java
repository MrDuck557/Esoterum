package esoterum.world.blocks.binary;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.io.*;
import esoterum.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class BinaryBlock extends Block {
    public TextureRegion topRegion, connectionRegion;
    public TextureRegion[] baseRegions = new TextureRegion[4];
    /** in order {front, left, back, right} */
    public boolean[] outputs = new boolean[]{false, false, false, false};
    public boolean[] inputs = new boolean[]{false, false, false, false};
    public boolean emits;
    public boolean allOutputs;
    public boolean drawConnectionArrows;
    public boolean drawRot = true;
    public int baseType = 0;
    public boolean rotatedBase = false;
    public int visitLimit = 5;
    public boolean propagates = true;

    public BinaryBlock(String name) {
        super(name);
        rotate = false;
        update = true;
        solid = true;
        destructible = true;
        hideDetails = false;
        buildVisibility = BuildVisibility.shown;
        category = Category.logic;
    }

    public void load() {
        super.load();
        if(rotatedBase){
            region = Core.atlas.find("esoterum-base-" + baseType, "esoterum-base-" + baseType);
        }else{
            region = Core.atlas.find(name + "-base", "esoterum-base-" + baseType);
        }
        for(int i = 0; i < 4; i++){
            baseRegions[i] = Core.atlas.find("esoterum-base-" + baseType + "-" + i, "esoterum-base");
        }
        connectionRegion = Core.atlas.find(name + "-connection", "esoterum-connection");
        topRegion = Core.atlas.find(name, "esoterum-router"); // router supremacy
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{
            rotate && rotatedBase ? baseRegions[0] : region,
            topRegion
        };
    }

    @Override
    public boolean canReplace(Block other) {
        if(other.alwaysReplace) return true;
        return (other != this || rotate) && other instanceof BinaryBlock && size == other.size;
    }

    public class BinaryBuild extends Building {
        public BinaryBuild[] nb = new BinaryBuild[]{null, null, null, null};
        public boolean[] connections = new boolean[]{false, false, false, false};

        public boolean[] signal = new boolean[]{false, false, false, false, false};

        // Mindustry saves block placement rotation even for blocks that don't rotate.
        // Usually this doesn't cause any problems, but with the current implementation
        // it is necessary for non-rotatable binary blocks to have a rotation of 0.
        @Override
        public void created(){
            super.created();
            if(!rotate) rotation(0);
        }

        @Override
        public void placed(){
            super.placed();
            SignalGraph.addVertex(this);
            updateNeighbours();
            updateConnections();
        }

        @Override
        public void rotation(int dir){
            super.rotation(dir);
            updateNeighbours();
            updateConnections();
        }

        @Override
        public void onRemoved(){
            super.onRemoved();
            SignalGraph.clearEdges(this);
            SignalGraph.removeVertex(this);
        }

        public void updateConnections(){
            for(int i = 0; i < 4; i++){
                connections[i] = connectionCheck(nb[i], this);
            }
            SignalGraph.clearEdges(this);
            for(BinaryBuild b : getOutputs()){
                if(b != null) SignalGraph.addEdge(this, b);
            }
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
            updateNeighbours();
            updateConnections();
        }

        public void updateNeighbours(){
            //I don't like this
            nb[0] = checkType(front());
            nb[1] = checkType(left());
            nb[2] = checkType(back());
            nb[3] = checkType(right());
        }

        public void updateSignal(){}

        public BinaryBuild[] getInputs(){
            BinaryBuild[] i = new BinaryBuild[nb.length];
            int c = 0;
            for(BinaryBuild b : nb)
                if (b != null && inputs(c) && connections[c]) i[c] = b;
            return i;
        }

        public BinaryBuild[] getOutputs(){
            BinaryBuild[] o = new BinaryBuild[nb.length];
            int c = 0;
            for(BinaryBuild b : nb)
                if (b != null && outputs(c) && connections[c]) o[c] = b;
            return o;
        }

        public BinaryBlock.BinaryBuild checkType(Building b){
            if(b instanceof BinaryBlock.BinaryBuild bb) return bb;
            return null;
        }

        public boolean connectionCheck(Building from, BinaryBlock.BinaryBuild to){
            if(from == null || to == null) return false;
            if(from instanceof BinaryBlock.BinaryBuild b){
                int t = EsoUtil.relativeDirection(b, to);
                int f = EsoUtil.relativeDirection(to, b);
                return b.outputs(t) & to.inputs(f)
                    || to.outputs(f) & b.inputs(t);
            }
            return false;
        }

        public boolean getSignal(Building from, BinaryBlock.BinaryBuild to){
            if(from instanceof BinaryBlock.BinaryBuild b)
                return b.signal[EsoUtil.relativeDirection(b, to)];
            return false;
        }

        public boolean signal(){
            return signal[0] || signal[1] || signal[2] || signal[3];
        }

        public void signal(boolean b){
            signal[0] = signal[1] = signal[2] = signal[3] = b;
        }

        @Override
        public void draw(){
            drawBase();
            drawConnections();
            Draw.color(Color.white, team.color, Mathf.num(signal()));
            Draw.rect(topRegion, x, y, (rotate && drawRot) ? rotdeg() : 0f);
        }

        public void drawBase(){
            if(!rotate || !rotatedBase){
                Draw.rect(region, x, y);
            }else{
                Draw.rect(baseRegions[rotation], x, y);
            }
        }

        public void drawConnections(){
            for(int i = 0; i < 4; i++){
                if(inputs(i)) Draw.color(Color.white, team.color, Mathf.num(getSignal(nb[i], this)));
                if(outputs(i)) Draw.color(Color.white, team.color, Mathf.num(signal()));
                if(connections[i]) Draw.rect(connectionRegion, x, y, rotdeg() + 90 * i);
            }
        }

        @Override
        public void drawTeam(){
            //no
        }

        @Override
        public void drawSelect(){
            if(!drawConnectionArrows) return;
            BinaryBuild b;
            for(int i = 0; i < 4; i++){
                if(connections[i]){
                    b = nb[i];

                    Draw.z(Layer.overlayUI);
                    Lines.stroke(3f);
                    Draw.color(Pal.gray);
                    Lines.line(x, y, b.x, b.y);
                }
            }

            for(int i = 0; i < 4; i++){
                if(outputs(i) && connections[i]){
                    b = nb[i];
                    Draw.z(Layer.overlayUI + 1);
                    Drawf.arrow(x, y, b.x, b.y, 2f, 2f, signal() ? team.color : Color.white);
                }
            }

            for (int i = 0; i < 4; i++){
                if(connections[i]) {
                    b = nb[i];
                    Draw.z(Layer.overlayUI + 3);
                    Lines.stroke(1f);
                    Draw.color((outputs(i) ? signal() : getSignal(b, this)) ? team.color : Color.white);
                    Lines.line(x, y, b.x, b.y);

                    Draw.reset();
                }
            }
        }

        @Override
        public void displayBars(Table table) {
            super.displayBars(table);
            table.table(e -> {
                e.row();
                e.left();
                e.label(() -> "State: " + (signal() ? "1" : "0")).color(Color.lightGray);
            }).left();
        }

        public boolean outputs(int i){
            return outputs[i];
        }

        public boolean inputs(int i) {
            return inputs[i];
        }

        public boolean propagates(){
            return propagates;
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            if(revision >= 2){
                signal[0] = read.bool();
                signal[1] = read.bool();
                signal[2] = read.bool();
                signal[3] = read.bool();
            } else if(revision >= 1){
                signal[0] = signal[1] = signal[2] = signal[3] = read.bool();
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.bool(signal[0]);
            write.bool(signal[1]);
            write.bool(signal[2]);
            write.bool(signal[3]);
        }

        @Override
        public byte version() {
            return 2;
        }

        @Override
        public double sense(LAccess sensor){
            if(sensor == LAccess.enabled) return Mathf.num(signal());
            return super.sense(sensor);
        }
    }
}
