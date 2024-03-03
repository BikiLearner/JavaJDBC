import java.sql.*;
import java.util.ArrayList;

public class QuizDao {

    private String url="jdbc:mysql://127.0.0.1:3306/BikiJDBC";
    String uname="root";
    String password="Biki12#23";
    private Connection con;
    public QuizDao(){
        setConnection();
    }

    private void setConnection() {
        try {
            con= DriverManager.getConnection(url,uname,password);
        }catch (SQLException e){
            System.out.println("SqlException : " + e);
        }

    }
    public ArrayList<Question> getQuestions(String difficulty){
        try {
            ArrayList<Question> questionArrayList=new ArrayList<>();
            String query;
            if(difficulty.isEmpty()){
               query="select * from questions";
            }else {
                query="SELECT * FROM questions WHERE difficulty_level = '"+difficulty+"'";
            }

            Statement statement= con.createStatement();
            ResultSet resultSet=statement.executeQuery(query);
            while (resultSet.next()){
                Question question=processResultSet(resultSet);
                questionArrayList.add(question);
            }
            return questionArrayList;
        }catch (SQLException e){
            System.out.println("SqlException : " + e);
        }
        return new ArrayList<>();
    }
    public boolean loginUser(String password){
        try {
            String query = "SELECT COUNT(*) FROM player WHERE password = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            System.out.println("SqlException : " + e);
        }
        return false;
    }
    public boolean isUserPresent(String uname, String email) {
        try {
            String query = "SELECT COUNT(*) FROM player WHERE username = ? OR email = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, uname);
            preparedStatement.setString(2, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("SqlException : " + e);
        }
        return false;
    }
    public boolean registerPlayer(String uname,String email,String password){
        try {
            String query = "INSERT INTO player (username, email, password, last_login) VALUES (?,?,?,CURRENT_TIMESTAMP)";
            PreparedStatement preparedStatement= con.prepareStatement(query);
            preparedStatement.setString(1,uname);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int count = preparedStatement.executeUpdate();
            return count > 0;
        }catch (SQLException e) {
            System.out.println("SqlException : " + e);
        }
        return false;
    }
    public static Question processResultSet(ResultSet resultSet) throws SQLException {
        int questionId = resultSet.getInt("question_id");
        String questionText = resultSet.getString("question_text");
        String optionA = resultSet.getString("option_a");
        String optionB = resultSet.getString("option_b");
        String optionC = resultSet.getString("option_c");
        String optionD = resultSet.getString("option_d");
        char correctOption = resultSet.getString("correct_option").charAt(0);
        String explanation = resultSet.getString("explanation");
        int categoryId = resultSet.getInt("category_id");
        String difficultyLevel = resultSet.getString("difficulty_level");

        return new Question(questionId, questionText, optionA, optionB, optionC, optionD, correctOption, explanation, categoryId, difficultyLevel);
    }

}
