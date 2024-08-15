package jprTool;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class CustomDateDeserializer implements JsonDeserializer<Date>{

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss aa", Locale.ROOT);

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            // 输出 JSON 字符串以进行调试
        	System.out.println("Rejester done: ");
            String dateStr = json.getAsString();
            System.out.println("Parsing date: " + dateStr);
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }
}
