/*
 *  Copyright 2025
 *  @author Tang Bowen
 *  @author Katsuhisa Maruyama
 */

package org.jtool.prmodel;

import java.util.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

public class PRModelDate {
    
    private LocalDateTime localTime;
    
	public PRModelDate(Date date) {
		if (date != null) {
			Instant instant = date.toInstant();
			localTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		}
	}
    
    public PRModelDate(int year, int month, int day, int hour, int minute) {
        localTime = LocalDateTime.of(year, month, day, hour, minute);
    }
    
    public PRModelDate(CharSequence text) {
        localTime = LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
    
    public PRModelDate(CharSequence text, DateTimeFormatter formatter) {
        localTime = LocalDateTime.parse(text, formatter);
    }
    
    public String toString(DateTimeFormatter formatter) {
    	if(localTime!=null) {
        return localTime.format(formatter);
    	}else {
    		return null;
    	}
    }
    
    public LocalDateTime getLocalTime() {
        return localTime;
    }
    
    public int compareTo(PRModelDate date) {
        return localTime.compareTo(date.getLocalTime());
    }
    
    public long until(PRModelDate date) {
        return localTime.until(date.localTime, ChronoUnit.MILLIS);
    }
    
    public long from(PRModelDate date) {
        return date.localTime.until(localTime, ChronoUnit.MILLIS);
    }
    
    @Override
    public String toString() {
        return toString(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
