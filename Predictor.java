///////////////////////////////////////////////////////////////////////////////
// Title:              Predictor
// Files:              None
// Quarter:            CSE 8B Spring 2021
//
// Author:             Jonathan Tran
// Email:              jot002@ucsd.edu
// Instructor's Name:  Professor Allos
//
///////////////////////////////////////////////////////////////////////////////
import java.io.*;
import java.util.*;

/**
 * Creates a Predictor object that has the functionality of a rating predictor.
 * 
 * Bugs: None known
 *
 * @author   Jonathan Tran
 */
public class Predictor {
    private HashMap<String, int[]> wordFreqMap;
    private HashSet<String> stopWords;

    /**
     * public constructor that initialize member variables with empty 
     * collections.
     */
    public Predictor() {
        this.wordFreqMap = new HashMap<>();
        this.stopWords = new HashSet<>();
    }

    /**
     * Getter that returns wordFreqMap
     * @return the instance variable wordFreqMap
     */
    public HashMap<String, int[]> getWordFreqMap() {
        return this.wordFreqMap;
    }

    /**
     * Getter that returns stopWords
     * @return the instance variable stopWords
     */
    public HashSet<String> getStopWords() {
        return this.stopWords;
    }

    /**
     * Setter that sets wordFreqMap
     * @param map the new wordFreqMap to be assigned
     */
    public void setWordFreqMap(HashMap<String, int[]> map) {
        this.wordFreqMap = map;
    }

    /**
     * Setter that sets stopWords
     * @param stopWords the new stopWords to be assigned
     */
    public void setStopWords(HashSet<String> stopWords) {
        this.stopWords = stopWords;
    }

    /**
     * This method takes in a filename and read its contents to the HashSet 
     * instance variable this.stopwords.
     * 
     * @param inFile the filename to be read
     * @throws IOException
     */
    public void createStopWordsSet(String inFile) throws IOException{
        Scanner scnr = new Scanner(new File(inFile));

        // iterate over all lines and each line contain a single word
        while (scnr.hasNextLine()) {
            String data = scnr.nextLine();
            this.stopWords.add(data);
        }
        scnr.close();
    }

    /**
     * This method takes in a string of sentence and split the string to a list
     * of words by delimited by whitespces.
     *
     * @param sentence a String object of the sentence to split
     * @return a new list of words
     */
    public ArrayList<String> splitLine(String sentence) {
        ArrayList<String> list = new ArrayList<>();
        // Uses static method to add all elements of string[] to ArrayList
        Collections.addAll(list, sentence.split(" "));
        return list;
    }

    /**
     * This method takes in a list of words and for each word it trys to split
     * at hyphens.
     * 
     * @param words a list of words to split
     * @return a new list of words split at hyphens
     */
    public ArrayList<String> splitAtHyphens(ArrayList<String> words){
        ArrayList<String> list = new ArrayList<>();

        for (String word : words) {
            // split at hyphens for each word
            Collections.addAll(list, word.split("-"));
        }
        return list;
    } 

    /**
     * This method takes in a list of words and for each word it removes all 
     * punctuations. The new words are then added to a new list to return.
     * 
     * @param words a list of words to get punctuations removed
     * @return a new list of words with punctuations removed
     */
    public ArrayList<String> removePunctuation(ArrayList<String> words) {
        ArrayList<String> list = new ArrayList<>();

        for (String word : words) {
            // regular expression
            list.add(word.replaceAll("[^a-zA-Z0-9]", "")); 
        }
        return list;
    }

    /**
     * This method takes in a list of words and for each word it trims the white
     * spaces. The new words are then added to a new list to return.
     * 
     * @param words a list of words to get spaces trimmed
     * @return a new list of words with spaces trimmed
     */
    public ArrayList<String> trimWhiteSpaces(ArrayList<String> words){
        ArrayList<String> list = new ArrayList<>();
        
        for (String word : words) {
            // simply call its instance method for string object
            list.add(word.trim());
        }
        return list;
    }

    /**
     * This method takes in a list of words and removes all the empty and single
     * words. The new words are then added to a new list to return.
     * 
     * @param words a list of words to be filtered
     * @return a new list of words with empty and single words removed
     */
    public ArrayList<String> removeEmptySingleWords(ArrayList<String> words){
        ArrayList<String> list = new ArrayList<>();

        for (String word : words) {
            // add the word to the new list of its length is bigger than 1
            if (word.length() > 1) {
                list.add(word);
            }
        }
        return list;
    }

    /**
     * This method takes in a list of words and cast all words to lower case.
     * 
     * @param words a list of words to be casted
     * @return a new list of words with all lower cases
     */
    public ArrayList<String> toLowerCase(ArrayList<String> words){
        ArrayList<String> list = new ArrayList<>();

        for (String word : words) {
            // simply call its instance method for string object
            list.add(word.toLowerCase());
        }
        return list;
    }


    /**
     * This method takes in a list of words and remove all stop words
     * 
     * @param words a list of words to be casted
     * @return a new list of words with all words that are not stop words
     */
    public ArrayList<String> removeStopWords(ArrayList<String> words){
        ArrayList<String> list = new ArrayList<>();
        // iterate through each element in words
        for (String word : words) {
            // checks if word is not in stopWords
            if (!stopWords.contains(word)) {
                // add the word to a new list if it is not a stop word
                list.add(word);
            }
        }
        return list;
    }

