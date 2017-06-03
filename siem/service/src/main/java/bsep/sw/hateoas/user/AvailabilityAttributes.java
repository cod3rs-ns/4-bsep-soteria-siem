package bsep.sw.hateoas.user;


public class AvailabilityAttributes {

    private String property;
    private Boolean available;

    public AvailabilityAttributes(final String property, final Boolean available) {
        this.property = property;
        this.available = available;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(final String property) {
        this.property = property;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(final Boolean available) {
        this.available = available;
    }
}
