package net.serveron.hane.athletics.config;

import org.bukkit.Location;

public class TeleportStructure {

    private final String name;
    private final Location targetLocation;
    private final Location loc1;
    private final Location loc2;
    private final int lowX;
    private final int highX;
    private final int lowY;
    private final int highY;
    private final int lowZ;
    private final int highZ;

    public TeleportStructure(String name, Location target, Location loc1, Location loc2){
        this.name = name;
        targetLocation = target;
        this.loc1 = loc1;
        this.loc2 = loc2;
        lowX = Math.min(loc1.getBlockX(),loc2.getBlockX());
        highX = Math.max(loc1.getBlockX(),loc2.getBlockX());
        lowY = Math.min(loc1.getBlockY(),loc2.getBlockY());
        highY = Math.max(loc1.getBlockY(),loc2.getBlockY());
        lowZ = Math.min(loc1.getBlockZ(),loc2.getBlockZ());
        highZ = Math.max(loc1.getBlockZ(),loc2.getBlockZ());
    }

    public String getName() {
        return name;
    }

    public boolean checkLocation(Location target){
        if(lowX <= target.getBlockX() && target.getBlockX() <= highX ){
            if(lowZ <= target.getBlockZ() && target.getBlockZ() <= highZ ){
                if(lowY <= target.getBlockY() && target.getBlockY() <= highY ){
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Location getTargetLocation(){
        return targetLocation;
    }

    public Location getLoc1() {
        return loc1;
    }

    public Location getLoc2() {
        return loc2;
    }

    public String getText(){
        return name+"("+targetLocation.getBlockX()+","+targetLocation.getBlockY()+","+targetLocation.getBlockZ()+")";
    }

}
