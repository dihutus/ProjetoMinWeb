package org.jaxen.jericho;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTagType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jaxen.DefaultNavigator;
import org.jaxen.JaxenConstants;
import org.jaxen.NamedAccessNavigator;
import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;
import org.jaxen.XPath;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.util.SingleObjectIterator;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class DocumentNavigator extends DefaultNavigator implements
		NamedAccessNavigator {

	private static final long serialVersionUID = 8640276699026512314L;

	private final Log log = LogFactory.getLog(getClass());

	private DocumentNavigator() {
	}

	private static class LazySingleton {
		public static DocumentNavigator INSTANCE = new DocumentNavigator();
	}

	public static Navigator getInstance() {
		return LazySingleton.INSTANCE;
	}

	@Override
	public boolean isAttribute(Object obj) {
		return obj instanceof Attribute;
	}

	@Override
	public boolean isComment(Object obj) {
		return (obj instanceof Element) ? isElementOfType((Element)obj,
				StartTagType.COMMENT) : false;
	}

	@Override
	public boolean isDocument(Object obj) {
		return (obj instanceof Source);
	}

	@Override
	public boolean isElement(Object obj) {
		return (obj instanceof Element) ? isElementOfType((Element)obj,
				StartTagType.NORMAL) : false;
	}

	@Override
	public boolean isNamespace(Object obj) {
		return false;
	}

	@Override
	public boolean isProcessingInstruction(Object obj) {
		if (obj instanceof Element) {
			Element element = (Element)obj;
			return isElementOfType(element,
					StartTagType.XML_PROCESSING_INSTRUCTION)
					|| isElementOfType(element, StartTagType.XML_DECLARATION);
		}
		return false;
	}

	@Override
	public boolean isText(Object obj) {
		if (obj instanceof CharacterReference || obj instanceof String) {
			return true;
		}
		return false;
	}

	@Override
	public String getAttributeName(Object obj) {
		if (obj instanceof Attribute) {
			Attribute attr = (Attribute)obj;
			return attr.getName();
		} else {
			return "";
		}
	}

	@Override
	public String getAttributeNamespaceUri(Object obj) {
		return "";
	}

	@Override
	public String getAttributeQName(Object obj) {
		return getAttributeName(obj);
	}

	@Override
	public String getAttributeStringValue(Object obj) {
		if (obj instanceof Attribute) {
			Attribute attr = (Attribute)obj;
			return attr.getValue();
		} else {
			return "";
		}
	}

	@Override
	public String getCommentStringValue(Object obj) {
		if (isComment(obj)) {
			Element element = (Element)obj;
			return element.getContent().getTextExtractor().toString();
		} else {
			return "";
		}
	}

	@Override
	public String getElementName(Object obj) {
		if (obj instanceof Element) {
			Element element = (Element)obj;
			return element.getName();
		} else {
			return "";
		}
	}

	@Override
	public String getElementNamespaceUri(Object obj) {
		return "";
	}

	@Override
	public String getElementQName(Object obj) {
		return getElementName(obj);
	}

	@Override
	public String getElementStringValue(Object obj) {
		if (obj instanceof Element) {
			Element element = (Element)obj;
			return element.getContent().getTextExtractor().toString();
		} else if (obj instanceof String) {
			return ((String)obj);
		} else {
			return String.valueOf(obj);
		}
	}

	@Override
	public String getNamespacePrefix(Object obj) {
		return "";
	}

	@Override
	public String getNamespaceStringValue(Object obj) {
		return getElementStringValue(obj);
	}

	@Override
	public String getTextStringValue(Object obj) {
		return getElementStringValue(obj);
	}

	@Override
	public Object getDocument(String url) {
		try {
			URLConnection conn = new URL(url).openConnection();
			Source source = new Source(conn.getInputStream());
			source.fullSequentialParse();
			return source;
		} catch (MalformedURLException e) {
			log.error("Malformed URL: " + url, e);
			return null;
		} catch (IOException e) {
			log.error("IO Exception for URL: " + url, e);
			return null;
		}
	}

	@Override
	public Object getDocumentNode(Object contextNode) {
		if (contextNode instanceof Source) {
			Source source = (Source)contextNode;
			return ((Segment)source).getAllElements("html").get(0);
		} else {
			return contextNode;
		}
	}

	@Override
	public Object getParentNode(Object node) {
		if (isElement(node)) {
			return ((Element)node).getParentElement();
		} else {
			return null;
		}
	}

	@Override
	public Object getElementById(final Object node, final String elementId) {
		if (isElement(node)) {
			return Iterables.find(((Element)node).getAllElements(),
					new Predicate<Element>() {
						@Override
						public boolean apply(Element elem) {
							return elementId.equals(elem
									.getAttributeValue("id"));
						}
					});
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Iterator<?> getChildAxisIterator(Object node) {
		if (isElement(node)) {
			Element elem = (Element)node;
			List<Object> children = Lists.newArrayList();
			children.addAll(elem.getChildElements());
			children.add(elem.getTextExtractor().toString());
			return children.iterator();
		} else {
			return JaxenConstants.EMPTY_ITERATOR;
		}
	}

	@Override
	public Iterator<?> getChildAxisIterator(Object contextNode,
			String localName, String namespacePrefix, String namespaceURI)
			throws UnsupportedAxisException {
		if (contextNode instanceof Element) {
			List<Element> children = ((Element)contextNode)
					.getAllElements(localName);
			return children.iterator();
		} else {
			return JaxenConstants.EMPTY_ITERATOR;
		}
	}

	@Override
	public Iterator<?> getNamespaceAxisIterator(Object contextNode) {
		return JaxenConstants.EMPTY_ITERATOR;
	}

	@Override
	public Iterator<?> getParentAxisIterator(Object contextNode) {
		if (isDocument(contextNode)) {
			return JaxenConstants.EMPTY_ITERATOR;
		}
		Element parent = null;
		if (isElement(contextNode)) {
			Element element = (Element)contextNode;
			parent = element.getParentElement();
		}
		if (parent == null) {
			return JaxenConstants.EMPTY_ITERATOR;
		} else {
			return new SingleObjectIterator(parent);
		}
	}

	@Override
	public Iterator<?> getAttributeAxisIterator(Object contextNode) {
		if (isElement(contextNode)) {
			Element element = (Element)contextNode;
			Attributes attrs = element.getAttributes();
			Iterator<?> ait = attrs.iterator();
			List<Object> attrlist = Lists.newArrayList();;
			while (ait.hasNext()) {
				attrlist.add(ait.next());
			}
			return attrlist.iterator();
		}
		return JaxenConstants.EMPTY_ITERATOR;
	}

	@Override
	public Iterator<?> getAttributeAxisIterator(Object contextNode,
			String localName, String namespacePrefix, String namespaceURI)
			throws UnsupportedAxisException {
		List<Object> namedAttrs = Lists.newArrayList();;
		if (contextNode instanceof Element) {
			Attributes attrs = ((Element)contextNode).getAttributes();
			Iterator<?> ait = attrs.iterator();
			while (ait.hasNext()) {
				Attribute attr = (Attribute)ait.next();
				if (localName.equals(attr.getName())) {
					namedAttrs.add(attr);
				}
			}
			return namedAttrs.iterator();
		} else {
			return JaxenConstants.EMPTY_ITERATOR;
		}
	}

	@Override
	public XPath parseXPath(String xpath) throws SAXPathException {
		return new JerichoXPath(xpath);
	}

	private boolean isElementOfType(Element element, StartTagType tagType) {
		if (element == null) {
			return false;
		}
		return element.getStartTag().getTagType().equals(tagType);
	}
}