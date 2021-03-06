package godaddy.registry.jtoolkit2.tmdb.model;

/**
 *  Represents the "holder" element in the 'urn:ietf:params:xml:ns:tmNotice-1.0' namespace,
 *  defined in the 'tmNotice-1.0.xsd' schema.
 */
public class TmHolder {
    private String entitlement;
    private String name;
    private String organisation;
    private String email;
    private String voice;
    private String voiceExtension;
    private String fax;
    private String faxExtension;
    private TmAddress address;

    public void setName(String name) {
        this.name = name;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public void setVoiceExtension(String voiceExtension) {
        this.voiceExtension = voiceExtension;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setFaxExtension(String faxExtension) {
        this.faxExtension = faxExtension;
    }

    public void setAddress(TmAddress address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getEmail() {
        return email;
    }

    public String getVoice() {
        return voice;
    }


    public String getVoiceExtension() {
        return voiceExtension;
    }

    public String getFax() {
        return fax;
    }

    public String getFaxExtension() {
        return faxExtension;
    }

    public TmAddress getAddress() {
        return address;
    }

    public void setEntitlement(String entitlement) {
        this.entitlement = entitlement;
    }

    public String getEntitlement() {
        return entitlement;
    }
}
