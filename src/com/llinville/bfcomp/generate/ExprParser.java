package com.llinville.bfcomp.generate;

import com.llinville.bfcomp.generate.util.NotationConverter;

public class ExprParser {

    public static void parseExprOnStack(Generator g, String inputString){
        inputString = makeSpaceDelimited(inputString);
        String inputPostfix = NotationConverter.infixToPostfix(inputString);
        String[] splitTokens = inputPostfix.split(" ");

        for(String token : splitTokens){
            if(token.matches("[0-9]+"))
        }

    }
}
