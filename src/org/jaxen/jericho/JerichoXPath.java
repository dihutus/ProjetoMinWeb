package org.jaxen.jericho;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import org.jaxen.BaseXPath;
import org.jaxen.Context;
import org.jaxen.JaxenException;
import org.jaxen.util.SingletonList;

public class JerichoXPath extends BaseXPath {
	private static final long serialVersionUID = -3265671828144237615L;

	public JerichoXPath(String xpath) throws JaxenException {
		super(xpath, DocumentNavigator.getInstance());
	}

	@Override
	protected Context getContext(Object node) {
		if (node instanceof Context)
			return (Context) node;

		Context ctx = new Context(getContextSupport());
		if (node instanceof Source) {
			Element root = (Element)getNavigator().getDocumentNode((Source)node);
			ctx.setNodeSet(new SingletonList(root));
		} else if (node instanceof List) {
			ctx.setNodeSet((List<?>)node);
		} else {
			ctx.setNodeSet(new SingletonList(node));
		}
		return ctx;
	}
}