package dawn.utils.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author dawn
 */
public class CustomizeLocalDateTimeDeserializer extends LocalDateTimeDeserializer {

	public CustomizeLocalDateTimeDeserializer() {
		super(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	@Override
	public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		try {
			return deserialize(jsonParser);
		} catch (Exception e) {
			return super.deserialize(jsonParser, deserializationContext);
		}
	}

	private LocalDateTime deserialize(JsonParser jsonParser) throws IOException {
		String text = "temp";
		int year = 0, month = 0, day = 0, hour = 0, min = 0, sec = 0;
		while (StringUtils.isNotBlank(text)) {
			text = String.valueOf(jsonParser.getText());
			if (StringUtils.isBlank(text) || "null".equals(text)) {
				break;
			}
			switch (text) {
				case "year":
					jsonParser.nextToken();
					year = jsonParser.getIntValue();
					break;
				case "monthValue":
					jsonParser.nextToken();
					month = jsonParser.getIntValue();
					break;
				case "dayOfMonth":
					jsonParser.nextToken();
					day = jsonParser.getIntValue();
					break;
				case "hour":
					jsonParser.nextToken();
					hour = jsonParser.getIntValue();
					break;
				case "minute":
					jsonParser.nextToken();
					min = jsonParser.getIntValue();
					break;
				case "second":
					jsonParser.nextToken();
					sec = jsonParser.getIntValue();
					break;
				default:
					break;
			}
			jsonParser.nextToken();
		}
		return LocalDateTime.of(year, month, day, hour, min, sec);
	}
}
