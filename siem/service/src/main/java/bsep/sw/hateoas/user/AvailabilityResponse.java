package bsep.sw.hateoas.user;


public class AvailabilityResponse {

    private AvailabilityData data;

    public AvailabilityResponse(final String property, final Boolean available) {
        final AvailabilityAttributes attrs = new AvailabilityAttributes(property, available);
        this.data = new AvailabilityData(attrs);
    }

    public AvailabilityData getData() {
        return data;
    }

    public void setData(final AvailabilityData data) {
        this.data = data;
    }
}
