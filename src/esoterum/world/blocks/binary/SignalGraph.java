package esoterum.world.blocks.binary;

import java.util.*;

import arc.struct.*;
import esoterum.util.*;
import mindustry.Vars;
import mindustry.io.*;

public class SignalGraph {
    public static HashMap<BinaryBlock.BinaryBuild, HashSet<BinaryBlock.BinaryBuild>> hm = new HashMap<>();
    public static HashSet<BinaryBlock.BinaryBuild> sources = new HashSet<>();

    public static void addVertex(BinaryBlock.BinaryBuild b){
        hm.put(b, new HashSet<>());
        if(!b.propagates()) sources.add(b);
    }

    public static void addEdge(BinaryBlock.BinaryBuild a, BinaryBlock.BinaryBuild b){
        if(hm.get(a) != null){
            hm.get(a).add(b);
        }
    }

    public static void removeVertex(BinaryBlock.BinaryBuild b){
        hm.remove(b);
    }

    public static void removeEdge(BinaryBlock.BinaryBuild a, BinaryBlock.BinaryBuild b){
        if(hm.get(a) != null){
            hm.get(a).remove(b);
        }
    }

    public static void clearEdges(BinaryBlock.BinaryBuild b){
        hm.get(b).clear();
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
                for(BinaryBlock.BinaryBuild bb : hm.get(b)){
                    s.push(bb);
                }
            }
        }
    }

    public static void update(){
        for(BinaryBlock.BinaryBuild b : sources){
            dfs(b);
        }
    }

    public static void run(){
        //I will change this later
        while(true){
            update();
        }
    }

    public static void readGraph(StringMap map){
        hm.clear();
        sources.clear();
        readMap(JsonIO.json.fromJson(StringMap.class, String.class, map.get("graph")));
        readSet(JsonIO.json.fromJson(IntIntMap.class, String.class, map.get("source")), sources);
    }

    public static void readMap(StringMap map){
        for(StringMap.Entry<String, String> e : map.entries()){
            hm.put((BinaryBlock.BinaryBuild)Vars.world.build(Integer.valueOf(e.key)), new HashSet<>());
            readSet(JsonIO.json.fromJson(IntIntMap.class, String.class, e.value), hm.get((BinaryBlock.BinaryBuild)Vars.world.build(Integer.valueOf(e.key))));
        }
    }

    public static void readSet(IntIntMap map, HashSet<BinaryBlock.BinaryBuild> h){
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
    
    public static IntIntMap writeSet(HashSet<BinaryBlock.BinaryBuild> h){
        IntIntMap map = new IntIntMap();
        int i = 0;
        for(BinaryBlock.BinaryBuild b : h){
            map.put(i, b.pos());
            i++;
        }
        return map;
    }
}
