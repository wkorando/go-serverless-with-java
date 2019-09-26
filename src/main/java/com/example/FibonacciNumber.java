package com.example;

import java.math.BigInteger;
import java.util.logging.Logger;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class FibonacciNumber {
    protected static final Logger logger = Logger.getLogger("basic");

	public static JsonObject main (JsonObject args) {
        JsonObject response = new JsonObject();
        JsonPrimitive numberArg = args.getAsJsonPrimitive("number");

        if (numberArg.isNumber()) {
            int n = numberArg.getAsInt();

            // Special case: for n = 0 F(0) is also 0
            if (n == 0) {
                response.addProperty("input", n);
                response.addProperty("output", 0);

                return response;
            }

            // Initialize counters to calculate n-th Fibonacci number
            BigInteger a = new BigInteger("0");
            BigInteger b = new BigInteger("1");
            BigInteger c;
            
            // Start interating & calculate the sum of the two previous numbers
            for (int i = 2; i <= n; i++) {
                c = a.add(b);
                a = b;
                b = c;
            }

            // Construc return JSON. This input parameter as well as the n-th and (n-1)-th Fibonacci number
            response.addProperty("input", n);
            response.addProperty("output1", a.toString());
            response.addProperty("output2", b.toString());
        } else {
            logger.warning("Oops....something went wrong...");

            throw new Error("Input argument is not a number...Please try again and give a number as input");
        }

        return response;
    }  
} 
 

