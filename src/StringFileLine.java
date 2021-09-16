public class StringFileLine extends FileLine implements Comparable<StringFileLine> {
    //String value from one particular line
    private String value;

    public StringFileLine(int arrayNumber, String value) {
        super(arrayNumber);
        this.value = value;
    }

    @Override
    public int compareTo(StringFileLine o) {
        return value.compareTo(o.value);
    }

    public String getValue() {
        return value;
    }
}
