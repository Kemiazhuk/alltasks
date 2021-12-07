import org.apache.http.HttpStatus;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/calc/*")
public class FilterSession implements Filter {
    public static final Integer LEFT_RANGE = -10000;
    public static final Integer RIGHT_RANGE = 10000;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String body = requestWrapper.getBody();
        String parameter = new StringBuilder(requestWrapper.getPathInfo()).delete(0, 1).toString();
        HttpSession session = requestWrapper.getSession();
        if (parameter.equals(CalculatorServlet.EXPRESSION) && !validFormatExpression(body)) {
            resp.setStatus(HttpStatus.SC_BAD_REQUEST);
            return;
        } else if (!parameter.equals(CalculatorServlet.EXPRESSION) &&
                containsVariable((String) session.getAttribute(CalculatorServlet.EXPRESSION), parameter)) {
            while (containsVariable((String) session.getAttribute(CalculatorServlet.EXPRESSION), parameter) &&
                    session.getAttribute(body) != null) {
                parameter = body;
                body = (String) session.getAttribute(parameter);
            }
            try {
                Integer value = Integer.valueOf(body);
                if (value > RIGHT_RANGE || value < LEFT_RANGE) {
                    resp.setStatus(HttpStatus.SC_FORBIDDEN);
                    return;
                }
            } catch (NumberFormatException ex) {
                resp.setStatus(HttpStatus.SC_BAD_REQUEST);
                ex.printStackTrace();
            }
        }

        filterChain.doFilter(requestWrapper, servletResponse);
    }

    private boolean validFormatExpression(String body) {
        char[] arrayOfSymbols = body.toCharArray();
        char previousSymbol = arrayOfSymbols[0];
        for (int i = 1; i < arrayOfSymbols.length; i++) {
            if (CalculatorServlet.SPACE == arrayOfSymbols[i]) {
                continue;
            }
            if ((previousSymbol >= CalculatorServlet.LETTER_A && previousSymbol <= CalculatorServlet.LETTER_Z)
                    && ((arrayOfSymbols[i] == CalculatorServlet.OPEN_BRACKET)
                    || (arrayOfSymbols[i] >= CalculatorServlet.LETTER_A && arrayOfSymbols[i] <= CalculatorServlet.LETTER_Z))) {
                return false;
            } else if (((previousSymbol == CalculatorServlet.PLUS || previousSymbol == CalculatorServlet.MINUS ||
                    previousSymbol == CalculatorServlet.MULTIPLE || previousSymbol == CalculatorServlet.DIVIDE)
                    && (arrayOfSymbols[i] == CalculatorServlet.CLOSE_BRACKET))) {
                return false;
            } else if (previousSymbol == CalculatorServlet.OPEN_BRACKET &&
                    (arrayOfSymbols[i] <= CalculatorServlet.LETTER_A && arrayOfSymbols[i] >= CalculatorServlet.LETTER_Z)) {
                return false;
            } else if (((previousSymbol == CalculatorServlet.CLOSE_BRACKET) ||
                    (previousSymbol >= CalculatorServlet.NUMBER_0 && previousSymbol <= CalculatorServlet.NUMBER_9)) &&
                    (!(arrayOfSymbols[i] == CalculatorServlet.PLUS) && !(arrayOfSymbols[i] == CalculatorServlet.MINUS) &&
                            !(arrayOfSymbols[i] == CalculatorServlet.MULTIPLE) && !(arrayOfSymbols[i] == CalculatorServlet.DIVIDE))) {
                return false;
            }
            previousSymbol = arrayOfSymbols[i];
        }
        return true;
    }

    private boolean containsVariable(String expression, CharSequence variable) {
        return expression.contains(variable);
    }

    @Override
    public void destroy() {

    }
}
