package org.scot;

import java.io.IOException;

public class FixWuiEx {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FIXService fixService = new FIXServiceImpl();
		fixService.init();
		
		System.out.println("press <enter> to quit");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
