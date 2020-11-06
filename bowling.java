import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;

//changed to read a .csv file containing the scores, assumes a correctly formatted scv
public class bowling{

    //reads a correctly formatted .csv and calculates the score for each line (game)
    private static ArrayList<Integer> readCSV(String filePath){
        try{
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            //each line is a complete game's worth of scores
            ArrayList<Integer> scores = new ArrayList<Integer>();
            while(scanner.hasNextLine()){
                scores.add(calculateScore(scanner.nextLine()));
            }
            scanner.close();
            return scores;
        } catch (FileNotFoundException e){
            System.err.println(e);
            return null;
        }
    }

    /*  Artifact of porting from C
        created because C.atoi() processes strings not individual chars
        converts char to int, also interprets strikes and spares as ints,
        special return case of -1 for spare.
    */
    private static int ctoi(int i, String str){
        int ret;

        if(str.charAt(i) == 'X'){
            ret = 10;
        }else if(str.charAt(i) == '/'){
            ret = -1;
        }else{
            ret = str.charAt(i) -'0'; //converts numeric char to int
        }

        return ret;
    }

    //calculates a bowling score given a correctly formatted array of bowling throws, read from csv
    private static int calculateScore(String scoreFrames){
        int frame = 0, throw1 = 0, throw2 = 0, total = 0;

        for(int i = 0; i < scoreFrames.length(); i++){
            switch(scoreFrames.charAt(i)){
                case ',': //delimeter for bowling rounds, the only change from the original input method
                    frame++;
                break;

                case 'X': //calculate strike
                    if(frame == 10)break; //skip 11th frame (0 indexed)

                    if(scoreFrames.charAt(i+2) == 'X'){ //first throw is another strike
                        throw1 = 10;

                        if(frame==9){
                            throw2 = ctoi(i+3,scoreFrames);
                        }else{
                            throw2 = ctoi(i+4,scoreFrames);
                        }
                    }else{
                        throw1 = scoreFrames.charAt(i+2) - '0';

                        throw2 = ctoi(i+3,scoreFrames);
                        if(throw2 == -1)throw2 = 10 - throw1; //if throw2 was a spare
                    }

                    total += 10 + throw1 + throw2;
                break;

                default: //calculate two numeric throws or spare
                    if(frame == 10)break;

                    if(scoreFrames.charAt(i+1) == '/'){
                        throw1 = ctoi(i+3,scoreFrames);
                        total += 10 + throw1;
                        i++;//consume '/' char
                    }else{
                        throw1 = ctoi(i++,scoreFrames);
                        throw2 = ctoi(i,scoreFrames);
                        total += throw1 + throw2;
                    }
                break;
            }
        }
        return total;
    }

    //run all tests to completion; assert will abort program upon a failed test
    private static void unitTests(){
        System.out.println("Running Numeric tests: ");
        ArrayList<Integer> numericExpected = new ArrayList<Integer>();
        numericExpected.add(0); numericExpected.add(10); numericExpected.add(90);
        ArrayList<Integer> numericActual = readCSV("./Tests/numeric_test.csv");
        assert numericActual.equals(numericExpected);
        System.out.println("PASSED\n");

        System.out.println("Running Spares tests: ");
        ArrayList<Integer> sparesExpected = new ArrayList<Integer>();
        sparesExpected.add(96); sparesExpected.add(105); sparesExpected.add(150);
        ArrayList<Integer> sparesActual = readCSV("./Tests/spares_test.csv");
        assert sparesActual.equals(sparesExpected);
        System.out.println("PASSED\n");

        System.out.println("Running Strikes tests: ");
        ArrayList<Integer> strikesExpected = new ArrayList<Integer>();
        strikesExpected.add(46); strikesExpected.add(100); strikesExpected.add(101); strikesExpected.add(300);
        ArrayList<Integer> strikesActual = readCSV("./Tests/strikes_test.csv");
        assert strikesActual.equals(sparesExpected);
        System.out.println("PASSED");
    }

    public static void main(String[] args){
        if(args.length == 0){ //no input defaults to included unit tests
            unitTests();
        }else if(args[0] != null){ //expects only 1 file as input, the file can have numerous games
            ArrayList<Integer> results = readCSV(args[0]);
            if(results != null){
                for(int score:results){
                    System.out.println(score);
                }
            }else{
                System.out.println("Unable to calculate score");
            }
        }
    }
}