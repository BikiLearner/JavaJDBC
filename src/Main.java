import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

// Design pattern DAO -> Data access object
public class Main {
    private static QuizDao quizDao;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner=new Scanner(System.in);
        quizDao = new QuizDao();
        checkForUser();
        scanner.close();
    }

    private static void checkForUser() {
        scanner = new Scanner(System.in);
        System.out.print("Enter your name : ");
        String uname = scanner.nextLine();
        System.out.print("Enter your email : ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (quizDao.isUserPresent(uname, email)) {
            if(quizDao.loginUser(password)){
                startQuestion(uname);
                scanner.close();
            }else {
                System.out.println("Wrong Password");
                System.out.println("Enter every thing again");
                checkForUser();
                scanner.close();
            }
        } else {
            System.out.print("Confirm your password: ");
            String confirmPass = scanner.nextLine();
            if (confirmPass.equals(password)) {
                if (quizDao.registerPlayer(uname, email, password)) {
                    System.out.println("Player Registered");
                    scanner.close();
                    startQuestion(uname);
                } else {
                    System.out.println("Player is not registered");
                    scanner.close();
                }
            } else {
                System.out.println("Password does not match with confirm password");
                checkForUser();
                scanner.close();
            }

        }
    }
    private static String difficultyOptions(){
        // Display difficulty levels
        System.out.println("Difficulty Levels");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        System.out.println("4. Allow Everything");
        System.out.println("Enter your difficulty choice: ");

        // Read user input for difficulty choice
        int choice = scanner.nextInt();

        // Process user choice using switch statement
        return switch (choice) {
            case 1 -> "easy";
            case 2 -> "medium";
            case 3 -> "hard";
            default -> "";
        };
    }
    private static void startQuestion(String uname) {

        ArrayList<Question> questionArrayList=quizDao.getQuestions(difficultyOptions());
        ArrayList<Question> wrongAnswer = new ArrayList<>();
        AtomicInteger countCorrect= new AtomicInteger();

        int totalQuestion=questionArrayList.size();
        questionArrayList.forEach(question -> {
            System.out.println(question.getQuestionText());
            System.out.println("A. " + question.getOptionA());
            System.out.println("B. " +question.getOptionB());
            System.out.println("C. " +question.getOptionC());
            System.out.println("D. " +question.getOptionD());
            System.out.print("Enter Your option (Ex : (A,B,C,D)) : ");
            String answer=scanner.next();
            if(Character.toLowerCase(answer.charAt(0))==question.getCorrectOption()){
                countCorrect.getAndIncrement();
            }else {
                wrongAnswer.add(question);
            }
            System.out.println("Press enter for next question");
            scanner.nextLine();
            System.out.flush();
        });
        scoreBoard(wrongAnswer,countCorrect,uname,totalQuestion);
    }

    private static void scoreBoard(ArrayList<Question> wrongAnswer, AtomicInteger countCorrect, String uname, int totalQuestion) {
        System.out.println("Player Name : "+ uname);
        System.out.println("Player score : " + countCorrect + " / " + totalQuestion);
        System.out.println("Press 1 to see the wrong answers or press 0 : ");
        int choice= scanner.nextInt();
        if(choice==1){
            showWrongAnswerDetails(wrongAnswer);
        }else {
            System.out.println("You are a great player ");
            System.out.println("Press 1 to try again else press 0");
            if(scanner.nextInt()==1){
                startQuestion(uname);
            }else {
                System.exit(0);
            }
        }

    }
    private static String getCorrectAnswer(Question question){

        // Process user choice using switch statement
        return switch (question.getCorrectOption()) {
            case 'A' -> question.getOptionA();
            case 'B' -> question.getOptionB();
            case 'C' -> question.getOptionC();
            case 'D' -> question.getOptionD();
            default -> "";
        };
    }
    private static void showWrongAnswerDetails(ArrayList<Question> wrongAnswer) {
        wrongAnswer.forEach(wrongAns->{
            System.out.println(wrongAns.getQuestionText());
            System.out.println("Correct Answer is " + wrongAns.getCorrectOption() + " : "
                    +getCorrectAnswer(wrongAns));
            System.out.println("Explanation for the answer : ");
            System.out.println(wrongAns.getExplanation());
            System.out.println("Press enter for next one");
            scanner.nextLine();
        });
    }


}