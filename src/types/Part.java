package types;

public class Part {
    final int start, count;
    private final Topology type;

    public Part(int start, int count, Topology type) {
        this.start = start;
        this.count = count;
        this.type = type;
    }

    public int getStart() {
        return start;
    }

    public int getCount() {
        return count;
    }

    public Topology getType() {
        return type;
    }
}
