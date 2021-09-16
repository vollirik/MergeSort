public abstract class FileLine {
    //The number of this line file
    private int fileNumber;

    public FileLine(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    public int getFileNumber() {
        return fileNumber;
    }
}
