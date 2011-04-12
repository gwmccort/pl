import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class Dict {

	HashMap<String, Object> m = new HashMap();

	public void add(String key, Object value) {
		m.put(key, value);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (Map.Entry<String, Object> entry : m.entrySet()) {
			sb.append(entry.getKey());
			sb.append(':');
			sb.append(entry.getValue());
			sb.append(", ");
		}
		sb.append(']');
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {

		// File file = new File("dict.xml");
		File file = new File("itunesLib.xml");
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader reader = inputFactory
				.createXMLEventReader(new FileInputStream(file));

		try {
			while (reader.hasNext()) {
				XMLEvent e = reader.nextEvent();
				if (e.isCharacters() && e.asCharacters().isWhiteSpace()) {
					continue;
				} else if (e.isStartElement()) {
					StartElement se = e.asStartElement();
					System.out.println("se:" + se.getName());
					if ("dict".equals(se.getName().getLocalPart())) {
						Dict d = processDict(reader);
						System.out.println("******************");
						System.out.println(d);
					}
				}
			}

		} finally {
			reader.close();
		}
	}

	public static Dict processDict(XMLEventReader reader) throws XMLStreamException {
		System.out.println("**** dict called");
		Dict results = new Dict();

		// skip dict
		// System.out.println("skiped:" + reader.nextEvent().getEventType());

		// reader.nextEvent();

		while (reader.hasNext()) {
			XMLEvent e = reader.nextEvent();

			if (e.isStartElement()
					&& "key".equals(e.asStartElement().getName().getLocalPart())) {

				// read key
				String key = reader.getElementText();
				//System.out.println("found key:" + key);
				// System.out.println("start:" + e.asStartElement().getName()
				// + ":" + key);

				// skip whitespace
				while (e.isCharacters() && e.asCharacters().isWhiteSpace()){
					//System.out.println("skiping whitespace...");
					e = reader.nextEvent();
				}

				// read value
				e = reader.nextEvent();


				// skip whitespace
				while (e.isCharacters() && e.asCharacters().isWhiteSpace()){
					//System.out.println("skiping whitespace...");
					e = reader.nextEvent();
				}

				if (e.isStartElement() && "dict".equals(e.asStartElement().getName().getLocalPart())){
					results.add(key, processDict(reader));
					//System.out.println("after processDict !!!!!!!!!!!!!!!!!!!");
				}
				else if (e.isStartElement() && "array".equals(e.asStartElement().getName().getLocalPart())){
					//System.out.println("array processing TBD:");
					while (reader.hasNext()) {
						e = reader.nextEvent();
					}
				}
				else if (e.isStartElement()){
					String type = e.asStartElement().getName().getLocalPart();
//					System.out.println("type:" + type);
					String valueString = reader.getElementText();
					// System.out.println("\ttype:" + type + ":" + valueString);
					if ("integer".equals(type))
						results.add(key, new Long(valueString));
					else if ("true".equals(type))
						results.add(key, new Boolean(true));
					else if ("false".equals(type))
						results.add(key, new Boolean(false));
					else if ("string".equals(type))
						results.add(key, valueString);
					else if ("dict".equals(type))
						results.add(key, processDict(reader));
				} else {
					System.out.println("not a start element!!!!!! :" + e.getEventType());
				}
			} else if (e.isStartElement()) {
				System.out.println("start element not type key:" + e.asStartElement().getName());
			} else if (e.isCharacters() && e.asCharacters().isWhiteSpace()) {
				//System.out.println("skiping white space");
				continue;
			} else if (e.isEndElement()) {
				System.out.println("found end tag:"
						+ e.asEndElement().getName().getLocalPart());
				return results;
			}
		}
		System.out.println("end of events");
		return results;
	}

}
