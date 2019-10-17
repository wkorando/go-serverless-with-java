package com.example;

import java.math.BigDecimal;
import java.util.logging.Logger;
import com.google.gson.JsonObject;

public class CalculateRatioWeb {
    protected static final Logger logger = Logger.getLogger("basic");

	public static JsonObject main (JsonObject args) {
        // Some initialization
        JsonObject response = new JsonObject();      
        int n = args.getAsJsonPrimitive("input").getAsInt();;           
        BigDecimal arg1 = new BigDecimal(1);
        BigDecimal arg2 = new BigDecimal(1);
        BigDecimal ratio = new BigDecimal(1);
        String relOperator = "=";

        try {
            // Obtain the two numbers from the input
            if (n == 0) {
                throw new ArithmeticException("Cannot calculate the golden ratio of just one Fibonnaci number. Need at least two!!");
            } else {
                arg1 = args.getAsJsonPrimitive("output1").getAsBigDecimal();
                arg2 = args.getAsJsonPrimitive("output2").getAsBigDecimal();            
    
                ratio = arg2.divide(arg1);    
            }
        } catch (ArithmeticException e) {
            if (n >= 2) { // ratio can be calculated
                ratio = arg2.divide(arg1, 15, BigDecimal.ROUND_CEILING);
                relOperator = "\\approx";    
            } else { // Handle special case n=0 or n=1
                logger.warning(e.getMessage());
                response.addProperty("body",                
                    "<html>" +
                    "<script src=\"https://polyfill.io/v3/polyfill.min.js?features=es6\"></script>" +
                    "<script id=\"MathJax-script\" async src=\"https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js\"></script>" +    
                    "<body><h1>Calculate the Golden Ratio</h1>" + 
                    "Please \\((n \\ge 2)\\) to prevent dividing by zero..." +                    
                    "</body></html>"
                );
            }
        }
        
        // There is no "body" property yet (n>=2), so we're adding one that shows the calculate golden ratio (if n is big enough :-))
        if (!response.has("body")) {
            response.addProperty("body",
                "<html>" +
                "<script src=\"https://polyfill.io/v3/polyfill.min.js?features=es6\"></script>" +
                "<script id=\"MathJax-script\" async src=\"https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js\"></script>" +
                "<body><h1>Calculate the Golden Ratio</h1>" + 
                "The golden ratio is the limit of the ratios of successive terms of the Fibonacci sequence (or any Fibonacci-like sequence)" +
                "$$ \\lim_{n \\to \\infty } {F_{n+1} \\over F_{n}} = \\varphi $$" +
                "In other words, if a Fibonacci number is divided by its immediate predecessor in the sequence, the quotient approximates \\(\\varphi\\)." +
                "<p>source: <a href=\"https://en.wikipedia.org/wiki/Golden_ratio\">https://en.wikipedia.org/wiki/Golden_ratio</a></p>" +
                "<br><br><h3>Input parameters</h3>" +
                "<p>Now, let's calculate the ratio of the two Fibonacci numbers that were passed as input parameters. The following input has been received:</p>" + 
                "<pre>{<br>" +
                "   \"input\": " + n + ",<br>" +
                "   \"output1\": " + arg1 + ",<br>" + 
                "   \"output2\": " + arg2 + "<br>" +   
                "}</pre>" + 
                "<p>For \\(n = " + n + "\\), the \\(n^{th}\\) Fibonacci number is \\(F_{" + n + "} =  " + arg2 + "\\) and its immediate predecessor \\(F_{" + (n - 1) + "} = " + arg1 + "\\)." +  
                "&nbsp;The ratio between these two numbers is:</p>" +
                "$$ \\frac{F_{n+1}}{F_{n}} = \\frac{" + arg2 + "}{" + arg1 + "} " + relOperator + " " + ratio + "$$" +
                "</body></html>"
            );    
        }

        return response;
    }  
} 
 

