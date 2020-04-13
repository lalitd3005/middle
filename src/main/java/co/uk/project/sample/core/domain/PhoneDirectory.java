package co.uk.project.sample.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

//Intersect Model  (COMPOSITE PRIMARY KEY = customerId + phoneId)
public class PhoneDirectory {

    private Customer customerId;
    private Phone phoneId;

    public PhoneDirectory(){}
    public PhoneDirectory(Customer customerId, Phone phoneId) {
        this.customerId = customerId;
        this.phoneId = phoneId;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Phone getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Phone phoneId) {
        this.phoneId = phoneId;
    }
    public int hashcode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }


}
