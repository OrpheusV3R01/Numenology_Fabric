package numenology.energy;

public class NumenChunkData {

    private int energy;
    private int maxEnergy;
    private int miasma;
    private int stability;
    private boolean hasLumenTree;

    public NumenChunkData() {
        this.energy = 500;
        this.maxEnergy = 1000;
        this.miasma = 1;
        this.stability = 100;
        this.hasLumenTree = false;
    }

    // ==============================
    // ⚡ ЭНЕРГИЯ
    // ==============================
    public int getEnergy() {
        return energy;
    }

    public void addEnergy(int amount) {
        energy += amount;
        if (energy > maxEnergy) energy = maxEnergy;
        if (energy < 0) energy = 0;
    }

    public void removeEnergy(int amount) {
        energy -= amount;
        if (energy < 0) energy = 0;
    }

    public void setEnergy(int energy) {
        this.energy = Math.max(0, Math.min(maxEnergy, energy));
    }

    // ==============================
    // 💥 MAX ENERGY
    // ==============================
    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(int value) {
        this.maxEnergy = Math.max(100, Math.min(5000, value));
    }

    public void addMaxEnergy(int amount) {
        setMaxEnergy(this.maxEnergy + amount);
    }

    // ==============================
    // ☠ МИАЗМА
    // ==============================
    public int getMiasma() {
        return miasma;
    }

    public void addMiasma(int amount) {
        setMiasma(this.miasma + amount);
    }

    public void setMiasma(int miasma) {
        this.miasma = Math.max(1, Math.min(100, miasma));
        this.stability = 101 - this.miasma;
    }

    // ==============================
    // 🌀 СТАБИЛЬНОСТЬ
    // ==============================
    public int getStability() {
        return stability;
    }

    public void addStability(int amount) {
        setStability(this.stability + amount);
    }

    public void setStability(int stability) {
        this.stability = Math.max(1, Math.min(100, stability));
    }

    // ==============================
    // 🌳 LUMEN TREE
    // ==============================
    public boolean hasLumenTree() {
        return hasLumenTree;
    }

    public void setLumenTree(boolean hasLumenTree) {
        this.hasLumenTree = hasLumenTree;
    }
}