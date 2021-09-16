public class IntegerFileLine extends FileLine implements Comparable<IntegerFileLine> {
    //integer value from one particular line
    private int value;

    public IntegerFileLine(int arrayNumber, int value) {
        super(arrayNumber);
        this.value = value;
    }

    @Override
    public int compareTo(IntegerFileLine o) {
        return Integer.compare(value, o.value);
    }

    public int getValue() {
        return value;
    }
}
