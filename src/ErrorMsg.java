/**
 * Created by Shine Kaye on 2017/3/26.
 * A class used to get Error Message
 */

public class ErrorMsg {

    public static final int MATH_ERROR = 0;

    public static final int VAR_NOT_EXIST = 1;

    public static final int SYNTAX_ERROR = 2;

    public static final int INVALID_OPERATOR = 3;

    public static final int LEFT_BRACKET = 4;

    public static final int RIGHT_BRACKET = 5;

    public static final int KEY_WORD_NOT_FOUND = 6;

    public static final int SEMICOLON = 7;

    public static final int INVALID_VAR = 8;

    public static final int INVALID_EXP = 9;

    private static String getType(int flag)
    {
        switch (flag)
        {
            case MATH_ERROR:
                return "Math Error!";

            case VAR_NOT_EXIST:
                return "Variable Not Found!";

            case SYNTAX_ERROR:
                return "Syntax Error!";

            case INVALID_OPERATOR:
                return "Invalid Operator!";
        }
        return null;
    }

    private static String getSynErrDetail(int flag)
    {
        String detail = "";
        switch (flag)
        {
            case LEFT_BRACKET:
                detail = " \'(\' Missing!";
                break;

            case RIGHT_BRACKET:
                detail = " \')\' Missing!";
                break;

            case SEMICOLON:
                detail = " \';\' Missing!";
                break;

            case KEY_WORD_NOT_FOUND:
                detail = "Undefined identifier!";
                break;

            case INVALID_VAR:
                detail = "Invalid Variable Defined!";
                break;

            case INVALID_EXP:
                detail = "Invalid Expression!";
                break;

            default:
                //do nothing
                break;
        }
        return detail;
    }

    public static void getErrMsg(int line, int position, int type, int detail){
        System.err.println("Error" + "(" + "line:" + line + ",position:" + position + "):"
                + getType(type) + getSynErrDetail(detail));
        System.exit(0);
    }

    public static void getErrMsg(int line, int position, int type){
        System.err.println("Error" + "(" + "line:" + line + ",position:" + position + "):"
                + getType(type));
        System.exit(0);
    }

    public static void getErrMsg(int type){
        System.err.println(getType(type) + " Divided by Zero!");
        System.exit(0);
    }
}
