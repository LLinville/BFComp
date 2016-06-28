package com.llinville.bfcomp.generate;

import com.llinville.bfcomp.generate.util.NotationConverter;

public class ExprParser {
    private static String makeSpaceDelimited(String input){
        String withoutSpaces = input.replaceAll(" ", "");
        String[] tokens = withoutSpaces.split("(?=\\+)|(?<=\\+)|(?=\\-)|(?<=\\-)|(?=\\*)|(?<=\\*)|(?=\\\\)|(?<=\\\\)|(?=\\()|(?<=\\()|(?=\\))|(?<=\\))");
        return String.join(" ", tokens);
    }

    public static void parseExprOnStack(Generator g, String inputString){
        inputString = makeSpaceDelimited(inputString);
        String inputPostfix = NotationConverter.infixToPostfix(inputString);
        String[] splitTokens = inputPostfix.split(" ");

        for(String token : splitTokens){
            if(token.matches("[0-9]+")) {
                g.pushToStack(Integer.parseInt(token));
            } else if(token.matches("[a-zA-Z][a-zA-Z0-9]*")){
                g.pushVariableOntoStack(token);
            } else if(token.equals("+")){
                g.add();
            } else if(token.equals("-")){
                g.sub();
            } else if(token.equals("*")){
                g.mult();
            } else if(token.equals("/")){
                g.divmod();
                g.popStack();
            } else {
                System.err.println("Unrecognized token in expression: " + token);
            }
        }
    }
}
