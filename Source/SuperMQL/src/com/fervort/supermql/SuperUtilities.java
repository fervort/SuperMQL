package com.fervort.supermql;

public class SuperUtilities {

	public static String replaceMultipleSpaceToSingle(String string)
	{
		return string.trim().replaceAll(" +", " ");
	}
}
