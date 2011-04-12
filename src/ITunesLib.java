import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class ITunesLib {

	public static void main(String[] args) throws Exception {
		File file = new File("itunesLib.xml");

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader r = inputFactory
				.createXMLEventReader(new FileInputStream(file));
		processITunesXML(r);
	}

	public static void main2(String[] args) throws Exception {

		File file = new File("itunesLib.xml");

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader r = inputFactory
				.createXMLEventReader(new FileInputStream(file));
		try {
			while (r.hasNext()) {
				XMLEvent event = r.nextEvent();
				if (event.isStartElement()) {
					StartElement start = event.asStartElement();
					boolean isExtension = false;
					boolean elementPrinted = false;
					// if (!ATOM_NS.equals(start.getName().getNamespaceURI())) {
					System.out.println("start:" + start.getName());
					isExtension = true;
					elementPrinted = true;
					// }

					for (Iterator i = start.getAttributes(); i.hasNext();) {
						Attribute attr = (Attribute) i.next();
						// String ns = attr.getName().getNamespaceURI();

						// if (ATOM_NS.equals(ns))
						// continue;
						//
						// if ("".equals(ns) && !isExtension)
						// continue;

						// if ("xml".equals(attr.getName().getPrefix()))
						// continue;
						//
						// if (!elementPrinted) {
						// elementPrinted = true;
						// System.out.println(start.getName());
						// }

						System.out.print("\t");
						System.out.println("attr:" + attr);
					}
				} else if (event.isEndElement()) {
					EndElement end = event.asEndElement();
					System.out.println("end:" + end.getName());
				} else if (event.isCharacters()) {
					Characters ce = event.asCharacters();
					if (!ce.isWhiteSpace()) {
						System.out.println("\tchar:" + ce.getData());
					}
				}
			}
		} finally {
			r.close();
		}

		// input.close();

	}

	private static void processITunesXML(XMLEventReader reader)
			throws Exception {
		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			// System.out.println(event.getEventType());
			if (event.isStartElement()) {
				StartElement se = event.asStartElement();
				if ("key".equals(se.getName().getLocalPart())
						&& "Tracks".equals(reader.getElementText()))
					processTracks(reader);
			}
		}

	}

	private static void processTracks(XMLEventReader reader) throws Exception {
		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();

			if (event.isEndElement()) {
				System.out.println("end:" + event.asEndElement().getName());
				// "Tracks".equals(event.asEndElement().getName().getLocalPart())){

				// System.out.println("found end tracks");
			} else if (event.isStartElement()) {
				System.out.println("start:" + event.asStartElement().getName());
			}
		}

	}

}
