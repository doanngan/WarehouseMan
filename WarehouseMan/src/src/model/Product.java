package src.model;

import java.util.Calendar;
import java.util.List;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.util.Tuple;

@DClass(schema="WarehouseMan")
public class Product {
	
	public static final String A_id = "id";
	public static final String A_name = "name";
	public static final String A_idate = "idate";
	public static final String A_iprice = "iprice";
	public static final String A_rptProductByType = "rptProductByType";
	
    @DAttr(name = A_id, id = true, type = Type.String, auto = true, length = 6,
            mutable = false, optional = false)
    private String id;
    
    private static int idCounter = 0;
    
    @DAttr(name = A_name, type = Type.String, length = 20, optional = false)
    private String name;
    
    @DAttr(name = A_idate, type = Type.String, length = 15, optional = false)
    private String idate;
    
    @DAttr(name = A_iprice, type = Type.Double, length = 15, optional = false)
    private Double iprice;
    
    @DAttr(name = "type", type = Type.Domain, length = 20, optional = false)
	  @DAssoc(ascName = "product-type", role = "type", 
	    ascType = AssocType.One2Many, endType = AssocEndType.Many, 
	    associate = @Associate(type = TypeOfProduct.class, cardMin = 1, cardMax = 1), dependsOn = true)
	  private TypeOfProduct type;
    
    @DAttr(name = "provider", type = Type.Domain, length = 20, optional = false)
	  @DAssoc(ascName = "product-provider", role = "provider", 
	    ascType = AssocType.One2Many, endType = AssocEndType.Many, 
	    associate = @Associate(type = Provider.class, cardMin = 1, cardMax = 1), dependsOn = true)
	  private Provider provider;
    
    @DAttr(name=A_rptProductByType,type=Type.Domain, serialisable=false, 
		      virtual=true)
		  private ProductsByTypeReport rptProductByType;

    
    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    @DOpt(type=DOpt.Type.RequiredConstructor)
    public Product(
            @AttrRef("name") String name,
            @AttrRef("idate") String idate,
            @AttrRef("iprice") Double iprice,
            @AttrRef("type") TypeOfProduct type,
            @AttrRef("provider") Provider provider) {
        this(null,name,idate,iprice,type,provider);
    }

    	@DOpt(type=DOpt.Type.DataSourceConstructor)
  	  public Product(@AttrRef("id") String id, @AttrRef("name") String name,
  			@AttrRef("idate") String idate, @AttrRef("iprice") Double iprice,
  			@AttrRef("type") TypeOfProduct type, @AttrRef("provider") Provider provider) 
  	  throws ConstraintViolationException {
    		
      this.id = nextID(id);
      this.name = name;
      this.idate = idate;
      this.iprice = iprice;
      this.type = type;
      this.provider = provider;
    }
    
    public String getId() {
    	return id;
    }
    	
    public String getName() {
        return name;
      }
    
    public void setName(String name){
    	this.name = name;
    }
    
    public String getIdate() {
        return idate;
      }

    public void setIdate(String idate){
    	this.idate = idate;
    }
    
    public Double getIprice() {
        return iprice; 
      }

    public void setIprice(Double iprice){
    	this.iprice = iprice;
    }
    
      public TypeOfProduct getType() {
        return type;
      }
     
      public void settype(TypeOfProduct type){
      	this.type = type;
      }
      
      public Provider getProvider(){
    	  return provider;
      }
      
      public void setProvider(Provider provider){
        	this.provider = provider;
        }
    
      public ProductsByTypeReport getRptProductByType() {
  	    return rptProductByType;
  	  } 
      
//      // override toString
//      /**
//       * @effects returns <code>this.id</code>
//       */
//      @Override
//      public String toString() {
//        return toString(true);
//      }
//
//      /**
//       * @effects returns <code>Product(id,name)</code>.
//       */
//      public String toString(boolean full) {
//        if (full)
//          return "Product(" + id + "," + name + "," + idate + "," + type + ","
//              + iprice + ")";
//        else
//          return "Product(" + id + ")";
//      }
      
      @Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((id == null) ? 0 : id.hashCode());
	    return result;
	  }

	  @Override
	  public boolean equals(Object obj) {
	    if (this == obj)
	      return true;
	    if (obj == null)
	      return false;
	    if (getClass() != obj.getClass())
	      return false;
	    Product other = (Product) obj;
	    if (id == null) {
	      if (other.id != null)
	        return false;
	    } else if (!id.equals(other.id))
	      return false;
	    return true;
	  }
	  
	  private String nextID(String id) throws ConstraintViolationException {
		    if (id == null) { // generate a new id
		      if (idCounter == 0) {
		        idCounter = Calendar.getInstance().get(Calendar.YEAR);
		      } else {
		        idCounter++;
		      }
		      return "P" + idCounter;
		    } else {
		      // update id
		      int num;
		      try {
		        num = Integer.parseInt(id.substring(1));
		      } catch (RuntimeException e) {
		        throw new ConstraintViolationException(
		            ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] { id });
		      }
		      
		      if (num > idCounter) {
		        idCounter = num;
		      }
		      
		      return id;
		    }
		  }
	  
	  @DOpt(type=DOpt.Type.AutoAttributeValueSynchroniser)
	  public static void updateAutoGeneratedValue(
	      DAttr attrib,
	      Tuple derivingValue, 
	      Object minVal, 
	      Object maxVal) throws ConstraintViolationException {
	    
	    if (minVal != null && maxVal != null) {
	      //TODO: update this for the correct attribute if there are more than one auto attributes of this class 

	      String maxId = (String) maxVal;
	      
	      try {
	        int maxIdNum = Integer.parseInt(maxId.substring(1));
	        
	        if (maxIdNum > idCounter) // extra check
	          idCounter = maxIdNum;
	        
	      } catch (RuntimeException e) {
	        throw new ConstraintViolationException(
	            ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] {maxId});
	      }
	    }
	  }
	}
     