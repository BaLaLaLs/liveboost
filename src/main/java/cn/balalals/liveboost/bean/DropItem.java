package cn.balalals.liveboost.bean;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DropItem {
    private Material material;
    private int max;
    private int min;
    private float probability;
    private ItemStack itemStack;
    public DropItem(Material material, int max, int min, float probability) {
        this.material = material;
        this.max = max;
        this.min = min;
        this.probability = probability;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    @Override
    public String toString() {
        return "DropItem{" +
                "material=" + material +
                ", max=" + max +
                ", min=" + min +
                ", probability=" + probability +
                '}';
    }
}
