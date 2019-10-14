package com.botongglcontroller.view;

import com.botongglcontroller.interfaces.Formatter;

import java.text.DecimalFormat;

/**
 * Created by prem on 10/28/14.
 */
public class DecimalFormatter implements Formatter {

	private final DecimalFormat format = new DecimalFormat("#.00");

	@Override
	public String format(String prefix, String suffix, float value) {
		return prefix + format.format(value) + suffix;
	}
}
