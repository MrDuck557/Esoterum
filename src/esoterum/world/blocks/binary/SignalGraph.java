package esoterum.world.blocks.binary;

import java.util.*;
import java.util.concurrent.*;

import esoterum.util.*;

public class SignalGraph {
    public static ConcurrentHashMap<BinaryBlock.BinaryBuild, Set<BinaryBlock.BinaryBuild>> hm = new ConcurrentHashMap<>();
    public static Set<BinaryBlock.BinaryBuild> sources = ConcurrentHashMap.newKeySet();
    public static boolean run = false;

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
        Stack<BinaryBlock.BinaryBuild> s = new Stack<>();
        HashMap<BinaryBlock.BinaryBuild, Integer> h = new HashMap<>();
        s.push(b);
        BinaryBlock.BinaryBuild p;
        while(!s.isEmpty()){
            p = b;
            b = s.pop();
            updateSignal(b);
            int dir = EsoUtil.relativeDirection(b, p);
            if(h.get(b) == null || h.get(b) != dir){
                h.put(b, dir);
                if(hm.get(b) != null) 
                    for(BinaryBlock.BinaryBuild bb : hm.get(b)) 
                        s.push(bb);
            }
        }
    }

    public static void update(){
        for(BinaryBlock.BinaryBuild b : sources){
            dfs(b);
        }
    }

    public static void run(boolean b){
        run = b;
    }

    public static void run(){
        //Log.info("run");
        while(true){
            //Log.info(run);
            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(run) update();
        }
    }
    /*
    public static void readGraph(StringMap map){
        hm.clear();
        sources.clear();
        if(map.get("graph") != null)
            readMap(JsonIO.json.fromJson(StringMap.class, String.class, map.get("graph")));
        if(map.get("source") != null)
            readSet(JsonIO.json.fromJson(IntIntMap.class, String.class, map.get("source")), sources);
    }

    public static void readMap(StringMap map){
        for(StringMap.Entry<String, String> e : map.entries()){
            hm.put((BinaryBlock.BinaryBuild)Vars.world.build(Integer.valueOf(e.key)), new HashSet<>());
            var tmp = hm.get((BinaryBlock.BinaryBuild)Vars.world.build(Integer.valueOf(e.key)));
            if(tmp != null)
                readSet(JsonIO.json.fromJson(IntIntMap.class, String.class, e.value), tmp);
        }
    }

    public static void readSet(IntIntMap map, Set<BinaryBlock.BinaryBuild> h){
        for(IntIntMap.Entry e : map.entries()){
            h.add((BinaryBlock.BinaryBuild)Vars.world.build(e.value));
        }
    }

    public static void writeGraph(StringMap map){
        map.put("graph", JsonIO.json.toJson(writeMap(), StringMap.class, String.class));
        map.put("sources", JsonIO.json.toJson(writeSet(sources), IntIntMap.class, String.class));
    }

    public static StringMap writeMap(){
        StringMap map = new StringMap();
        for(BinaryBlock.BinaryBuild b : hm.keySet()){
            map.put(Integer.toString(b.pos()), JsonIO.json.toJson(writeSet(hm.get(b)), IntIntMap.class, String.class));
        }
        return map;
    }
    
    public static IntIntMap writeSet(Set<BinaryBlock.BinaryBuild> h){
        IntIntMap map = new IntIntMap();
        int i = 0;
        for(BinaryBlock.BinaryBuild b : h){
            map.put(i, b.pos());
            i++;
        }
        return map;
    }
    */
}
