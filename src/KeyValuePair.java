import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;


public class KeyValuePair {
	String key;
	String type;
	String value;
	private static final Logger log = Logger.getLogger(KeyValuePair.class.getName());

	public KeyValuePair(String key, String type, String value) {
		super();
		this.key = key;
		this.type = type;
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

//FIXME: remove this method!
	private static void getKeyValueDeleteMe(XMLEventReader reader) throws XMLStreamException {
		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			log.debug("eventType:" + event.getEventType());
			if (event.isStartElement()){
				String eText = reader.getElementText();
				log.debug("eText:" + eText);
			}
		}

	}

	public static KeyValuePair getKeyValue(XMLEventReader reader, String key) throws XMLStreamException {
		log.trace("starting getKeyValue()");
		String type = "";
		String value = "";
		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			if (event.isStartElement()){
				type = event.asStartElement().getName().getLocalPart();
				log.trace("type:" + type);
				String eText = reader.getElementText();
				value = eText;
				break;
			}
		}

		log.trace("key:" + key + " type:" + type + " value:" + value);
		return new KeyValuePair(key, type, value);


	}
}
