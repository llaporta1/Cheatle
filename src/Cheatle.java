import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;
import java.io.*;

public class Cheatle {
//use scanner to read in txt file and use .read() that will give you a string
//if ties- get hint with more stars
//unguessed = alphabet

        HashSet<String> hashDictionary;
        TreeSet<String> treeSolutions;
        HashMap<String, ArrayList<String>> hintMap;
        int length;
        TreeSet<Character> correctLetters;
        TreeSet<Character> misplacedLetters;
        TreeSet<String> allSolutions;
        TreeSet<Character> elimLetters;
        TreeSet<Character> notGuessed;
        TreeSet<Character> allGuessed;
        int guesses;
        TreeSet<Character> tree;
        String alphabet;

        //Reads the dictionaryFile and puts all the allowed guesses into a data structure,
        //and also reads the solutionFile and puts all the possible solutions into a data structure,
        //also adding all the possible solutions to the allowed guesses.
        //Throws a BadDictionaryException if not every word in the dictionary & solutions are of the same length
        public Cheatle(String dictionaryFile, String solutionFile) throws BadDictionaryException, FileNotFoundException {
            //binary search tree, use hashmap reads in a guess and stores a hashmap of all possible * ! ? frequencies- chooses the key with the longest arraylist contains go through buffer reader to store alphabet in a hashset (or string?)
            hintMap = new HashMap<String,ArrayList<String>>();
            hashDictionary = new HashSet();
            treeSolutions = new TreeSet();
            allSolutions = new TreeSet();
            File file = new File(dictionaryFile);
            Scanner scan = new Scanner(file);
            String str = scan.next();
            hashDictionary.add(str);
            length = str.length();
            while(scan.hasNext())
            {
                String str2 = scan.next();
                if (str2.length() == length)
                {
                    hashDictionary.add(str2);
                }
                else
                    throw new BadDictionaryException();
                }
            }
    File file2 = new File(solutionFile);
            Scanner scan2 = new Scanner(file2);
            while (scan2.hasNext())
            {
                String str3 = scan2.next();
                if (str3.length() == length)
                {
                    hashDictionary.add(str3);
                    allSolutions.add(str3);
                    treeSolutions.add(str3);

                }
                else
                {
                    throw new BadDictionaryException();
                }
            }
        }


        //Returns the length of the words in the list of words
        public int getWordLength() {
            return length;
        }

        private String getDictionary()
        {
            String str="";
            for(String s: hashDictionary)
            {
                str+= s;
            }
            return str;
        }
        private String getSolutions()
        {
            String str="";
            for(String s: treeSolutions)
            {
                str+=s;
            }
            return str;
        }

        //Returns the complete alphabet of chars that are used in any word in the solution list,
        //in order as a String
        public String getAlphabet() {
            tree = new TreeSet();
            String alphabet = "";
            if (treeSolutions != null)
            {
                for (String s: treeSolutions)
                {
                    for (int i=0; i<length; i++)
                    {
                        char character = s.charAt(i);
                        tree.add(character);
                    }
                }
                for (char character: tree)
                {
                    alphabet += character;
                }
            }
            return alphabet;
        }


        //Begins a game of Cheatle, initializing any private instance variables necessary for
        // a single game.
        public void beginGame() {
            guesses=0;
            correctLetters = new TreeSet();
            misplacedLetters = new TreeSet();
            elimLetters = new TreeSet();
            notGuessed = new TreeSet();
            treeSolutions = allSolutions;
            allGuessed = new TreeSet();
        }

        //Checks to see if the guess is in the dictionary of words.
        //Does NOT check to see whether the guess is one of the REMAINING words.
        public boolean isAllowable(String guess) { //binary search
            if (hashDictionary.contains(guess))
            {
                return true;
            }
            return false;

        }

        //Given a guess, returns a String of '*', '?', and '!'
        //that gives feedback about the corresponding letters in that guess:
        // * means that letter is not in the word
        // ? means that letter is in the word, but not in that place
        // ! means that letter is in that exact place in the word
        // Because this is CHEATLE, not Wordle, you are to return the feedback
        // that leaves the LARGEST possible number of words remaining!!!
        //makeGuess should also UPDATE the list of remaining words
        // and update the list of letters where we KNOW where they are,
        // the list of letters that are definitely in the word but we don't know where they are,
        // the list of letters that are not in the word,
        // and the list of letters that are still possibilities
        //find ! and ? then fill in left over spaces with **
        public String makeGuess(String guess) {
            hintMap = new HashMap<String,ArrayList<String>>();
            int max=0;
            int starCount=0;
            String hint="";
            String feedback = "";
            for (String s: treeSolutions)
            {
                ArrayList<Character> solution = new ArrayList<Character>();
                for (int i=0; i<s.length(); i++)
                {
                    solution.add(s.charAt(i));
                }
                hint = getHint(guess,solution);
                if (!hintMap.containsKey(hint))
                {
                    ArrayList<String> solutionsList = new ArrayList();
                    solutionsList.add(s);
                    hintMap.put(hint,solutionsList);
                    if (solutionsList.size()>max)
                    {
                        max = solutionsList.size();
                        feedback = hint;
                    }
                    else if (solutionsList.size()==max)
                    {
                        int sumHint=0;
                        int sumFeedback=0;
                        for(int i=0; i<hint.length(); i++)
                        {
                            if(hint.charAt(i)=='*')
                            {
                                sumHint++;
                            }
                        }
                        for (int i=0; i<feedback.length(); i++)
                        {
                            if(feedback.charAt(i)=='*')
                            {
                                sumFeedback++;
                            }
                        }
                        if (sumHint>sumFeedback)
                        {
                            max = solutionsList.size();
                            starCount = sumHint;
                            feedback = hint;
                        }

                    }

                }
                else
                {
                    ArrayList<String> solutionsList2 = hintMap.get(hint);
                    solutionsList2.add(s);
                    hintMap.replace(hint,solutionsList2);
                    if (solutionsList2.size()>max)
                    {
                        max = solutionsList2.size();
                        feedback = hint;

                    }
                    else if (solutionsList2.size()==max)
                    {
                        int sumHint=0;
                        int sumFeedback=0;
                        for(int i=0; i<hint.length(); i++)
                        {
                            if(hint.charAt(i)=='*')
                            {
                                sumHint++;
                            }
                        }
                        for (int i=0; i<feedback.length(); i++)
                        {
                            if(feedback.charAt(i)=='*')
                            {
                                sumFeedback++;
                            }
                        }
                        if (sumHint>sumFeedback)
                        {
                            max = solutionsList2.size();
                            starCount = sumHint;
                            feedback = hint;
                        }

                    }
                }
            }
            treeSolutions = new TreeSet();
            ArrayList<String> newSolutions = hintMap.get(feedback);
            for (int i=0; i<newSolutions.size(); i++)
            {
                treeSolutions.add(newSolutions.get(i));
            }
            String alphabet = getAlphabet();
            for (int i=0; i<alphabet.length(); i++)
            {
                notGuessed.add(alphabet.charAt(i));
            }
            guesses++;
            for (int i=0; i<feedback.length(); i++)
            {
                allGuessed.add(guess.charAt(i));
                if (feedback.charAt(i) == '!')
                {
                    correctLetters.add(guess.charAt(i));
                }
                else if (feedback.charAt(i) == '?')
                {
                    misplacedLetters.add(guess.charAt(i));
                }
                else if (feedback.charAt(i) == '*')
                {
                    elimLetters.add(guess.charAt(i));
                }
            }
            return feedback;
        }

        private static String getHint(String guess, String solution)
        {
            ArrayList<Character> solutionList = new ArrayList<Character>();
            for (int i=0; i<solution.length(); i++)
            {
                solutionList.add(solution.charAt(i));
            }
            return getHint(guess,solutionList);
        }
        private static String getHint(String guess, ArrayList<Character> solution)
        {
            char[] hint = new char[guess.length()];
            String s="";
            for (int i=0; i<guess.length(); i++)
            {

                if (guess.charAt(i) == solution.get(i))
                {
                    hint[i] = '!';
                    char c = 'x';
                    solution.set(i,c);
                }
            }
            for (int i=0; i<guess.length(); i++)
            {
                if (solution.contains(guess.charAt(i)) && hint[i] != '!')
                {
                    hint[i] = '?';
                    int index = solution.indexOf(guess.charAt(i));
                    char c = 'x';
                    solution.set(index,c);
                }
            }
            for (int i=0; i<hint.length; i++)
            {
                if (hint[i] == 0)
                {
                    hint[i] = '*';
                }
            }
            for (int i=0; i<guess.length(); i++)
            {
                s+=hint[i];
            }
            return s;
        }

        //Returns a String of all letters that have received a ! feedback
        public String correctPlaceLetters() {
            String s="";
            if (correctLetters != null)
            {
                for(char letter: correctLetters)
                {
                    s+=letter;
                }
                return s;
            }
            return "";
        }

        //Returns a String of all letters that have received a ? feedback
        public String wrongPlaceLetters() {
            String s="";
            if (correctLetters != null)
            {
                for (char letter: correctLetters)
                {
                    if(misplacedLetters != null && misplacedLetters.contains(letter))
                    {
                        misplacedLetters.remove(letter);
                    }
                }
            }
            if (misplacedLetters != null)
            {
                for(char letter: misplacedLetters)
                {
                    s+=letter;
                }
                return s;
            }
            return "";
        }

        //Returns a String of all letters that have received a * feedback
        public String eliminatedLetters() {
            String s="";
            if (elimLetters != null) {
                for(char letter: elimLetters)
                {
                    s+=letter;
                }
                return s;
            }
            return "";
        }

        //Returns a String of all unguessed letters (not in guess but in solution)
        public String unguessedLetters()
        {
            String s="";
            TreeSet<Character> unguessed = new TreeSet();
            if (notGuessed != null)
            {
                for(char letter: notGuessed)
                {
                    unguessed.add(letter);
                }
                for(char letter: allGuessed)
                {
                    if (unguessed.contains(letter))
                    {
                        unguessed.remove(letter);
                    }
                }
                for (char letter: unguessed)
                {
                    s+=letter;
                }
                return s;
            }
            return "";
        }


        //Returns true if the feedback string is the winning one,
        //i.e. if it is all !s
        public boolean isWinningFeedback(String feedback) {
            int sum=0;
            for (int i=0; i<feedback.length();i++)
            {
                if(feedback.charAt(i)=='!')
                {
                    sum++;
                }
            }
            return sum==feedback.length();
        }

        //Returns a String of all the remaining possible words, with one word per line ALPHABETICAL ORDER
        public String getWordsRemaining() {
            String wordsRemaining = "";
            if (wordsRemaining != null)
            {
                for (String s: treeSolutions)
                {
                    wordsRemaining+=s + "\n";
                }
                return wordsRemaining;
            }
            return "";
        }

        //Returns the number of possible words remaining
        public int getNumRemaining() {
            return treeSolutions.size();
        }

        //Returns the number of guesses made in this game
        public int numberOfGuessesMade() {
            return guesses;
        }

        //Ends the current game and starts a new game.
        public void restart() {
            beginGame();
        }

        public static void readText(String dictionaryFile) throws FileNotFoundException
        {
            File file = new File(dictionaryFile);
            Scanner scan = new Scanner(file);
            String str = scan.next();
            while(scan.hasNext())
            {
                String str2 = scan.next();
                System.out.println(getHint("abash",str2));
            }
        }

        public static void main (String [] args) throws BadDictionaryException, FileNotFoundException
        {
            Cheatle absurdle = new Cheatle("WordleDictionarycopy.txt","WordleSolutionListcopy.txt");
            absurdle.beginGame();
            absurdle.readText("Dictionarycopy.txt");
            System.out.println(getHint("abets","abele")); //ERROR
            //System.out.println(getHint("abets","abcee"));
            //System.out.println(getHint("abets","abler"));
		/*System.out.println(getHint("abash","aahed"));
		System.out.println(getHint("abash","aalii"));
		System.out.println(getHint("abash","aargh"));
		System.out.println(getHint("abash","aarti"));
		System.out.println(getHint("abash","abaca"));
		System.out.println(getHint("abash","abacs"));
		System.out.println(getHint("abash","abaft"));
		System.out.println(getHint("abash","aahed"));

		System.out.println(getHint("abash",""));
		System.out.println(getHint("abash",""));
		System.out.println(getHint("abash",""));
		System.out.println(getHint("abash",""));
		System.out.println(getHint("abash",""));
		System.out.println(getHint("abash",""));
		System.out.println(getHint("abash",""));
		System.out.println(getHint("abash",""));*/
            //System.out.println(getHint("there","slate"));
            //System.out.println(getHint("stitch","thinkd"));
            //System.out.println(getHint("sunny","fearn"));
            //System.out.println(getHint("train","juicy"));
            //System.out.println(getHint("their","fried"));


		/*System.out.println(absurdle.makeGuess("round"));
		System.out.println("guesses: " + absurdle.numberOfGuessesMade());
		System.out.println("words remaining: " + absurdle.getNumRemaining());
		System.out.println("unguessed letters: " + absurdle.unguessedLetters());
		System.out.println("elim letters: " + absurdle.eliminatedLetters());
		System.out.println("wrong place: " + absurdle.wrongPlaceLetters());
		System.out.println("correct: " + absurdle.correctPlaceLetters());
		System.out.println(absurdle.makeGuess("slate"));
		System.out.println("guesses: " + absurdle.numberOfGuessesMade());
		System.out.println("words remaining: " + absurdle.getNumRemaining());
		System.out.println("unguessed letters: " + absurdle.unguessedLetters());
		System.out.println("elim letters: " + absurdle.eliminatedLetters());
		System.out.println("wrong place: " + absurdle.wrongPlaceLetters());
		System.out.println("correct: " + absurdle.correctPlaceLetters());
		System.out.println(absurdle.makeGuess("juicy"));
		System.out.println("guesses: " + absurdle.numberOfGuessesMade());
		System.out.println("words remaining: " + absurdle.getNumRemaining());
		System.out.println("unguessed letters: " + absurdle.unguessedLetters());
		System.out.println("elim letters: " + absurdle.eliminatedLetters());
		System.out.println("wrong place: " + absurdle.wrongPlaceLetters());
		System.out.println("correct: " + absurdle.correctPlaceLetters());
		absurdle.restart();
		System.out.println(absurdle.makeGuess("round"));
		System.out.println("guesses: " + absurdle.numberOfGuessesMade());
		System.out.println("words remaining: " + absurdle.getNumRemaining());
		System.out.println("unguessed letters: " + absurdle.unguessedLetters());
		System.out.println("elim letters: " + absurdle.eliminatedLetters());
		System.out.println("wrong place: " + absurdle.wrongPlaceLetters());
		System.out.println("correct: " + absurdle.correctPlaceLetters());
		System.out.println(absurdle.makeGuess("slate"));
		System.out.println("guesses: " + absurdle.numberOfGuessesMade());
		System.out.println("words remaining: " + absurdle.getNumRemaining());
		System.out.println("unguessed letters: " + absurdle.unguessedLetters());
		System.out.println("elim letters: " + absurdle.eliminatedLetters());
		System.out.println("wrong place: " + absurdle.wrongPlaceLetters());
		System.out.println("correct: " + absurdle.correctPlaceLetters());
		System.out.println(absurdle.makeGuess("juicy"));
		System.out.println("guesses: " + absurdle.numberOfGuessesMade());
		System.out.println("words remaining: " + absurdle.getNumRemaining());
		System.out.println("unguessed letters: " + absurdle.unguessedLetters());
		System.out.println("elim letters: " + absurdle.eliminatedLetters());
		System.out.println("wrong place: " + absurdle.wrongPlaceLetters());
		System.out.println("correct: " + absurdle.correctPlaceLetters());
		/*System.out.println(absurdle.getDictionary());
		System.out.println(absurdle.getSolutions());
		absurdle.beginGame();
		System.out.println(absurdle.getWordLength());
		System.out.println(absurdle.makeGuess("board"));
		System.out.println(absurdle.numberOfGuessesMade());
		System.out.println(absurdle.getWordsRemaining());
		System.out.println(absurdle.unguessedLetters());
		System.out.println(absurdle.eliminatedLetters());
		System.out.println(absurdle.wrongPlaceLetters());
		System.out.println(absurdle.correctPlaceLetters());
		System.out.println(absurdle.makeGuess("brink"));
		System.out.println(absurdle.numberOfGuessesMade());
		System.out.println(absurdle.getWordsRemaining());*/

        }
    }
