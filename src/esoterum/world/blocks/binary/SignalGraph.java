package esoterum.world.blocks.binary;

import arc.*;
import arc.util.Log;
import esoterum.world.blocks.binary.transmission.*;

import java.util.*;
import java.util.concurrent.*;

public class SignalGraph {
    public static ConcurrentHashMap<BinaryBlock.BinaryBuild, Set<BinaryBlock.BinaryBuild>> hm = new ConcurrentHashMap<>();
    public static Set<BinaryBlock.BinaryBuild> sources = ConcurrentHashMap.newKeySet();
    public static boolean run = false;
    public static ForkJoinPool e = ForkJoinPool.commonPool();
    public static int millis = 16, nanos = 666666;

    public static class Edge {
        public BinaryBlock.BinaryBuild from, to;

        public Edge(BinaryBlock.BinaryBuild f, BinaryBlock.BinaryBuild t){
            from = f;
            to = t;
        }

        @Override
        public boolean equals(Object o){
            if(o == null) return false;
            if(o instanceof Edge e){
                return from == e.from && to == e.to;
            }
            return false;
        }

        @Override
        public int hashCode(){
            return from.hashCode() * to.hashCode();
        }
    }

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
        //Log.info("start");
        Deque<Edge> stack = new ArrayDeque<>();
        Edge current;
        HashMap<Edge, Boolean> visited = new HashMap<>();
        stack.push(new Edge(b, b));
        while(!stack.isEmpty()){
            //Log.info("mainloop");
            current = stack.pop();
            if(visited.get(current) == null || !visited.get(current)){
                //Log.info("unvisited");
                if(current.to.updateSignal() || true)
                    for(BinaryBlock.BinaryBuild next : hm.get(current.to)){
                        //Log.info("subloop");
                        Edge candidate = new Edge(current.to, next);
                        //Log.info("Candidate " + next.getDisplayName() + " at " + next.x / 8 + ", " + next.y / 8 + " from " + current.to.x / 8 + ", " + current.to.y / 8);
                        if(visited.get(candidate) == null || !visited.get(candidate)){
                            if(next instanceof BinaryJunction.BinaryJunctionBuild) {
                                if(Math.abs(current.from.x - next.x) == 16
                                || Math.abs(current.from.y - next.y) == 16) stack.push(candidate);
                            } else if(next instanceof BinaryCJunction.BinaryCJunctionBuild) {
                                if(Math.abs(current.from.x - next.x) == 8
                                && Math.abs(current.from.y - next.y) == 8) stack.push(candidate);
                            } else if(next.pos() != current.from.pos()) stack.push(candidate);
                        }
                    }
                visited.put(current, true);
                //Log.info("Updated " + current.to.getDisplayName() + " at " + current.to.x / 8 + ", " + current.to.y / 8 + " from " + current.from.x / 8 + ", " + current.from.y / 8);
            } //else Log.info("visited");
            
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