    /**
     * This method takes in a cleaned data file and uses it to update HashMap
     * 
     * @param inCleanFile cleaned data file
     */
    public void updateHashMap(String inCleanFile) throws IOException {
        // magic number variable
        int sizeTwo = 2;
        // gets inCleanFile ready to be read
        Scanner scnr = new Scanner(new File(inCleanFile));
        // checks if there is another line to read
        while (scnr.hasNextLine()) {
            // splits the line into a list of elements (split by spaces)
            ArrayList<String> list = splitLine(scnr.nextLine());
            // gets the rating
            int rating = Integer.parseInt(list.remove(0));
            // iterates through every string in the list
            for (String word : list) {
                // checks if wordFreqMap does not have the string
                if (!this.wordFreqMap.containsKey(word)) {
                    // creates an int array of size 2
                    int[] value = new int[sizeTwo];
                    // makes the first element the rating
                    value[0] = rating;
                    // makes the second element equal to 1 since it is not 
                    // in the wordFreqMap 
                    value[1] = 1;
                    // puts the string, rating, and string count into wordFreqMap
                    this.wordFreqMap.put(word, value);
                }
                // if the word is in wordFreqMap
                else {
                    // gets the rating and string count
                    int[] value = wordFreqMap.get(word);
                    // updates the rating
                    value[0] += rating;
                    // increments the word count by 1
                    value[1] += 1;
                    // puts it back into wordFreqMap
                    this.wordFreqMap.put(word, value);
                }
            }
        }
        // closes scnr
        scnr.close();
    }
    

    /**
     * This method takes in a cleaned data file and predicts its ratings based 
     * on the HashMap. After predicting a rating for each review in the clean
     * file, it will write it to an output file
     * 
     * @param inCleanFile cleaned data file
     * @param outRatingsFile output file
     */
    public void rateReviews(String inCleanFile, String outRatingsFile) 
    throws IOException {
        // magic number variable
        int neutralRating = 2;
        // opens inCleanFile for reading
        Scanner scnr = new Scanner(new File(inCleanFile));
        // opens outRatingsFile to write the predicted rating
        FileWriter newFile = new FileWriter(new File(outRatingsFile), true);
        // keeps going as long as there is another line
        while (scnr.hasNextLine()) {
            // splits the review strings into a list (split by spaces)
            ArrayList<String> list = splitLine(scnr.nextLine());
            double total = 0;
            int numWords = 0;
            // iterates through each element in the list
            for (String word : list) {
                // checks if string is in wordFreqMap
                if (this.wordFreqMap.containsKey(word)) {
                    numWords += 1;
                    // gets the rating and string frequency from wordFreqMap
                    int[] value = this.wordFreqMap.get(word);
                    // rating is the first element
                    int rating = value[0];
                    // num 
                    int num = value[1];
                    // gets the averge rating per string frequency
                    double avg = ((double) rating / num);
                    // adds it to the total
                    total += avg;
                }
                // if string is not wordFreqMap
                else {
                    // adds 2 to the total since it is not in wordFreqMap
                    total += neutralRating;
                    // increments numWords by 1
                    numWords += 1;
                }
            }
            // if there are no words on the line, the rating will be 2
            if (numWords == 0) {
                newFile.write("2");
            }
            // if there are words on the line, the rating will be the total 
            // score divided by the number of words on the line
            else {
                // rounds the number to the nearest tenth
                String strDouble = String.format("%.1f", total / numWords);
                // writes the rating into the new file
                newFile.write(strDouble);
            }
            // goes to a newline
            newFile.write("\n");
            
        }
        // closes scnr and newFile
        scnr.close();
        newFile.close();
        // can look at HashMap
        // Set<String> keySet = this.getWordFreqMap().keySet();
        // for (String key : keySet) {
        //     System.out.println(key + ": " + Arrays.toString(this.getWordFreqMap().get(key)));
        }
        

    /**
     * This method takes in a filename called inFile, read it line by line, 
     * clean each line and write to a new file called outFile.
     * @param inFile the filename to read
     * @param outFile the filename to write 
     * @param ratingIncluded whether the inFile contains rating at front
     * @throws IOException
     */
    public void cleanData(String inFile, String outFile, boolean ratingIncluded) 
    throws IOException {

        // the following lines of code basically opens inFile to be read and 
        // opens outFile to be an ouput file where things will be written in it
        Scanner scnr = new Scanner(new File(inFile));
        FileWriter writer = new FileWriter(new File(outFile));

        // checks if there is another line in inFile
        while (scnr.hasNextLine()) {
            // sentence is assigned to a line in inFile
            String sentence = scnr.nextLine();
            // puts the strings in this line into a list (split by spaces)
            ArrayList<String> list = this.splitLine(sentence);

            // set rating to null and checks if the parameter ratingIncluded is
            // true, and if it is, then it will set rating to the first 
            // element in the list and remove the element from the list. If
            // it is false, rating will stay null
            String rating = null;
            if (ratingIncluded) {
                rating = list.remove(0);
            }

            // uses implemented methods to clean each line. 
            // splits the strings in list by if they have a hyphen
            list = this.splitAtHyphens(list);
            // removes punctutation from the strings in list
            list = this.removePunctuation(list);
            // removes all leading and trailing white spaces from the strings
            // in list
            list = this.trimWhiteSpaces(list);
            // remove all empty strings and single letter strings
            list = this.removeEmptySingleWords(list);
            // makes all strings to lower case
            list = this.toLowerCase(list);
            // removes all stop words in list
            list = this.removeStopWords(list);

            // set result equal to the strings in list separated by a space
            // in between each string
            String result = String.join(" ", list); 

            // if the parameter ratingIncluded is true, update result to
            // include the rating in front of it, separating the rating and
            // the original result by a space. If it is false, keep result
            // the same
            if (ratingIncluded) {
                result = rating + " " + result;
            }

            // writes the result on a line in outFile and skips to the next line
            writer.write(result + "\n");
        }

        // closes scnr and closes writer
        scnr.close();
        writer.close();
    }

    

}