package godaddy.registry.jtoolkit2.se.mzb;

import java.util.GregorianCalendar;
import javax.xml.xpath.XPathExpressionException;

import godaddy.registry.jtoolkit2.EPPDateFormatter;
import godaddy.registry.jtoolkit2.se.ExtendedObjectType;
import godaddy.registry.jtoolkit2.se.TransferResponse;
import godaddy.registry.jtoolkit2.xml.XMLDocument;

/**
 * Use this to access MZB object transfer information as provided in an EPP
 * MZB transfer response compliant with RFC5730 and RFC5731.  Such a service
 * element is sent by a compliant EPP server in response to a valid MZB
 * transfer command, implemented by a subclass of the MzbTransferCommand
 * class.
 *
 * @see MzbTransferCommand
 * @see MzbTransferRequestCommand
 * @see MzbTransferApproveCommand
 * @see MzbTransferCancelCommand
 * @see MzbTransferRejectCommand
 * @see MzbTransferQueryCommand
 */
public class MzbTransferResponse extends TransferResponse {
    private static final long serialVersionUID = -3652461689615678734L;

    private static final String DOM_NAME_EXPR = RES_DATA_EXPR + "/mzb:trnData/mzb:roid/text()";
    private static final String DOM_EXDATE_EXPR = RES_DATA_EXPR + "/mzb:trnData/mzb:exDate/text()";

    private static final String DOM_TR_STATUS_EXPR = exprReplace(TR_STATUS_EXPR);
    private static final String DOM_REID_EXPR = exprReplace(REID_EXPR);
    private static final String DOM_REDATE_EXPR = exprReplace(REDATE_EXPR);
    private static final String DOM_ACID_EXPR = exprReplace(ACID_EXPR);
    private static final String DOM_ACDATE_EXPR = exprReplace(ACDATE_EXPR);

    private GregorianCalendar exDate;
    private String name;
    private String exDateStr;

    public MzbTransferResponse() {
        super(ExtendedObjectType.MZB);
    }

    protected static String exprReplace(String expr) {
        return expr.replaceAll(OBJ,
                ExtendedObjectType.MZB.getName());
    }

    public String getName() {
        return name;
    }

    public GregorianCalendar getExpiryDate() {
        return exDate;
    }

    @Override
    public void fromXML(XMLDocument xmlDoc) {
        super.fromXML(xmlDoc);

        if (!resultArray[0].succeeded()) {
            return;
        }

        try {
            name = xmlDoc.getNodeValue(DOM_NAME_EXPR);
            exDateStr = xmlDoc.getNodeValue(DOM_EXDATE_EXPR);
            if (exDateStr != null && exDateStr.length() > 0) {
                exDate = EPPDateFormatter.fromXSDateTime(exDateStr);
            }
        } catch (XPathExpressionException xpee) {
            maintLogger.warning(xpee.getMessage());
        }
    }

    @Override
    public String toString() {
        String retval = super.toString();

        if (exDate != null) {
            retval += "(exDate = " + exDateStr + ")";
        }

        return retval;
    }

    protected String trStatusExpr() {
        return DOM_TR_STATUS_EXPR;
    }

    protected String reIDExpr() {
        return DOM_REID_EXPR;
    }

    protected String reDateExpr() {
        return DOM_REDATE_EXPR;
    }

    protected String acIDExpr() {
        return DOM_ACID_EXPR;
    }

    protected String acDateExpr() {
        return DOM_ACDATE_EXPR;
    }
}

