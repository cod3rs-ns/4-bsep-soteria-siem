package bsep.sw.hateoas.user;


public class AvailabilityData {

    private AvailabilityAttributes attributes;

    public AvailabilityData(final AvailabilityAttributes attributes) {
        this.attributes = attributes;
    }

    public AvailabilityAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(final AvailabilityAttributes attributes) {
        this.attributes = attributes;
    }
}
