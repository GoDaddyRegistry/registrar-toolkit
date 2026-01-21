package godaddy.registry.jtoolkit2.se;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPathExpressionException;

import godaddy.registry.jtoolkit2.xml.XMLDocument;

public abstract class AbstractDomainVariantResponseExtension extends ResponseExtension {

    private static final long serialVersionUID = -2441248857298156911L;

    private List<IdnDomainVariant> variants;
    private String responseType;
    private boolean initialised;

    protected AbstractDomainVariantResponseExtension(String responseType) {
        this.responseType = responseType;
    }

    protected abstract String getVariantPrefix();

    /**
     * @see godaddy.registry.jtoolkit2.se.ResponseExtension#fromXML(XMLDocument)
     */
    @Override
    public void fromXML(final XMLDocument xmlDoc) throws XPathExpressionException {
        variants = null;

        String variantPrefix = getVariantPrefix();
        String variantListExpr = EXTENSION_EXPR + "/" + variantPrefix + ":RESPONSE_TYPE/" + variantPrefix + ":variant";
        String variantCountExpr = "count(" + variantListExpr + ")";
        String variantIndExpr = variantListExpr + "[IDX]";

        final int variantCount = xmlDoc.getNodeCount(replaceResponseType(variantCountExpr, responseType));
        final String indexExpression = replaceResponseType(variantIndExpr, responseType);

        variants = new ArrayList<>();

        for (int i = 0; i < variantCount; i++) {
            final String query = ReceiveSE.replaceIndex(indexExpression, i + 1);
            final String domainName = xmlDoc.getNodeValue(query);
            variants.add(new IdnDomainVariant(domainName));
        }

        initialised = (variantCount > 0);
    }

    public List<IdnDomainVariant> getVariants() {
        return variants;
    }

    @Override
    public boolean isInitialised() {
        return initialised;
    }
}
