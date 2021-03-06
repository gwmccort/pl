import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

public class ITunesLib {

	static final String inFile = "data/itunesLib.xml";

	private static final Logger log = Logger.getLogger(ITunesLib.class.getName());

	private static void getDict(XMLEventReader reader) throws Exception {
		System.out.println("starting getDic() ...");
		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				System.out.println("end:" + ee.getName());
				if ("dict".equals(ee.getName().getLocalPart())) {
					System.out
							.println("***** found end dict tag, exiting getDict");
					return;
				}

				// "Tracks".equals(event.asEndElement().getName().getLocalPart())){

				// System.out.println("found end tracks");
			} else if (event.isStartElement()) {
				StartElement se = event.asStartElement();
				if ("key".equals(se.getName().getLocalPart())) {
					String kv = reader.getElementText();
					System.out
							.println("start:"
									+ event.asStartElement().getName()
									+ " value:" + kv);
				} else {
					System.out.println("start:"
							+ event.asStartElement().getName());
				}
			}

		}
	}

	private static void getKeyValue(XMLEventReader reader) throws XMLStreamException {
		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			log.debug("eventType:" + event.getEventType());
			if (event.isStartElement()){
				String eText = reader.getElementText();
				log.debug("eText:" + eText);
			}
		}

	}

	public static void main(String[] args) throws Exception {
		log.trace("starting ITunesLib.main()...");
		File file = new File(inFile);

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader r = inputFactory
				.createXMLEventReader(new FileInputStream(file));
		processITunesXML(r);
		log.info("finished ITunesLib.main()!");
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
					log.debug("start:" + start.getName());
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

	/**
	 * Process iTunes xml file
	 *
	 * @param reader
	 * @throws Exception
	 */
	private static void processITunesXML(XMLEventReader reader)
			throws Exception {
		log.trace("starting processITunesXML()...");
		try {
			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();
				// log.debug("event type:" + event.getEventType() + " isStart:"
				// + event.isStartElement());

				if (event.isStartElement()) {
					// log.debug("event is start name:" +
					// event.asStartElement().getName());
					StartElement se = event.asStartElement();
					if ("key".equals(se.getName().getLocalPart())) {
						String tag = reader.getElementText();
						if ("Tracks".equals(tag)) {
							processTracks(reader);
							log.debug("after processTracke()");
						} else if ("Playlists".equals(tag)) {
							processPlayLists(reader);
							log.debug("after playlist");
						}
					}
					// if ("key".equals(se.getName().getLocalPart())
					// && "Tracks".equals(reader.getElementText())){
					// processTracks(reader);
					// log.debug("after processingTracks call");
					// } else if ("key".equals(se.getName().getLocalPart())
					// && "Playlists".equals(reader.getElementText())){
					// System.out.println("found playlist!!!");
					// processPlayList(reader);
					// }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Process play list section.
	 * @param reader
	 * @throws XMLStreamException
	 */
	private static void processPlayListItem(XMLEventReader reader)
			throws XMLStreamException {
		log.trace("starting processPlayListItem ...");

		String name;
		int id;
		String pId;
		boolean allItems;

		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
//			log.debug("elementType:" + event.getEventType());
			if (event.isStartElement()){
				StartElement se = event.asStartElement();
				if ("key".equals(se.getName().getLocalPart())){
					String eText = reader.getElementText();
					if ("Playlist Items".equals(eText)){
						getPlayListItems(reader);
					} else {
						KeyValuePair kvp = KeyValuePair.getKeyValue(reader, eText);
						log.debug("key:" + kvp.getKey() + " type:" + kvp.getType() + " value:" + kvp.getValue());
					}
				} else {
					log.warn("found start" + se.getName());
				}
			}
			else if (event.isEndElement()){
				log.debug("end:" + event.asEndElement().getName());
				if ("dict".equals(event.asEndElement().getName().getLocalPart()))
					break;
			}

		}

	}

	private static void getPlayListItems(XMLEventReader reader) throws XMLStreamException {
		log.trace("starting getPlayListItems()...");
		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			if (event.isStartElement()){
				StartElement se = event.asStartElement();
				//log.trace("start:" + se.getName());
				if ("key".equals(se.getName().getLocalPart())){
					KeyValuePair kvp = KeyValuePair.getKeyValue(reader, reader.getElementText());
					log.debug("key:" + kvp.getKey() + " type:" + kvp.getType() + " value:" + kvp.getValue());
				}
			} else if (event.isEndElement()){
				EndElement ee = event.asEndElement();
				//log.trace("end:" + ee.getName());
				if("array".equals(ee.getName().getLocalPart()))
					return;
			}
		}

	}

	/**
	 * Process PlayLists section
	 *
	 * @param reader
	 * @throws XMLStreamException
	 */
	private static void processPlayLists(XMLEventReader reader)
			throws XMLStreamException {
		log.trace("starting processPlayList ...");
		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if ("array".equals(ee.getName().getLocalPart())) {
					log.debug("***** found end array element");
				}
			}
			if (event.isStartElement()) {
				StartElement se = event.asStartElement();
				if ("dict".equals(se.getName().getLocalPart())) {
					log.debug("$$$ found start dict");
					processPlayListItem(reader);
				} else {
					log.debug("start name:" + event.asStartElement().getName());
				}
			}
		}

	}

	/**
	 * Process Tracks section
	 *
	 * @param reader
	 * @throws Exception
	 */
	private static void processTracks(XMLEventReader reader) throws Exception {
		log.trace("starting processTracks() ...");

		// skip first dict element
		skipWhiteSpace(reader);
		XMLEvent event;
		event = reader.nextEvent();

		while (reader.hasNext()) {
			event = reader.nextEvent();

			if (event.isEndElement()) {
				EndElement ee = event.asEndElement();
				if ("dict".equals(ee.getName().getLocalPart())) {
					return;
				}
			} else if (event.isStartElement()) {
				StartElement se = event.asStartElement();
				if ("key".equals(se.getName().getLocalPart())) {
					String kv = reader.getElementText();
					log.debug("start:" + event.asStartElement().getName()
							+ " value:" + kv);
					// getDict(reader);
					Dict d = Dict.processDict(reader);
					//log.info("track dict:" + d);
					Track t = new Track(d);
					log.info("track:" + t);
				} else {
					log.debug("start:" + event.asStartElement().getName());
				}
			}
		}
	}

	/**
	 * Skip events till next non-white space event.
	 * @param reader
	 * @throws XMLStreamException
	 */
	private static void skipWhiteSpace(XMLEventReader reader)
			throws XMLStreamException {
		while (reader.hasNext()) {
			XMLEvent e = reader.nextEvent();
			if (e.isCharacters() && e.asCharacters().isWhiteSpace()) {
				continue;
			} else {
				break;
			}
		}
	}

}
