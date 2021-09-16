import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author Kirillov Daniil
 */

public class MergeSort {

    //Boolean indicating if files contain strings; is false if files contain integers
    private boolean hasStrings;

    //Boolean indicating if sorting is done in ascending order
    private boolean ascendingSort;

    //Name of output file
    private String outputFileName;

    //List of names of input files
    private List<String> inputFileNames;

    public MergeSort() {
        inputFileNames = new ArrayList<>();
    }


    /**
     * Constructs a new MergeSort object by parsing args array
     *
     * @throws IllegalArgumentException  if not all necessary parameters are passed
     * @throws IndexOutOfBoundsException if command line parameters are invalid
     */
    public static MergeSort setup(String[] args) {
        if (args.length < 3) throw new IllegalArgumentException("Cannot execute, invalid input parameters");
        MergeSort mergeSort = new MergeSort();
        int currentIndex = 0;
        if (args[currentIndex].equals("-a")) {
            mergeSort.setAscendingSort(true);
            currentIndex++;
        } else if (args[currentIndex].equals("-d")) {
            mergeSort.setAscendingSort(false);
            currentIndex++;
        } else {
            mergeSort.setAscendingSort(true);
        }
        if (args[currentIndex].equals("-i")) {
            mergeSort.setHasStrings(false);
        } else if (args[currentIndex].equals("-s")) {
            mergeSort.setHasStrings(true);
        } else {
            mergeSort.setHasStrings(true);
        }
        mergeSort.setOutputFileName(args[++currentIndex]);
        for (currentIndex++; currentIndex < args.length; currentIndex++) {
            mergeSort.addInputFileName(args[currentIndex]);
        }
        return mergeSort;
    }

    public static void main(String[] args) {
        try {
            MergeSort mergeSort = setup(args);
            mergeSort.sort();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Cannot execute, invalid input parameters");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sets up merge sort. Calls sortForIntegers or sortForStrings
     */
    public void sort() {
        List<Scanner> inputScannerList = new ArrayList<>();
        File outputFile = new File(outputFileName);

        //Creating scanners for every input file
        try (PrintWriter pw = new PrintWriter(outputFile)) {
            for (String fileName : inputFileNames
            ) {
                File inputFile = new File(fileName);
                try {
                    inputScannerList.add(new Scanner(inputFile));
                } catch (FileNotFoundException e) {
                    System.out.println("File " + fileName + " not found");
                }
            }
            if (hasStrings) sortForStrings(inputScannerList, pw);
            else sortForIntegers(inputScannerList, pw);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            for (Scanner scanner : inputScannerList
            ) {
                scanner.close();
            }
        }
    }

    /**
     * Merges all input files (for integer files)
     */
    private void sortForIntegers(List<Scanner> inputScannerList, PrintWriter pw) {
        Comparator<IntegerFileLine> comparator = ascendingSort ? Comparator.naturalOrder() : Comparator.reverseOrder();
        PriorityQueue<IntegerFileLine> heap = new PriorityQueue<>(inputScannerList.size(), comparator);
        //Initializing heap
        for (int arrayNumber = 0; arrayNumber < inputScannerList.size(); arrayNumber++) {
            Scanner in = inputScannerList.get(arrayNumber);
            if (in.hasNextLine())
                try {
                    heap.add(new IntegerFileLine(arrayNumber, Integer.parseInt(in.nextLine())));
                } catch (NumberFormatException e) {
                    //arrayNumber-- because we have to try to put number from this file again
                    arrayNumber--;
                }
        }
        //Basically merge sort algorithm
        IntegerFileLine prevIntegerFromFile = null;
        while (!heap.isEmpty()) {
            IntegerFileLine integerFromFile = heap.poll();
            Scanner in = inputScannerList.get(integerFromFile.getFileNumber());
            //Checking if files are sorted in right order
            if (prevIntegerFromFile == null || comparator.compare(integerFromFile, prevIntegerFromFile) >= 0) {
                prevIntegerFromFile = integerFromFile;
                pw.println(integerFromFile.getValue());
            }
            //Adding next number to heap
            while (in.hasNextLine()) {
                String nextLine = in.nextLine();
                //Ignore line if we cannot parse it as integer
                if(isInteger(nextLine)){
                    heap.add(new IntegerFileLine(integerFromFile.getFileNumber(), Integer.parseInt(nextLine)));
                    break;
                }
            }
        }
    }

    /**
     * Same algorithm for strings
     */
    private void sortForStrings(List<Scanner> inputScannerList, PrintWriter pw) {
        Comparator<StringFileLine> comparator = ascendingSort ? Comparator.naturalOrder() : Comparator.reverseOrder();
        PriorityQueue<StringFileLine> heap = new PriorityQueue<>(inputScannerList.size(), comparator);
        for (int arrayNumber = 0; arrayNumber < inputScannerList.size(); arrayNumber++) {
            Scanner in = inputScannerList.get(arrayNumber);
            if (in.hasNextLine())
                heap.add(new StringFileLine(arrayNumber, in.nextLine()));
        }

        StringFileLine prevStringFromFile = null;
        while (!heap.isEmpty()) {
            StringFileLine stringFromFile = heap.poll();
            Scanner in = inputScannerList.get(stringFromFile.getFileNumber());
            if (prevStringFromFile == null || comparator.compare(stringFromFile, prevStringFromFile) >= 0) {
                prevStringFromFile = stringFromFile;
                pw.println(stringFromFile.getValue());
            }
            if (in.hasNextLine())
                heap.add(new StringFileLine(stringFromFile.getFileNumber(), in.nextLine()));
        }
    }

    /**
     * Determines if a String is an Integer
     * @param s - checked string
     */
    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void setHasStrings(boolean hasStrings) {
        this.hasStrings = hasStrings;
    }

    public void setAscendingSort(boolean ascendingSort) {
        this.ascendingSort = ascendingSort;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void addInputFileName(String inputFileName) {
        inputFileNames.add(inputFileName);
    }
}

