import org.apache.http.HttpStatus;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Stack;

@WebServlet("/calc/*")
public class CalculatorServlet extends HttpServlet {
    public static final Character LETTER_A = 'a';
    public static final Character LETTER_Z = 'z';
    public static final Character SPACE = ' ';
    public static final Character OPEN_BRACKET = '(';
    public static final Character CLOSE_BRACKET = ')';
    public static final Character PLUS = '+';
    public static final Character MINUS = '-';
    public static final Character MULTIPLE = '*';
    public static final Character DIVIDE = '/';
    public static final String EXPRESSION = "expression";
    public static final String MSG_ERROR_DIVIDE_ZERO = "Cannot divide by zero";
    public static final Character NUMBER_0 = '0';
    public static final Character NUMBER_9 = '9';

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Integer result = calculateExpression((String) session.getAttribute(EXPRESSION), session);
        if (result != null) {
            resp.setStatus(HttpStatus.SC_OK);
            resp.getWriter().print(result);
        } else {
            resp.setStatus(HttpStatus.SC_CONFLICT);
        }
    }

    private Integer calculateExpression(String expression, HttpSession session) {
        Stack<Character> operations = new Stack<>();
        Stack<Integer> numbers = new Stack<>();
        char[] symbols = expression.toCharArray();
        for (Character symbol : symbols) {
            if (symbol == SPACE) {
                continue;
            }
            if (symbol >= NUMBER_0 && symbol <= NUMBER_9) {
                numbers.push(Integer.parseInt(symbol.toString()));
            } else if ((symbol >= LETTER_A && symbol <= LETTER_Z)) {
                String value = (String) session.getAttribute(String.valueOf(symbol));
                if (value == null) {
                    return null;
                }
                char charValue = value.charAt(0);
                while (charValue >= LETTER_A && charValue <= LETTER_Z) {
                    value = (String) session.getAttribute(String.valueOf(charValue));
                    charValue = value.charAt(0);
                }
                numbers.push(Integer.valueOf(value));
            } else if (symbol == OPEN_BRACKET) {
                operations.push(symbol);
            } else if (symbol == CLOSE_BRACKET) {
                while (operations.peek() != OPEN_BRACKET) {
                    numbers.push(doOperation(operations.pop(), numbers.pop(), numbers.pop()));
                }
                operations.pop();
            } else if (symbol == PLUS || symbol == MINUS || symbol == MULTIPLE || symbol == DIVIDE) {
                while (!operations.isEmpty() && hasPriority(symbol, operations.peek())) {
                    numbers.push(doOperation(operations.pop(), numbers.pop(), numbers.pop()));
                }
                operations.push(symbol);
            }
        }
        while (!operations.isEmpty()) {
            numbers.push(doOperation(operations.pop(), numbers.pop(), numbers.pop()));
        }
        return numbers.pop();
    }

    private Integer doOperation(char operation, Integer second, Integer first) {
        if (operation == DIVIDE && second == 0) {
            throw new UnsupportedOperationException(MSG_ERROR_DIVIDE_ZERO);
        }
        return ((operation == PLUS) ? first + second :
                (operation == MINUS) ? first - second :
                        (operation == MULTIPLE) ? first * second :
                                (operation == DIVIDE) ? first / second : 0);
    }

    private boolean hasPriority(char firstOperation, char secondOperation) {
        if (secondOperation == OPEN_BRACKET || secondOperation == CLOSE_BRACKET)
            return false;
        return (firstOperation != MULTIPLE && firstOperation != DIVIDE) ||
                (secondOperation != PLUS && secondOperation != MINUS);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String parameter = new StringBuilder(req.getPathInfo()).delete(0, 1).toString();
        String body = inputStreamToString(req.getInputStream());
        if (session.getAttribute(parameter) == null) {
            resp.setStatus(HttpStatus.SC_CREATED);
        } else if (session.getAttribute(parameter) != null) {
            resp.setStatus(HttpStatus.SC_OK);
        }
        session.setAttribute(parameter, body);
    }

    private String inputStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        StringBuilder body = new StringBuilder();
        while (scanner.hasNext()) {
            body.append(scanner.next());
        }
        return body.toString();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String parameter = new StringBuilder(req.getPathInfo()).delete(0, 1).toString();
        if (session.getAttribute(parameter) != null) {
            session.removeAttribute(parameter);
            resp.setStatus(HttpStatus.SC_NO_CONTENT);
        }
    }
}
