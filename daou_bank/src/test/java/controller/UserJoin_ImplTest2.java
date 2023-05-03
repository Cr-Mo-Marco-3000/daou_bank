package controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

class UserJoin_ImplTest2 {

	@Test
	void test() {
		Random rand = new Random();
		String FirstNum = Integer.toString(rand.nextInt(399 - 311 + 1) + 311);
		String SecondNum = Integer.toString(rand.nextInt(999 - 100 + 1) + 100);
		String ThirdNum = Integer.toString(rand.nextInt(999999 - 111111 + 1) + 111111);
		System.out.println(FirstNum + '-' + SecondNum + '-' + ThirdNum);
		assertTrue((FirstNum + SecondNum + ThirdNum).length() == 12);
	}


}
