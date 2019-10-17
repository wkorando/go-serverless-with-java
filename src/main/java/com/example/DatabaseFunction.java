package com.example;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;

public class DatabaseFunction {
	
	public static void main(String[] args) {
		CloudantClient client = ClientBuilder.account(args[0])
                .username(args[1])
                .password(args[2])
                .build();
		
		
	}

}
