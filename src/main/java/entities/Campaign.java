package entities;

import java.util.Collection;
import java.util.Date;

public class Campaign {
    private String name;
    private Date startDate;
    private Double bid;
    private Integer campaignId;
    private Collection<Product>  products;
    private Boolean isActive;
}
