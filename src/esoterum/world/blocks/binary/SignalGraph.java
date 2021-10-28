package esoterum.world.blocks.binary;

import arc.*;
import arc.math.geom.*;
import esoterum.world.blocks.binary.transmission.*;

import java.util.*;
import java.util.concurrent.*;

public class SignalGraph {
    public static ConcurrentHashMap<BinaryBlock.BinaryBuild, Set<BinaryBlock.BinaryBuild>> hm = new ConcurrentHashMap<>();
    public static Set<BinaryBlock.BinaryBuild> sources = ConcurrentHashMap.newKeySet();
    public static boolean run = false;
    public static ForkJoinPool e = ForkJoinPool.commonPool();
    public static int millis = 16, nanos = 666666;

    public static void addVertex(BinaryBlock.BinaryBuild b){
        hm.put(b, ConcurrentHashMap.newKeySet());
        if(!b.propagates()) sources.add(b);
        //Log.info("add");
    }

    public static void addEdge(BinaryBlock.BinaryBuild a, BinaryBlock.BinaryBuild b){
        if(hm.get(a) != null){
            hm.get(a).add(b);
        }
    }

    public static void removeVertex(BinaryBlock.BinaryBuild b){
        hm.remove(b);
        if(!b.propagates()) sources.remove(b);
        //Log.info("rm");
    }

    public static void clearVertices(){
        hm.clear();
    }

    public static void removeEdge(BinaryBlock.BinaryBuild a, BinaryBlock.BinaryBuild b){
        if(hm.get(a) != null){
            hm.get(a).remove(b);
        }
    }

    public static void clear(){
        hm.clear();
        sources.clear();
    }

    public static void clearEdges(BinaryBlock.BinaryBuild b){
        if(hm.get(b) != null) hm.get(b).clear();
    }

    public static void updateSignal(BinaryBlock.BinaryBuild b){
        b.updateSignal();
    }

    public static void dfs(BinaryBlock.BinaryBuild b){
        Deque<BinaryBlock.BinaryBuild> s = new ArrayDeque<>();
        Deque<Integer> d = new ArrayDeque<>();
        HashMap<Integer, Integer> v = new HashMap<>();
        s.push(b);
        d.push(5);
        int p;
        //Log.info("start");
        while(!s.isEmpty()){
            b = s.pop();
            p = d.pop();
            updateSignal(b);
            //Log.info("mainloop");
            //Log.info("Updated " + b.getDisplayName() + " at " + String.valueOf(b.x / 8) + ", " + String.valueOf(b.y / 8) + " from " + String.valueOf(Point2.unpack(p).x) + ", " + String.valueOf(Point2.unpack(p).y));
            if(v.get(b.pos()) == null || v.get(b.pos()) != p){
                //Log.info("unvisited");
                v.put(b.pos(), p);
                //Log.info("condition");
                if(hm.get(b) != null) 
                    for(BinaryBlock.BinaryBuild bb : hm.get(b)) {
                        //Log.info("Candidate " + bb.getDisplayName() + " at " + String.valueOf(bb.x / 8) + ", " + String.valueOf(bb.y / 8) + " in direction " + String.valueOf(EsoUtil.relativeDirection(bb, b)));
                        if(b instanceof BinaryJunction.BinaryJunctionBuild) {
                            if(Math.abs(Point2.unpack(p).x - Point2.unpack(bb.pos()).x) == 2
                            || Math.abs(Point2.unpack(p).y - Point2.unpack(bb.pos()).y) == 2) {
                                s.push(bb);
                                d.push(b.pos());
                            }
                        } else if(b instanceof BinaryCJunction.BinaryCJunctionBuild) {
                            if(Math.abs(Point2.unpack(p).x - Point2.unpack(bb.pos()).x) == 1
                            && Math.abs(Point2.unpack(p).y - Point2.unpack(bb.pos()).y) == 1) {
                                s.push(bb);
                                d.push(b.pos());
                            }
                        } else if(bb.pos() != p) {
                            s.push(bb);
                            d.push(b.pos());
                        }//Log.info("subloop");
                    }
            }// else Log.info("visited");
        }
        //Log.info("end");
    }

    public static void update(){
        for(BinaryBlock.BinaryBuild b : sources){
            e.execute(() -> dfs(b));
        }
    }

    public static void run(boolean b){
        run = b;
    }

    public static void run(){
        //Log.info("run");
        while(true){
            try {
                Thread.sleep(
                    Core.settings.getInt("eso-signal-millis"),
                    Core.settings.getInt("eso-signal-nanos")
                );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(run) update();
        }
    }
}