package godaddy.registry.jtoolkit2.se.fund;

import godaddy.registry.jtoolkit2.se.ExtendedObjectType;
import godaddy.registry.jtoolkit2.se.InfoCommand;

/**
 * Use this to request information about fund objects. Instances of this class
 * generate RFC5730 compliant fund info EPP command service elements via the toXML method.
 *
 * @see godaddy.registry.jtoolkit2.se.fund.FundInfoResponse
 */
public class FundInfoCommand extends InfoCommand {
    /**
     * Create a fund info command.
     */
    public FundInfoCommand() {
        super(ExtendedObjectType.FUND);
    }

}
